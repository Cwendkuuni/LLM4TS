
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.oceanbase.odc.core.shared.constant.TaskType;
import org.flowable.engine.delegate.DelegateExecution;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    return new Object();
                };
            }
        };
        Callable<Object> callable = delegate.initCallable(delegateExecution);
        assertNotNull(callable);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();
        assertFalse(delegate.isDone());
        delegate.get();
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.run(delegateExecution);
        assertNotNull(delegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    return new Object();
                };
            }
        };
        delegate.run(delegateExecution);

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(delegate.getTargetTaskInstanceId());
        delegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    return new Object();
                };
            }
        };
        delegate.run(delegateExecution);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        // Verify that status is updated correctly
        assertEquals(FlowStatus.COMPLETED, delegate.getFlowInstanceId());
    }

}
