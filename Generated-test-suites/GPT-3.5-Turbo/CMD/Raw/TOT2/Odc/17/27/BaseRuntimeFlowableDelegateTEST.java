import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = delegate.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);
        String result = delegate.get();
        assertNotNull(result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);
        String result = delegate.get(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }
}