package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @ org.junit.Before
    public void setup() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException("initCallable should be implemented in subclass");
            }
        };

        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setRetryExecutor(retryExecutor);
        baseRuntimeFlowableDelegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        baseRuntimeFlowableDelegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
        baseRuntimeFlowableDelegate.init(eventPublisher);
    }

    @ org.junit.Test
    public void testInit() {
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @ org.junit.Test
    public void testRunWithSuccess() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(callable);

        baseRuntimeFlowableDelegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(execution);
        verify(baseRuntimeFlowableDelegate, times(1)).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
        assertEquals("result", baseRuntimeFlowableDelegate.get());
    }

    @ org.junit.Test
    public void testRunWithException() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("Error"));

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(callable);

        try {
            baseRuntimeFlowableDelegate.run(execution);
            fail("Expected ServiceTaskError to be thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Error", e.getCause().getMessage());
        }

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, never()).call();
        verify(baseRuntimeFlowableDelegate, never()).preHandle(execution);
        verify(baseRuntimeFlowableDelegate, never()).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
    }

    @ org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(callable);

        baseRuntimeFlowableDelegate.run(execution);

        assertEquals("result", baseRuntimeFlowableDelegate.get());
    }

    @ org.junit.Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(callable);

        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
            fail("Expected TimeoutException to be thrown");
        } catch (TimeoutException e) {
            // expected
        }

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(execution);
        verify(baseRuntimeFlowableDelegate, times(1)).updateFlowInstanceStatus(eq(FlowStatus.FAILED));
        verifyNoInteractions(baseRuntimeFlowableDelegate.get());
    }

    @ org.junit.Test
    public void testInitTargetTaskInstanceId() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));

        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(TaskType.TASK, baseRuntimeFlowableDelegate.getTaskType());
        assertNull(baseRuntimeFlowableDelegate.getStrategyConfig());
        assertEquals("processDefinitionId", baseRuntimeFlowableDelegate.getProcessDefinitionId());

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, never()).getTaskInstanceByActivityId(anyString(), anyLong());
        verifyNoMoreInteractions(flowableAdaptor);
    }

    @ org.junit.Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(eq(baseRuntimeFlowableDelegate));
        verify(baseRuntimeFlowableDelegate, times(1)).updateFlowInstanceStatus(eq(FlowStatus.ACTIVE));
    }

}