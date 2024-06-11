package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

class BaseRuntimeFlowableDelegateTest {

    @org.junit.Before
    public void setUp() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = Mockito.spy(RetryExecutor.class);

        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
    }

    @org.junit.Test
    public void testRun_success() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Optional.of(new FlowTaskInstance()));

        PreConditions.stubValidateExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId");

        when(callable.call()).thenReturn("result");

        // When
        delegate.run(execution);

        // Then
        assertFalse(execution.isTerminated());
        assertEquals("result", delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(Mockito.any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(Mockito.any(ActiveTaskStatisticsListener.class));
    }

    @org.junit.Test
    public void testRun_initializationFailed() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenThrow(new RuntimeException());

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        assertThrows(ServiceTaskError.class, () -> {
            delegate.run(execution);
        });

        // Then
        assertTrue(execution.isTerminated());
        assertSame(execution, delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).removeEventListener(Mockito.any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, never()).removeEventListener(Mockito.any(ActiveTaskStatisticsListener.class));
    }

    @org.junit.Test
    public void testRun_executionFailed() throws Exception {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Optional.of(new FlowTaskInstance()));

        PreConditions.stubValidateExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId");

        when(callable.call()).thenThrow(new RuntimeException());

        // When
        delegate.run(execution);

        // Then
        assertTrue(execution.isTerminated());
        assertNull(delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(Mockito.any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(Mockito.any(ActiveTaskStatisticsListener.class));
    }

    @org.junit.Test
    public void testGet_success() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Optional.of(new FlowTaskInstance()));

        PreConditions.stubValidateExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId");

        when(callable.call()).thenReturn("result");

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.run(execution);

        // Then
        assertEquals("result", delegate.get());
    }

    @org.junit.Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Optional.of(new FlowTaskInstance()));

        PreConditions.stubValidateExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId");

        when(callable.call()).thenReturn(null);

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.run(execution);

        // Then
        assertThrows(TimeoutException.class, () -> {
            delegate.get(1, TimeUnit.SECONDS);
        });
    }

    @org.junit.Test
    public void testGet_interrupted() throws InterruptedException, ExecutionException {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L))).thenReturn(Optional.of(new FlowTaskInstance()));

        PreConditions.stubValidateExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId");

        when(callable.call()).thenReturn(null);

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.run(execution);

        // Then
        boolean interrupted = Thread.interrupted();
        assertFalse(execution.isTerminated());
        assertNull(delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(Mockito.any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(Mockito.any(ActiveTaskStatisticsListener.class));
        assertTrue(interrupted);
    }

    @org.junit.Test
    public void testInitCallable() {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();

        // Then
        Callable<Object> callable = delegate.initCallable(execution);
        assertNull(callable);
    }

    static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {
    }
}