package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = mock(RetryExecutor.class);

        this.delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // empty implementation
            }

            @Override
            public void bindToFlowTaskInstance(FlowTaskInstance taskInstance) {
                // empty implementation
            }

            @Override
            protected void updateFlowInstanceStatus(FlowStatus flowStatus) {
                // empty implementation
            }
        };

        this.delegate.flowableAdaptor = this.flowableAdaptor;
        this.delegate.eventPublisher = this.eventPublisher;
        this.delegate.flowInstanceRepository = this.flowInstanceRepository;
        this.delegate.retryExecutor = this.retryExecutor;
    }

    @Test
    public void testRun_normalCase() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = mock(Callable.class);
        when(callable.call()).thenReturn(1);

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(this.flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        this.delegate.init(this.eventPublisher);
        this.delegate.initTargetTaskInstanceId(execution);
        this.delegate.run(execution);

        verify(this.eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        assertEquals("processDefinitionId", serviceTaskStartedEventCaptor.getValue().getDelegateExecution().getProcessDefinitionId());

        verify(callable).call();
        verify(this.flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testRun_thrownException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("Error message"));

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);

        this.delegate.init(this.eventPublisher);
        this.delegate.initTargetTaskInstanceId(execution);

        assertThrows(ServiceTaskError.class, () -> this.delegate.run(execution));
        verify(this.eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, never()).call();
        verify(this.flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testGet_normalCase() throws InterruptedException, ExecutionException {
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = mock(Callable.class);
        when(callable.call()).thenReturn(1);

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(this.flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        this.delegate.init(this.eventPublisher);
        this.delegate.initTargetTaskInstanceId(execution);
        this.delegate.run(execution);

        assertEquals(1, this.delegate.get());
        verify(this.eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable).call();
        verify(this.flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = mock(Callable.class);
        when(callable.call()).thenReturn(1);

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);

        this.delegate.init(this.eventPublisher);
        this.delegate.initTargetTaskInstanceId(execution);

        assertThrows(TimeoutException.class, () -> this.delegate.get(10, TimeUnit.MILLISECONDS));
        verify(this.eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, never()).call();
        verify(this.flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
    }

}