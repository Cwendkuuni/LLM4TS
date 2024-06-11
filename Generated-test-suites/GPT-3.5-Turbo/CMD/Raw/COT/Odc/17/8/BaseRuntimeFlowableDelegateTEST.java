
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        assertNull(baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_WithException() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout_Exception() throws Exception {
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }
}
