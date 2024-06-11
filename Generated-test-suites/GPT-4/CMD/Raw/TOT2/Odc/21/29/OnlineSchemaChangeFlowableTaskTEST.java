import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private TaskService taskService;

    @Mock
    private DelegateExecution execution;

    // Initialize the class to be tested
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        // Initialize the task
        task = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void testStartMethod() {
         // Mock required method calls, objects and return values
         long taskId = 1L;

         when(execution.getVariable("creator")).thenReturn("creator");
         when(taskService.start(taskId)).thenReturn(anyObject());

         try {
             task.start(taskId, taskService, execution);
         } catch (Exception e) {
             Assert.fail();
         }

         // Verify that the method to start task has been called
         verify(taskService, times(1)).start(taskId);
    }
}