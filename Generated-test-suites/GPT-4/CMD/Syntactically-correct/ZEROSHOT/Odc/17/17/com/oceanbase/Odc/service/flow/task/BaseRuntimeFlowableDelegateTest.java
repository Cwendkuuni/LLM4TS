package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private FlowTaskInstance flowTaskInstance;
    @Mock
    private ExecutionStrategyConfig strategyConfig;
    @Mock
    private Future<String> future;
    @Mock
    private TaskType taskType;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable Result";
            }
        };
        baseRuntimeFlowableDelegate.init(eventPublisher);
    }

    @Test
    public void testGetTaskType() {
        when(flowableAdaptor.getTaskInstanceByActivityId("1", 1L)).thenReturn(Optional.of(flowTaskInstance));
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        assertEquals(taskType, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertEquals("Test Callable Result", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        when(future.get()).thenThrow(new InterruptedException());
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertEquals("Test Callable Result", baseRuntimeFlowableDelegate.get(1L, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(future.get(1L, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
        baseRuntimeFlowableDelegate.get(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FAILED);
        verify(flowTaskInstance).setStatus(FlowStatus.FAILED);
    }
}
