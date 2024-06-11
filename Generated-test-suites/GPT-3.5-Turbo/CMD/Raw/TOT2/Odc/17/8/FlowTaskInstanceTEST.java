import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Before
    public void setUp() {
        flowTaskInstance = mock(FlowTaskInstance.class);
        targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals(flowTaskInstance.getTargetTaskHandle(), targetTaskHandle);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(flowTaskInstance.getStatus(), FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(flowTaskInstance.getStatus(), FlowNodeStatus.ABORTED);
    }

    @Test
    public void testCancel() {
        when(targetTaskHandle.cancel(anyBoolean())).thenReturn(true);
        assertTrue(flowTaskInstance.cancel(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelTaskNotOnMachine() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testIsDone() {
        when(targetTaskHandle.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneTaskNotOnMachine() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(targetTaskHandle.get()).thenReturn("Result");
        assertEquals(flowTaskInstance.getResult(), "Result");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultTaskNotOnMachine() throws InterruptedException, ExecutionException {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn("Result");
        assertEquals(flowTaskInstance.getResult(1, TimeUnit.SECONDS), "Result");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutTaskNotOnMachine() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

}