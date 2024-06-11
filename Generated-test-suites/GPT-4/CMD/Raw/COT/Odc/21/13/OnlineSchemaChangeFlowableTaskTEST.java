
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask task;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private DelegateExecution execution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testStart() throws Exception {
        Long taskId = 1L;
        task.start(taskId, taskService, execution);
        verify(taskService).start(taskId);
    }
    
    @Test
    public void testIsSuccessful() {
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(task.isFailure());
    }
    
    @Test
    public void testOnSuccessful() {
        Long taskId = 1L;
        task.onSuccessful(taskId, taskService);
        verify(taskService).detail(taskId);
    }
    
    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        task.onProgressUpdate(taskId, taskService);
        verify(taskService).detail(taskId);
    }
    
    @Test
    public void testCancel() {
        Long taskId = 1L;
        assertFalse(task.cancel(true, taskId, taskService));
    }

    @Test
    public void testIsCancelled() {
        assertFalse(task.isCancelled());
    }

}
