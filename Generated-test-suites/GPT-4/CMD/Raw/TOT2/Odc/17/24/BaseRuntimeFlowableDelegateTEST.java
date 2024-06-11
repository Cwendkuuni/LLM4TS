
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaseRuntimeFlowableDelegateTest {
  
    @InjectMocks
    BaseRuntimeFlowableDelegate<String> target;

    @Mock
    DelegateExecution delegateExecution;
    @Mock
    Callable<String> callable;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        when(target.initCallable(delegateExecution)).thenReturn(callable);
        when(callable.call()).thenReturn("output");

        target.run(delegateExecution);

        assertEquals("output", target.get());
        assertTrue(target.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testRunThrowsException() throws Exception {
        when(target.initCallable(delegateExecution)).thenReturn(callable);
        when(callable.call()).thenThrow(new RuntimeException());

        target.run(delegateExecution);

        assertFalse(target.isDone());
        target.get();
    }

    @Test
    public void testInitCallable() throws Exception{
        // This is depending on the subclass implementation. Add concrete tests in subclass's test class
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        when(target.initCallable(delegateExecution)).thenReturn(callable);
        when(callable.call()).thenReturn("output");

        target.run(delegateExecution);

        assertEquals("output", target.get(1000, TimeUnit.MILLISECONDS));
        assertTrue(target.isDone());
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutThrowsException() throws Exception {
        when(target.initCallable(delegateExecution)).thenReturn(callable);
        // simulate long running task
        doAnswer(invocation -> {
            Thread.sleep(2000);
            return null;
          }).when(callable).call();

        Thread t1 = new Thread(() -> {
           try {
             target.run(delegateExecution);
           } catch (Exception ignored) {
           }
           });

        t1.start();

        assertFalse(target.isDone());
        target.get(1000, TimeUnit.MILLISECONDS);
    }
}
