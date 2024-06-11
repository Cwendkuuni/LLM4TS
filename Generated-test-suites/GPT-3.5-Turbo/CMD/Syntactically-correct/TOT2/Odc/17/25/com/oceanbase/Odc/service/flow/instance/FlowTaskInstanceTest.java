import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance();
    }

    @Test
    public void testBindServiceTask() {
        // Test if binding a service task is successful
        BaseRuntimeFlowableDelegate<?> targetTask = new BaseRuntimeFlowableDelegate<>();
        flowTaskInstance.bindServiceTask(targetTask);
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testConfirmExecute() {
        // Test confirming task execution
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        // Test aborting task
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        // Test canceling task
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(false));
    }

    @Test
    public void testIsDone() {
        // Test checking if the task is done
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() {
        // Test getting the task result
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() {
        // Test getting the task result with timeout
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        // Test checking the resource type
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Test getting the target delegate class
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }
}