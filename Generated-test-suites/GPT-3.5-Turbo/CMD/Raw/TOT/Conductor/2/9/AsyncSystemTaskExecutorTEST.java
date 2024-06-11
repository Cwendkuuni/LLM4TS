import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock,
                queueDAOMock,
                metadataDAOMock,
                conductorPropertiesMock,
                workflowExecutorMock);
    }

    @Test
    public void testExecute_WhenTaskNotFound_LogErrorAndRemoveFromQueue() {
        // Setup
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        
        // Mock
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);
        
        // Execute
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        // Verify
        verify(queueDAOMock, times(1)).remove(eq(systemTask.getTaskType()), eq(taskId));
    }

    @Test
    public void testExecute_WhenTaskAlreadyCompleted_RemoveFromQueue() {
        // Setup
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // Mock
        TaskModel task = new TaskModel();
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);
        when(queueDAOMock.getQueueName(eq(task))).thenReturn("testQueue");
        task.setStatus(TaskModel.Status.COMPLETED);

        // Execute
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify
        verify(queueDAOMock, times(1)).remove(eq("testQueue"), eq(taskId));
    }
    
    // Add more test cases for other methods in the AsyncSystemTaskExecutor class

}