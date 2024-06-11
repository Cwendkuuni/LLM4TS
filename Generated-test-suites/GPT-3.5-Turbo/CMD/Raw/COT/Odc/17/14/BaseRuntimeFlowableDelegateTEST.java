
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        
        doCallRealMethod().when(delegate).run(execution);
        when(delegate.initCallable(execution)).thenReturn(() -> null);
        
        delegate.run(execution);
        
        assertTrue(delegate.isDone());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_Exception() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        doCallRealMethod().when(delegate).run(execution);
        when(delegate.initCallable(execution)).thenThrow(new RuntimeException());

        delegate.run(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.done = true;
        delegate.returnObject = new Object();

        assertEquals(delegate.get(), delegate.returnObject);
    }

    @Test(expected = ExecutionException.class)
    public void testGet_Exception() throws InterruptedException, ExecutionException {
        delegate.done = true;
        delegate.thrown = new Exception();

        delegate.get();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.done = true;
        delegate.returnObject = new Object();
        
        assertEquals(delegate.get(1000L, TimeUnit.MILLISECONDS), delegate.returnObject);
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.done = false;
        
        delegate.get(1000L, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout_Exception() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.done = true;
        delegate.thrown = new Exception();

        delegate.get(1000L, TimeUnit.MILLISECONDS);
    }
}
