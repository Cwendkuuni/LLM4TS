package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Execution strategyCallable;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(retryExecutor.run(any(Callable.class), any(Function.class))).thenReturn(true);
    }

    @Test
    public void testInitCallable() {
        Callable<Object> callable = delegate.initCallable(execution);
        assertNotNull(callable);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId())).thenThrow(new Exception());
        delegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(log, times(1)).warn("Initialization task failed", any(Exception.class));
        verify(execution, times(1)).setVariable("thrown", any(Exception.class));
        verify(execution, times(1)).setVariable("done", true);
        verify(latch, times(1)).countDown();
        throw new ServiceTaskError(any(Exception.class));
    }

    @Test
    public void testRunSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId())).thenReturn(1L);
        when(strategyCallable.call()).thenReturn("result");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(retryExecutor.run(any(Callable.class), any(Function.class))).thenReturn(true);

        assertEquals("result", delegate.get());
        verify(log, times(1)).info("Monitor task instance creation and bind tasks, taskInstanceId={}, activityId={}",
                anyLong(), anyString());
        verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testRunTimeout() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId())).thenReturn(1L);
        when(strategyCallable.call()).thenReturn("result");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(retryExecutor.run(any(Callable.class), any(Function.class))).thenReturn(false);

        assertNull(delegate.get(1, TimeUnit.SECONDS));
        verify(log, times(1)).warn("Task execution time out");
    }
}