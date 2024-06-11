import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        // Typical use case
        DelegateExecution execution = mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        Callable<String> callable = delegate.initCallable(execution);
        try {
            assertEquals("Test Callable", callable.call());
        } catch (Exception e) {
            fail("Exception thrown when executing callable");
        }
    }

    @Test
    public void testRun() {
        // Typical use case
        DelegateExecution execution = mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        try {
            delegate.run(execution);
        } catch (Exception e) {
            fail("Exception thrown when running delegate");
        }
    }

    @Test
    public void testGet() {
        // Typical use case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        try {
            delegate.run(null);
            String result = delegate.get();
            assertEquals("Test Callable", result);
        } catch (Exception e) {
            fail("Exception thrown when getting result");
        }
    }

    @Test
    public void testGetWithTimeout() {
        // Edge case: Timeout
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(2000);
                    return "Test Callable";
                };
            }
        };

        try {
            delegate.run(null);
            delegate.get(1, TimeUnit.SECONDS);
            fail("TimeoutException not thrown");
        } catch (TimeoutException e) {
            // Expected
        } catch (Exception e) {
            fail("Exception other than TimeoutException thrown");
        }
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Error scenario: Exception thrown in update
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        try {
            delegate.run(null);
            delegate.updateFlowInstanceStatus(FlowStatus.ERROR);
            fail("Exception not thrown in update");
        } catch (Exception e) {
            // Expected
        }
    }

}