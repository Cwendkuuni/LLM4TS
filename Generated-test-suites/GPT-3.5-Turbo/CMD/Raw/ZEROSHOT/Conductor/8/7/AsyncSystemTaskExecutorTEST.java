import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Test
    public void testExecute_TaskNotFound() {
        // mock objects
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        // mock behavior
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // execute method
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // verify behavior
        // check if error message logged
        verify(LOGGER).error("TaskId: {} could not be found while executing {}", taskId, systemTask);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        // mock objects
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        // mock behavior
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // execute method
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // verify behavior
        // check if task removed from queue
        verify(queueDAO).remove(anyString(), anyString());
    }

    // add more test methods for other scenarios

}