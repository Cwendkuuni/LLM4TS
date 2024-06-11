import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    // Test case for execute method with a valid task ID
    @Test
    public void testExecute_validTaskId() {
        // Mock objects
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "1234";

        // Execute method
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor();
        executor.execute(systemTask, taskId);

        // Verify task is retrieved and executed
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(systemTask, times(1)).start();
    }

    // Test case for execute method with a null task ID
    @Test
    public void testExecute_nullTaskId() {
        // Mock objects
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = null;

        // Execute method
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor();
        executor.execute(systemTask, taskId);

        // Verify task is not retrieved and systemTask is not started
        verify(executionDAOFacade, never()).getTaskModel(any());
        verify(systemTask, never()).start();
    }

    // Test case for execute method with a non-existent task
    @Test
    public void testExecute_nonExistentTask() {
        // Mock objects
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "5678";

        // Execute method
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor();
        executor.execute(systemTask, taskId);

        // Verify error message is logged
        verify(logger, times(1)).error(anyString());
    }
}