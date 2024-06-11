import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallableWithNull() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MockBaseRuntimeFlowableDelegate();
        try {
            delegate.run(null);
            fail("Expected ServiceTaskError to be thrown");
        } catch (ServiceTaskError e) {
            assertNotNull(e.getCause());
        } catch (Exception e) {
            fail("Expected ServiceTaskError to be thrown");
        }
    }

    @Test
    public void testRuntimeExceptionDuringInitialization() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MockBaseRuntimeFlowableDelegate() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Initialization error");
            }
        };
        try {
            delegate.run(null);
            fail("Expected ServiceTaskError to be thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Initialization error", e.getCause().getMessage());
        } catch (Exception e) {
            fail("Expected ServiceTaskError to be thrown");
        }
    }

    @Test
    public void testServiceTaskExecutionFailure() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MockBaseRuntimeFlowableDelegate() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new RuntimeException("Service task execution error");
                };
            }
        };
        try {
            delegate.run(null);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Service task execution error", e.getMessage());
        } catch (Exception e) {
            fail("Expected RuntimeException to be thrown");
        }
    }

    @Test
    public void testTimeoutExceptionInGet() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MockBaseRuntimeFlowableDelegate();
        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail("Expected TimeoutException to be thrown");
        } catch (TimeoutException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Expected TimeoutException to be thrown");
        }
    }

    @Test
    public void testExecutionExceptionInGet() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MockBaseRuntimeFlowableDelegate() {
            @Override
            public T get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new RuntimeException("Execution error"));
            }
        };
        try {
            delegate.get();
            fail("Expected ExecutionException to be thrown");
        } catch (ExecutionException e) {
            assertEquals("Execution error", e.getCause().getMessage());
        } catch (Exception e) {
            fail("Expected ExecutionException to be thrown");
        }
    }

    private static class MockBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return () -> null;
        }
    }

}