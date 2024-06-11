import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        // Typical use case - task exists
        TaskModel task = new TaskModel();
        task.setTaskId("1");
        when(executionDAOFacade.getTaskModel("1")).thenReturn(task);
        
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        
        asyncSystemTaskExecutor.execute(systemTask, "1");
        
        verify(executionDAOFacade, times(1)).getTaskModel("1");
        verify(queueDAO, times(1)).remove(anyString(), anyString());

        // Edge case - task does not exist
        when(executionDAOFacade.getTaskModel("2")).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, "2");
        
        verify(executionDAOFacade, times(1)).getTaskModel("2");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        
        // Error scenario - exception during execution
        when(executionDAOFacade.getTaskModel("3")).thenThrow(new RuntimeException("Test Exception"));
        
        asyncSystemTaskExecutor.execute(systemTask, "3");
        
        verify(executionDAOFacade, times(1)).getTaskModel("3");
    }
}