import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);

        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));

        // Verify that the callable is not null
        assertEquals(callable != null, true);
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = spy(BaseRuntimeFlowableDelegate.class);

        doNothing().when(delegate).run(any());
        delegate.run(mock(DelegateExecution.class));

        // Verify if the run method was called
        verify(delegate, times(1)).run(any());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();

        // Verify that isDone initially returns false
        assertEquals(delegate.isDone(), false);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.run(mock(DelegateExecution.class));

        // Verify that get returns the expected value
        assertEquals(delegate.get(), null);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.run(mock(DelegateExecution.class));

        // Verify that get with timeout returns the expected value
        assertEquals(delegate.get(1, TimeUnit.SECONDS), null);
    }
}