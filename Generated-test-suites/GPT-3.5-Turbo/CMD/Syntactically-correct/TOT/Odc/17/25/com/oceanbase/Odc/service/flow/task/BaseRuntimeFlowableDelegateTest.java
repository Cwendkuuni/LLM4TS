import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;
    private DelegateExecution executionMock;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    return new Object();
                };
            }
        };

        executionMock = mock(DelegateExecution.class);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Future<Object> future = baseRuntimeFlowableDelegate;
        assertNull(baseRuntimeFlowableDelegate.get());
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Future<Object> future = baseRuntimeFlowableDelegate;
        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(executionMock);
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }
}