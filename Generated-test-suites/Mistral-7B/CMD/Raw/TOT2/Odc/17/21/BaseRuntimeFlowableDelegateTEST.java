package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}.
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<Object> callable;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(createFlowTaskInstanceMock());
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenReturn(null);
        when(retryExecutor.run(any(Callable.class))).thenReturn(Optional.of(1L));

        delegate.init(eventPublisher);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun() throws Exception {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(createFlowTaskInstanceMock());
        when(callable.call()).thenReturn("test result");
        when(eventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenReturn(null);

        delegate.initCallable(execution);
        delegate.bindToFlowTaskInstance(createFlowTaskInstanceMock());
        delegate.run(execution);

        verify(execution, times(1)).setVariable("returnObject", "test result");
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
        verifyNoMoreInteractions(execution);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunException() throws Exception {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Initialization task failed"));

        delegate.init(eventPublisher);
        delegate.run(execution);

        fail("Should throw ServiceTaskError");
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(createFlowTaskInstanceMock());
        when(callable.call()).thenReturn("test result");
        when(eventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenReturn(null);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(createFlowTaskInstanceMock());
        delegate.initCallable(execution);

        assertEquals("test result", delegate.get());
    }

    private FlowTaskInstance createFlowTaskInstanceMock() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_MANUAL);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        return flowTaskInstance;
    }
}