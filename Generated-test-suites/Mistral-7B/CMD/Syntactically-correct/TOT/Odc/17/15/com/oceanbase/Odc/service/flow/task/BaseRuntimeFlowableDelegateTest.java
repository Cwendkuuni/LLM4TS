package com.oceanbase.odc.service.flow.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.execution = mock(DelegateExecution.class);
        this.delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException("initCallable not implemented");
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // no-op
            }

            @Override
            public String call() throws Exception {
                return "result";
            }
        };
        this.delegate.init(eventPublisher);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenReturn(true);
        when(eventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenReturn(true);
    }

    @Test
    public void testNormalExecution() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.run(execution);
        assertEquals("result", delegate.get());
    }

    @Test
    public void testInterruptedExecution() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.initTargetTaskInstanceId(execution);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenThrow(new RuntimeException());
        delegate.run(execution);
        assertNull(delegate.get());
        assertThrows(ServiceTaskError.class, () -> delegate.get(10, TimeUnit.SECONDS));
    }

    @Test
    public void testTimeoutExecution() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.initTargetTaskInstanceId(execution);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenCallRealMethod();
        delegate.run(execution);
        delegate.updateFlowInstanceStatus(FlowStatus.FAILED);
        assertNull(delegate.get());
        assertThrows(TimeoutException.class, () -> delegate.get(10, TimeUnit.SECONDS));
    }

    @Test
    public void testExceptionalExecution() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.initTargetTaskInstanceId(execution);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenThrow(new RuntimeException("error"));
        delegate.run(execution);
        assertThrows(ServiceTaskError.class, () -> delegate.get());
    }

    @Test
    public void testSuccessUpdateFlowInstanceStatus() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.initTargetTaskInstanceId(execution);
        delegate.run(execution);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        assertEquals(1, flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED));
    }

    @Test
    public void testFailedUpdateFlowInstanceStatus() {
        delegate.bindToFlowTaskInstance(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null));
        delegate.initTargetTaskInstanceId(execution);
        delegate.run(execution);
        delegate.updateFlowInstanceStatus(FlowStatus.FAILED);
        assertEquals(1, flowInstanceRepository.updateStatusById(1L, FlowStatus.FAILED));
    }
}