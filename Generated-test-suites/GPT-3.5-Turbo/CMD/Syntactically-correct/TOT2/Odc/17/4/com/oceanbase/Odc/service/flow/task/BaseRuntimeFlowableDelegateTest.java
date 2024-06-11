import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));
        assertNotNull(callable);
    }

    @Test
    public void testRun() {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate.run(execution);

        assertEquals("Test return value", delegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate.run(execution);

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetWithException() {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate.run(execution);

        RuntimeException exception = new RuntimeException("Test exception");
        delegate.setThrown(exception);

        try {
            delegate.get();
        } catch (ExecutionException e) {
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

}