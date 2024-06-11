import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteWithNonExistentTask() {
        // Test when task does not exist
        String taskId = "nonExistentTask";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // Execute the method
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(logger, times(1)).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
        verify(queueDAO, times(1)).remove(systemTask.getTaskType(), taskId);
    }

    @Test
    public void testExecuteWithCompletedTask() {
        // Test when task is already completed
        String taskId = "completedTask";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // Execute the method
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(logger, times(1)).info("Task {}/{} was already completed.", task.getTaskType(), task.getTaskId());
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    // Add more test cases here covering typical use cases, edge cases, and error scenarios

}