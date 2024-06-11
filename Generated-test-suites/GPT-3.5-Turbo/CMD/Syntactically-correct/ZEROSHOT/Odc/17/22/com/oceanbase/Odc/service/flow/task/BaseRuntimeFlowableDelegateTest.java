import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;

import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testIsDone() {
        assert !baseRuntimeFlowableDelegate.isDone();
        baseRuntimeFlowableDelegate.run(null);
        assert baseRuntimeFlowableDelegate.isDone();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(null);
        assert baseRuntimeFlowableDelegate.get().equals("Test");
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(null);
        assert baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS).equals("Test");
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.run(null);
        baseRuntimeFlowableDelegate.execute(null);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

}