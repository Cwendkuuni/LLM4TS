import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private BaseRuntimeFlowableDelegate<?> targetMock;

    @Before
    public void setUp() {
        targetMock = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance = new FlowTaskInstance(TaskType.TEST, 123L, 456L, new ExecutionStrategyConfig(), 
            true, false, new RuntimeDelegateMapper(), new FlowableAdaptor(), new EventPublisher(),
            mock(TaskService.class), mock(NodeInstanceEntityRepository.class),
            mock(SequenceInstanceRepository.class), mock(ServiceTaskInstanceRepository.class));
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(targetMock);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(targetMock);
        flowTaskInstance.cancel(false);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(targetMock);
        assertEquals(targetMock.get(), flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(targetMock);
        assertEquals(targetMock.get(10, TimeUnit.SECONDS), flowTaskInstance.getResult(10, TimeUnit.SECONDS));
    }

    // Test edge cases and exception handling scenarios below:

    // Edge case: handle when task is not present on this machine
    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskWhenNotPresent() {
        flowTaskInstance.bindServiceTask(null);
    }

    // Edge case: handle when task is done
    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenDone() {
        flowTaskInstance.cancel(false);
    }

    // Edge case: handle exception when getResult is called without task binding
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutBinding() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    // Edge case: handle exception when getResult with timeout is called without task binding
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutWithoutBinding() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(10, TimeUnit.SECONDS);
    }

}