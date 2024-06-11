import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private QueueDAO queueDAO = mock(QueueDAO.class);
    private MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor
        );
    }

    @Test
    public void testExecute() {
        // Typical Use Case
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        // Verify that the task is executed and persisted correctly
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));

        // Edge Case
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        // Verify that the task is postponed when task status is SCHEDULED
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());

        // Error Scenario
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        // Verify that task is removed from queue when an exception occurs
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

}