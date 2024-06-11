import static org.junit.Assert.*;

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

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;
    
    @Mock
    private DelegateExecution delegateExecution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> { return new Object(); };
            }
        };
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        Object result = baseRuntimeFlowableDelegate.get();
        assertNotNull(result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        Object result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithExecutionException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

}