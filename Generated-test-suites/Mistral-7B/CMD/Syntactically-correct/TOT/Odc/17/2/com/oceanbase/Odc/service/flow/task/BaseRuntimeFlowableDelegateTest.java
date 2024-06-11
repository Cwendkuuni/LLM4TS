package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.junit.jupiter.MockitoJUnitRunner;

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
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MockitoJUnitRunner
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private RetryExecutor retryExecutor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> target;

    @Before
    public void setup() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getFlowInstanceById(anyLong())).thenReturn(mock(Execution.class));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(retryExecutor.run(any(Callable.class), any(Predicate.class))).thenAnswer(invocation -> invocation.getArguments()[0].call());
    }

    @Test
    public void testInitializeTask() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        target.init(eventPublisher);
        target.initTargetTaskInstanceId(execution);

        assertEquals("activityId", target.getActivityId());
        assertEquals(1L, target.getFlowInstanceId());
        assertEquals(TaskType.TASK_TYPE_1, target.getTaskType());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        target.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(eq(target.getFlowInstanceId()), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(target.getTargetTaskInstanceId());
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        target.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(eq(target));
    }

    @Test
    public void testRunWithSuccess() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        when(target.initCallable(execution)).thenReturn(callable);

        target.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyZeroInteractions(target.getLatch());
        verify(target).preHandle(execution);
        verifyZeroInteractions(target.getEventPublisher());
        verifyZeroInteractions(target.getFlowableAdaptor());
        verifyZeroInteractions(target.getFlowInstanceRepository());
        verifyZeroInteractions(target.getRetryExecutor());
        verifyZeroInteractions(target.getActiveTaskStatisticsListener());
        verifyZeroInteractions(target.getTaskInstanceCreatedlistener());

        assertEquals("result", target.get());
    }

    @Test
    public void testRunWithException() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("test exception"));

        when(target.initCallable(execution)).thenReturn(callable);

        try {
            target.run(execution);
            fail();
        } catch (ExecutionException e) {
            verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
            verifyZeroInteractions(target.getLatch());
            verify(target).preHandle(execution);
            verifyZeroInteractions(target.getEventPublisher());
            verifyZeroInteractions(target.getFlowableAdaptor());
            verifyZeroInteractions(target.getFlowInstanceRepository());
            verifyZeroInteractions(target.getRetryExecutor());
            verifyZeroInteractions(target.getActiveTaskStatisticsListener());
            verifyZeroInteractions(target.getTaskInstanceCreatedlistener());
            assertEquals("test exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testGet() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        when(target.initCallable(execution)).thenReturn(callable);

        target.get();

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyZeroInteractions(target.getLatch());
        verify(target).preHandle(execution);
        verifyZeroInteractions(target.getEventPublisher());
        verifyZeroInteractions(target.getFlowableAdaptor());
        verifyZeroInteractions(target.getFlowInstanceRepository());
        verifyZeroInteractions(target.getRetryExecutor());
        verifyZeroInteractions(target.getActiveTaskStatisticsListener());
        verifyZeroInteractions(target.getTaskInstanceCreatedlistener());

        verify(target.getLatch()).await();
        verifyZeroInteractions(target);
        verifyZeroInteractions(target.getThrown());
        assertEquals("result", target.getReturnObject());
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        when(target.initCallable(execution)).thenReturn(callable);

        target.get(1, TimeUnit.SECONDS);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyZeroInteractions(target.getLatch());
        verify(target).preHandle(execution);
        verifyZeroInteractions(target.getEventPublisher());
        verifyZeroInteractions(target.getFlowableAdaptor());
        verifyZeroInteractions(target.getFlowInstanceRepository());
        verifyZeroInteractions(target.getRetryExecutor());
        verifyZeroInteractions(target.getActiveTaskStatisticsListener());
        verifyZeroInteractions(target.getTaskInstanceCreatedlistener());

        verify(target.getLatch()).await(eq(1), eq(TimeUnit.SECONDS));
        verifyZeroInteractions(target);
        verifyZeroInteractions(target.getThrown());
        verifyZeroInteractions(target.getReturnObject());
    }

    @Test
    public void testGetWithTimeoutException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("test exception"));

        when(target.initCallable(execution)).thenReturn(callable);

        try {
            target.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
            verifyZeroInteractions(target.getLatch());
            verify(target).preHandle(execution);
            verifyZeroInteractions(target.getEventPublisher());
            verifyZeroInteractions(target.getFlowableAdaptor());
            verifyZeroInteractions(target.getFlowInstanceRepository());
            verifyZeroInteractions(target.getRetryExecutor());
            verifyZeroInteractions(target.getActiveTaskStatisticsListener());
            verifyZeroInteractions(target.getTaskInstanceCreatedlistener());
            assertEquals("test exception", target.getThrown().getCause().getMessage());
        }
    }

    @Test
    public void testIsDone() {
        assertTrue(target.isDone());
    }

    @Test
    public void testIsDoneFalse() {
        target.setDone(false);
        assertFalse(target.isDone());
    }
}