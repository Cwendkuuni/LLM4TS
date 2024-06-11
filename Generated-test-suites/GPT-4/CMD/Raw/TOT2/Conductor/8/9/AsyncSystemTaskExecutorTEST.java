import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade mockExecutionDAOFacade;
    private QueueDAO mockQueueDAO;
    private MetadataDAO mockMetadataDAO;
    private ConductorProperties mockConductorProperties;
    private WorkflowExecutor mockWorkflowExecutor;
    private TaskModel mockTask;
    private WorkflowSystemTask mockWorkflowSystemTask;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        mockExecutionDAOFacade = mock(ExecutionDAOFacade.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockMetadataDAO = mock(MetadataDAO.class);
        mockConductorProperties = mock(ConductorProperties.class);
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockTask = mock(TaskModel.class);
        mockWorkflowSystemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                mockExecutionDAOFacade,
                mockQueueDAO,
                mockMetadataDAO,
                mockConductorProperties,
                mockWorkflowExecutor);
    }

    @Test
    public void testExecuteTaskNotFound() {
        String taskId = "123";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);
        verify(mockQueueDAO, times(0)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskIsTerminal() {
        String taskId = "123";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTask);
        doNothing().when(mockQueueDAO).remove(anyString(), anyString());
        when(mockTask.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);
        verify(mockQueueDAO, times(1)).remove(anyString(), anyString());
    }
    
    // similar tests for other edge cases and error scenarios
}