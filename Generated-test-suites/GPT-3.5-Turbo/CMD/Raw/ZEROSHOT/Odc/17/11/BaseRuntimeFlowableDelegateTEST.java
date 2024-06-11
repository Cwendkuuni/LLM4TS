import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = spy(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        assertNull(baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new RuntimeException("Test Exception");
                };
            }
        };
        baseRuntimeFlowableDelegate.run(null);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(2000);
                    return new Object();
                };
            }
        };
        baseRuntimeFlowableDelegate.run(null);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetWithSuccess() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> new Object();
            }
        };
        baseRuntimeFlowableDelegate.run(null);
        assertNotNull(baseRuntimeFlowableDelegate.get());
    }

}