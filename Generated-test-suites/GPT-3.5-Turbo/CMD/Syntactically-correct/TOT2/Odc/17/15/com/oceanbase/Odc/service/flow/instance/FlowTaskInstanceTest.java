import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = mock(FlowTaskInstance.class);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        // Verification logic for abortion
    }

    @Test
    public void testCancelSuccess() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        // Verification logic for successful cancellation
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelFailure() {
        flowTaskInstance.cancel(false);
        // Expected UnsupportedOperationException
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertFalse(flowTaskInstance.isDone());
        // Verification logic for task in progress
        // Optionally, simulate task completion and test again
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1000, TimeUnit.MILLISECONDS)).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("Result", flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    // Add more test cases for other methods as required

}