
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        ConductorProperties conductorProperties = new ConductorProperties();
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_CompletedTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is removed from the queue
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_ScheduledTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task execution is started
        verify(systemTask, times(1)).start(any(), eq(task), any());
    }

    @Test
    public void testExecute_InProgressTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task execution is continued
        verify(systemTask, times(1)).execute(any(), eq(task), any());
    }

    @Test
    public void testExecute_ErrorExecutingTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(task);
        doThrow(new RuntimeException()).when(systemTask).execute(any(), eq(task), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the error is handled and the task is updated
        verify(executionDAOFacade, times(1)).updateTask(eq(task));
    }
}
