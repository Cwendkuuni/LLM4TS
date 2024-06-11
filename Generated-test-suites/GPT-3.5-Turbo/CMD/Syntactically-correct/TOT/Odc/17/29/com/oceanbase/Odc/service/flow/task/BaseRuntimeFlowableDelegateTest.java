import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = spy(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testInitCallable_Success() {
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);

        assertNotNull(callable);
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException {
        Object expectedResult = new Object();
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));

        Object result = baseRuntimeFlowableDelegate.get();

        assertEquals(expectedResult, result);
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));

        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGet_ExecutionException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));
        baseRuntimeFlowableDelegate.setThrown(new Exception());

        baseRuntimeFlowableDelegate.get();
    }

}