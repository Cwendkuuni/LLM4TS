package com.netflix.conductor.core.execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    private String taskId = "task-id";
    private TaskModel task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        task = new TaskModel();
        task.setTaskId(taskId);
    }

    @Test
    public void testExecuteTaskNotFound() {
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).remove(any(String.class), any(String.class));
    }

    @Test
    public void testExecuteTaskFoundAndTerminal() {
        task.setStatus(TaskModel.Status.COMPLETED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).remove(any(String.class), any(String.class));
    }

    @Test
    public void testExecuteInProgressLimitExceeded() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        Mockito.when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).postpone(any(String.class), any(String.class), any(Integer.class), any(Long.class));
    }

    @Test
    public void testExecuteRateLimitExceeded() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        Mockito.verify(queueDAO).postpone(any(String.class), any(String.class), any(Integer.class), any(Long.class));
    }

    // Error scenarios, edge cases and tests for TaskModel.Status.SCHEDULED, 
    // TaskModel.Status.IN_PROGRESS and TaskModel.Status.terminal states would also be required to cover 
    // all relevant code paths within the execute method of AsyncSystemTaskExecutor.
}