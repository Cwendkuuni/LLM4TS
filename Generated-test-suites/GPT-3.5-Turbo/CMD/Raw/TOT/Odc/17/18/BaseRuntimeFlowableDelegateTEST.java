import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
                return () -> "TestCallable";
            }
        };
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("TestProcessDefinitionId");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

        assertEquals("TestProcessDefinitionId", baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals("TestActivityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(Long.valueOf(1L), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TaskType.TYPE1, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        baseRuntimeFlowableDelegate.run(null);
        assertEquals("TestCallable", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        baseRuntimeFlowableDelegate.run(null);
        assertEquals("TestCallable", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws ExecutionException, InterruptedException, TimeoutException {
        baseRuntimeFlowableDelegate.run(null);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

}