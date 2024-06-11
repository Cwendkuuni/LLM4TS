import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowSystemTask systemTask;

    @Mock
    private TaskModel task;

    @Before
    public void setup() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade,
            queueDAO,
            metadataDAO,
            new ConductorProperties(),
            workflowExecutor);
            
        initMocks(this);
        
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(systemTask.isAsyncComplete(any())).thenReturn(true);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    }

    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(systemTask, "taskID");
        
        verify(task, atLeastOnce()).getStatus();
        verify(systemTask, atLeastOnce()).isAsyncComplete(task);
    }
}