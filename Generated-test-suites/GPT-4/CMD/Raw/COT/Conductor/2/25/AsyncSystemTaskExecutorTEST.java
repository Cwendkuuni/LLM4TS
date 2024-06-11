import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

// Other necessary imports...

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
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
    }

    @Test
    public void testExecuteWithNullTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new NullPointerException());
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWithNonNullTask() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);        
        TaskModel taskModel = mock(TaskModel.class);
        
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    // Include more test cases to cover various scenarios and edge cases...
}