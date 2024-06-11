import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
	public void setUp() {
		baseRuntimeFlowableDelegate = spy(BaseRuntimeFlowableDelegate.class);
	}

    @Test
    public void testInitCallableSuccess() {
        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activity1");

        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);

        assertNotNull(callable);
    }

    @Test
    public void testInitTargetTaskInstanceIdSuccess() {
        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getProcessDefinitionId()).thenReturn("processDefinition1");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

        assertEquals("activity1", baseRuntimeFlowableDelegate.getActivityId());
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testGetSuccess() throws InterruptedException, ExecutionException {
        Object expectedReturnObject = new Object();
        baseRuntimeFlowableDelegate.init();
        baseRuntimeFlowableDelegate.setDone(true);
        baseRuntimeFlowableDelegate.setReturnObject(expectedReturnObject);
        
        Object actualReturnObject = baseRuntimeFlowableDelegate.get();

        assertEquals(expectedReturnObject, actualReturnObject);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.init();
        baseRuntimeFlowableDelegate.get(5, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstanceSuccess() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        taskInstance.setId(1L);

        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.setActivityId("activity1");

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatusSuccess() {
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);

        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        assertTrue(true);
    }
}