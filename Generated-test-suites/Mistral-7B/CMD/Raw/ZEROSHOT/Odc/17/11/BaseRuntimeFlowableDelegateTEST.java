package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Tests for BaseRuntimeFlowableDelegate.
 */
public class BaseRuntimeFlowableDelegateTest {

    private FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
    private RetryExecutor retryExecutor = Mockito.spy(RetryExecutor.class);
    private BaseRuntimeFlowableDelegate<String> testDelegate = new BaseRuntimeFlowableDelegateImpl<>();

    @org.junit.Before
    public void setup() {
        testDelegate.setFlowInstanceRepository(flowInstanceRepository);
        testDelegate.setEventPublisher(eventPublisher);
        testDelegate.setFlowableAdaptor(flowableAdaptor);
        testDelegate.setRetryExecutor(retryExecutor);
    }

    @org.junit.Test
    public void testInitializeTaskInstanceId() {
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null, null)));

        testDelegate.initTargetTaskInstanceId(null);

        assertEquals(1L, testDelegate.getFlowInstanceId());
        assertEquals("activityId", testDelegate.getActivityId());
        assertEquals(TaskType.TASK_TYPE_1, testDelegate.getTaskType());
    }

    @org.junit.Test
    public void testInitializeTaskInstanceIdWithRetry() {
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(1L));

        String activityId = "activityId";
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, activityId, TaskType.TASK_TYPE_1, null, null)));

        testDelegate.initTargetTaskInstanceId(null);

        verify(flowableAdaptor, times(2)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptor).getTaskInstanceByActivityId(activityId, anyLong());
    }

    @org.junit.Test(expected = ExecutionException.class)
    public void testInitializeTaskInstanceIdWithException() {
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenThrow(new RuntimeException("Failed to get flowInstanceId"));

        testDelegate.initTargetTaskInstanceId(null);

        fail("InitializeTaskInstanceId should have thrown Exception");
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatusWithNullReturn() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(null);

        testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
        verify(retryExecutor, times(1)).run(any(Callable.class), any(Optional.class::getClass));
    }

    @org.junit.Test
    public void testInitializeCallable() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        Callable<String> callable = testDelegate.initCallable(null);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        assertNotNull(callable);
    }

    @org.junit.Test
    public void testGet() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<String> callable = () -> "result";
        testDelegate.returnObject = "result";
        testDelegate.done = true;

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null, null)));

        when(retryExecutor.run(any(Callable.class), any(Optional.class.getClass)))
                .thenReturn(true);

        testDelegate.preHandle(execution);

        Future<String> future = testDelegate.call();

        testDelegate.get();

        verify(testDelegate, times(1)).preHandle(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(retryExecutor, times(1)).run(any(Callable.class), any(Optional.class.getClass));
        verify(testDelegate, times(1)).execute(execution);
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));

        assertEquals("result", future.get());
        assertEquals("result", testDelegate.getReturnObject());
        assertTrue(testDelegate.isDone());
    }

    @org.junit.Test
    public void testGetWithTimeout() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<String> callable = () -> {
            Thread.sleep(1001);
            return "result";
        };
        testDelegate.returnObject = "result";
        testDelegate.done = false;

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null, null)));

        when(retryExecutor.run(any(Callable.class), any(Optional.class.getClass)))
                .thenReturn(true);

        testDelegate.preHandle(execution);

        Future<String> future = testDelegate.call();

        assertFalse(testDelegate.isDone());

        try {
            testDelegate.get(1, TimeUnit.SECONDS);
            fail("Get with timeout should have thrown TimeoutException");
        } catch (TimeoutException e) {
            // expected
        }

        verify(testDelegate, times(1)).preHandle(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(retryExecutor, times(1)).run(any(Callable.class), any(Optional.class.getClass));
        verify(testDelegate, times(1)).execute(execution);
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));

        verify(testDelegate, never()).getReturnObject();
    }

    @org.junit.Test
    public void testGetError() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<String> callable = () -> {
            throw new RuntimeException("Test exception");
        };
        testDelegate.thrown = new RuntimeException("Test exception");
        testDelegate.done = false;

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, null, null)));

        when(retryExecutor.run(any(Callable.class), any(Optional.class.getClass)))
                .thenReturn(true);

        testDelegate.preHandle(execution);

        Future<String> future = testDelegate.call();

        assertFalse(testDelegate.isDone());

        try {
            testDelegate.get();
            fail("Get with no timeout should have thrown ExecutionException");
        } catch (ExecutionException e) {
            assertEquals("Test exception", e.getCause().getMessage());
        }

        verify(testDelegate, times(1)).preHandle(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(retryExecutor, times(1)).run(any(Callable.class), any(Optional.class.getClass));
        verify(testDelegate, times(1)).execute(execution);
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));

        verify(testDelegate, never()).getReturnObject();
        verify(testDelegate, never()).returnObject;
    }
}