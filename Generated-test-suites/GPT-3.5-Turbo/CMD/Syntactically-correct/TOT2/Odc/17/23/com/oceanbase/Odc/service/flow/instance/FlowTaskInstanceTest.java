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
        // Test binding service task to the FlowTaskInstance
        BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals(baseRuntimeFlowableDelegate, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testAbort() {
        // Test aborting the FlowTaskInstance
        flowTaskInstance.abort();
        // Verify if the task is in aborted state
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        // Test confirming execution of FlowTaskInstance
        flowTaskInstance.confirmExecute();
        // Verify if the task is completing execution
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        // Test canceling the task
        flowTaskInstance.cancel(true);
        // Verify UnsupportedOperationException is thrown if task is not on current machine
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        // Test checking if task is done
        flowTaskInstance.isPresentOnThisMachine();
        // Verify UnsupportedOperationException is thrown if task is not on current machine
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Test getting result of task
        flowTaskInstance.getResult();
        // Verify UnsupportedOperationException is thrown if task is not on current machine
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Test getting result of task with timeout
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        // Verify UnsupportedOperationException is thrown if task is not on current machine
    }
}