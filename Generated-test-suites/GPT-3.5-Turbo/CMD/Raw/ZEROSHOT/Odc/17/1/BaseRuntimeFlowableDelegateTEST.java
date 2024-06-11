import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = spy(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testInitTargetTaskInstanceId() throws InterruptedException {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.getFlowableAdaptor()).thenReturn(mock(FlowableAdaptor.class));
        when(baseRuntimeFlowableDelegate.getFlowInstanceRepository()).thenReturn(mock(FlowInstanceRepository.class));
        doReturn(Optional.of(Optional.of(1L))).when(baseRuntimeFlowableDelegate).retryExecutor.run(any(), any());
        doReturn(Optional.of(mock(FlowTaskInstance.class))).when(baseRuntimeFlowableDelegate).flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong());

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(Long.valueOf(1L), baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(Long.valueOf(0), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWhenThrownException() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.setDone(true);
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.setActivityId("activityId");
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(mock(FlowInstanceRepository.class));

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(baseRuntimeFlowableDelegate.getFlowInstanceRepository(), times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testPreHandle() {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.preHandle(execution);
        // No exception should be thrown
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.setReturnObject(new Object());
        baseRuntimeFlowableDelegate.setThrown(null);
        baseRuntimeFlowableDelegate.setDone(true);

        Object result = baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);

        assertNotNull(result);
    }

}