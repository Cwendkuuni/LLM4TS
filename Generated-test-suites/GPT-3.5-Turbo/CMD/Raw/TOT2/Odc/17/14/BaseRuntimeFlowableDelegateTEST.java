import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;

import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        delegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        });
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
    }

    @Test
    public void testRun_Success() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate.run(execution);
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        assertTrue(delegate.isDone());
        assertEquals("Test", delegate.get());
    }

    @Test
    public void testRun_ExceptionHandling() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        doThrow(new ServiceTaskError("Error")).when(delegate).initCallable(execution);
        try {
            delegate.run(execution);
        } catch (Exception e) {
            assertTrue(e instanceof ServiceTaskError);
            assertEquals("Error", e.getMessage());
        }
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet_TimeoutException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            delegate.notifyAll();
        }).start();
        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            assertTrue(e instanceof TimeoutException);
        }
    }

    @Test
    public void testInitTargetTaskInstanceId_Success() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(flowTaskInstance));
        delegate.run(execution);
        assertEquals("activityId", delegate.getActivityId());
        assertEquals(Long.valueOf(1), delegate.getFlowInstanceId());
    }

    @Test
    public void testInitCallable_Success() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = delegate.initCallable(execution);
        assertNotNull(callable);
        assertEquals("Test", callable.call());
    }
}