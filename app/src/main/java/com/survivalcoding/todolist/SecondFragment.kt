package com.survivalcoding.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.survivalcoding.todolist.databinding.FragmentSecondBinding
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedTodo?.let {
            binding.todoEditText.setText(it.title)
            binding.calendarView.date = it.date
        }

        val calendar = Calendar.getInstance()

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }


        binding.doneFab.setOnClickListener {
            if (binding.todoEditText.text.toString().isNotEmpty()) {
                if (viewModel.selectedTodo != null) {
                    viewModel.updateTodo(
                        binding.todoEditText.text.toString(),
                        calendar.timeInMillis
                    )
                } else {
                    viewModel.addTodo(
                        binding.todoEditText.text.toString(),
                        calendar.timeInMillis
                    )
                }
                findNavController().popBackStack()
            }
        }

        binding.deleteFab.setOnClickListener {
            viewModel.deleteTodo(viewModel.selectedTodo!!.id)
            findNavController().popBackStack()
        }

        // 선택된 할 일이 없을 때는 지우기 버튼 감추기
        if (viewModel.selectedTodo == null) {
            binding.deleteFab.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}