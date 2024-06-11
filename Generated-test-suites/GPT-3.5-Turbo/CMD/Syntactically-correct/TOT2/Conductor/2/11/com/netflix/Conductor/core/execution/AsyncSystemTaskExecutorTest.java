import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    private QueueDAO queueDAO;
    
    @Mock
    private MetadataDAO metadataDAO;
    
    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNull() {
        // Prepare
        String taskId = "task123";
        
        // Execute
        asyncSystemTaskExecutor.execute(null, taskId);
        
        // Verify
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_taskNotFound() {
        // Prepare
        String taskId = "task123";
        
        // Mock
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(eq(taskId))).thenReturn(null);
        
        // Execute
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);
        
        // Verify
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    // Add more test cases for other methods if needed

}