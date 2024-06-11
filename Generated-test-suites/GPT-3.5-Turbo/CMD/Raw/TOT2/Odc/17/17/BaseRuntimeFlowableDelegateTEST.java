import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = spy(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<String> callable = delegate.initCallable(execution);

        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = spy(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        doNothing().when(delegate).initTargetTaskInstanceId(execution);
        doReturn(() -> "test").when(delegate).initCallable(execution);

        delegate.run(execution);

        assertTrue(delegate.isDone());
        assertEquals("test", delegate.get());
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = spy(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        doNothing().when(delegate).initTargetTaskInstanceId(execution);
        doReturn(() -> "test").when(delegate).initCallable(execution);

        delegate.run(execution);

        assertEquals("test", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testRunWithException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = spy(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);

        doThrow(new NullPointerException()).when(delegate).initTargetTaskInstanceId(execution);
        doReturn(() -> "test").when(delegate).initCallable(execution);

        delegate.run(execution);

        delegate.get();
    }

    // Add more test cases for other methods in the class

}