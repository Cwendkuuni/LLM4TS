package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * BaseRuntimeFlowableDelegate Test
 *
 * @author yh263208
 * @date 2023-03-21
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            {
                this.flowableAdaptor = flowableAdaptor;
                this.eventPublisher = eventPublisher;
                this.flowInstanceRepository = flowInstanceRepository;
                this.retryExecutor = retryExecutor;
                this.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
                this.activeTaskStatisticsListener = activeTaskStatisticsListener;
            }
        };
        delegateExecution = mock(DelegateExecution.class);
    }

    @Test
    public void testInit() {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));

        // When
        baseRuntimeFlowableDelegate.init("processDefinitionId");

        // Then
        ArgumentCaptor<Long> flowInstanceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowInstanceRepository).updateStatusById(flowInstanceIdArgumentCaptor.capture(), eq(FlowStatus.RUNNING));
        assertEquals(1L, flowInstanceIdArgumentCaptor.getValue());
    }

    @Test
    public void testRun() {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(taskInstanceCreatedlistener.getId()).thenReturn(1L);
        when(activeTaskStatisticsListener.getId()).thenReturn(1L);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        // When
        baseRuntimeFlowableDelegate.run(delegateExecution);

        // Then
        verify(eventPublisher).publishEvent(eq(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Mockito.any())));
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowInstanceRepository).updateStatusById(eq(1L), eq(FlowStatus.RUNNING));
        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(baseRuntimeFlowableDelegate).execute(delegateExecution);
        verifyNoMoreInteractions(flowableAdaptor);
        verifyNoMoreInteractions(flowInstanceRepository);
        verifyNoMoreInteractions(eventPublisher);
        verify(baseRuntimeFlowableDelegate).initCallable(delegateExecution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(taskInstanceCreatedlistener.getId()).thenReturn(1L);
        when(activeTaskStatisticsListener.getId()).thenReturn(1L);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "testResult");

        // When
        baseRuntimeFlowableDelegate.init("processDefinitionId");
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        baseRuntimeFlowableDelegate.run(delegateExecution);
        T result = baseRuntimeFlowableDelegate.get();

        // Then
        assertNotNull(result);
        assertEquals("testResult", result);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testGetException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(taskInstanceCreatedlistener.getId()).thenReturn(1L);
        when(activeTaskStatisticsListener.getId()).thenReturn(1L);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenThrow(new RuntimeException("testException"));

        // When
        baseRuntimeFlowableDelegate.init("processDefinitionId");
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);
        T result = baseRuntimeFlowableDelegate.get();

        // Then
        assertNull(result);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
        verifyThrowing(baseRuntimeFlowableDelegate::get).withCause(instanceOf(RuntimeException.class));
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(taskInstanceCreatedlistener.getId()).thenReturn(1L);
        when(activeTaskStatisticsListener.getId()).thenReturn(1L);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenThrow(new InterruptedException());

        // When
        baseRuntimeFlowableDelegate.init("processDefinitionId");
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);

        // Then
        T result = baseRuntimeFlowableDelegate.get(5, TimeUnit.MILLISECONDS);

        // Then
        assertNull(result);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
        verifyThrowing(baseRuntimeFlowableDelegate::get, withTimeout(5, TimeUnit.MILLISECONDS));
    }

}