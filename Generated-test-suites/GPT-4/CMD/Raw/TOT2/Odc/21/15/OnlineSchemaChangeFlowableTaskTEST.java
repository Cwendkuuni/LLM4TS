
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.mockito.Mock;

public class OnlineSchemaChangeFlowableTaskTest {
    
    @Mock
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Test
    public void testStart() {
       //setup your mock and parameters
       Long taskId = 1L;
       TaskService taskService = mock(TaskService.class);
       DelegateExecution execution = mock(DelegateExecution.class);
       // make the call
       onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

       // verify and assert results
       // for instance, assert that start method does not return a null
       assertNotNull(onlineSchemaChangeFlowableTask.start(taskId, taskService, execution));
    }
}
