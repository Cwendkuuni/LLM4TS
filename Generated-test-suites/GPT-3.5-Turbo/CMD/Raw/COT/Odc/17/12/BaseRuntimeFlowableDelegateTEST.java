import static org.junit.Assert.*;
import org.junit.Test;
import org.flowable.engine.delegate.DelegateExecution;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        };

        Callable<String> callable = delegate.initCallable(null);
        try {
            String result = callable.call();
            assertEquals("Test Result", result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        delegate.setFlowInstanceId(12345L);
        delegate.initRetryExecutor(1000, 3);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        // Assert that no exception is thrown
    }
}