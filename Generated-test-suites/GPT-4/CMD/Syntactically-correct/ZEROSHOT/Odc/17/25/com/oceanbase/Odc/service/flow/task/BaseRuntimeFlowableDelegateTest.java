package com.oceanbase.odc.service.flow.task;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowTaskInstance flowTaskInstance;
    @Mock
    private DelegateExecution delegateExecution;

    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Before
    public void setup() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate() {
            @Override
            protected Callable initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
    }

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        assertNotNull("Event publisher should not be null", baseRuntimeFlowableDelegate.getEventPublisher());
    }

    @Test
    public void testRun() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
    }

    @Test
    public void testIsDone() {
        assertFalse("Should not be done before execution", baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        when(flowTaskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        assertNotNull("Flow task instance should be bind", baseRuntimeFlowableDelegate.getFlowTaskInstance());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        assertDoesNotThrow(() -> baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));
    }

    @Test(expected = ExecutionException.class)
    public void testExecuteThrowsException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        doThrow(new RuntimeException()).when(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutThrowsTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        doThrow(new InterruptedException()).when(delegateExecution).getProcessDefinitionId();
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }
}