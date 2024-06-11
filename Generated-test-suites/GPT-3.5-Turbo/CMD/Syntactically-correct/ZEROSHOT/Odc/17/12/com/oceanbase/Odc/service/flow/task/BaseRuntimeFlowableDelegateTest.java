import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<Object> runtimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        runtimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
    }

    @Test
    public void testIsDone() {
        assertFalse(runtimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException {
        assertNull(runtimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException {
        assertNull(runtimeFlowableDelegate.get(1, java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TYPE1);
        taskInstance.setStrategyConfig(new ExecutionStrategyConfig());

        runtimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        // Verify that the taskInstance was bound successfully
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        runtimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

        // Verify that the update was successful
        // You can add more verifications here based on your implementation
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_Exception() throws Exception {
        doThrow(Exception.class).when(execution).getCurrentActivityId();
        runtimeFlowableDelegate.run(execution);

        assertTrue(runtimeFlowableDelegate.isDone());
    }

}