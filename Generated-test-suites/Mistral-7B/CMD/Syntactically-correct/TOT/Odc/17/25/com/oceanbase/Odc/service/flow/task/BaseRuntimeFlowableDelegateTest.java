package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    private static final String FLOW_DEFINITION_ID = "flowDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_2;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final EventPublisher EVENT_PUBLISHER = mock(EventPublisher.class);
    private static final FlowInstanceRepository FLOW_INSTANCE_REPOSITORY = mock(FlowInstanceRepository.class);
    private static final FlowableAdaptor FLOWABLE_ADAPTOR = mock(FlowableAdaptor.class);
    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private FlowTaskInstance flowTaskInstance;
    private CountDownLatch latch;

    @Before
    public void setUp() {
        delegate = spy(new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected void initTargetTaskInstanceId(DelegateExecution execution) {
                super.initTargetTaskInstanceId(execution);
            }

            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return null;
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
            }

            @Override
            public void execute(DelegateExecution execution) {
            }
        });

        flowTaskInstance = mock(FlowTaskInstance.class);
        latch = new CountDownLatch(1);

        when(FLOWABLE_ADAPTOR.getFlowInstanceIdByProcessDefinitionId(FLOW_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(FLOWABLE_ADAPTOR.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(Optional.of(flowTaskInstance));
        when(FLOW_INSTANCE_REPOSITORY.updateStatusById(FLOW_INSTANCE_ID, any(FlowStatus.class))).thenReturn(true);

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(FLOW_DEFINITION_ID)).thenAnswer(invocationOnMock -> FLOW_INSTANCE_ID);

        delegate.init(EVENT_PUBLISHER);
        delegate.flowableAdaptor = FLOWABLE_ADAPTOR;
        delegate.flowInstanceRepository = FLOW_INSTANCE_REPOSITORY;
        delegate.eventPublisher = EVENT_PUBLISHER;
    }

    @Test
    public void test_initTargetTaskInstanceId_setsFlowInstanceId_whenSuccess() {
        // Given

        // When
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        // Then
        verify(delegate).flowInstanceId = FLOW_INSTANCE_ID;
    }

    @Test
    public void test_initTargetTaskInstanceId_throwsException_whenFlowInstanceNotFound() {
        // Given
        when(FLOWABLE_ADAPTOR.getFlowInstanceIdByProcessDefinitionId(FLOW_DEFINITION_ID)).thenReturn(null);

        // When and Then
        assertThrows(ServiceTaskError.class, delegate::initTargetTaskInstanceId);
        verify(delegate, never()).flowInstanceId = anyInt();
    }

    @Test
    public void test_initTargetTaskInstanceId_setsTaskType_whenSuccess() {
        // Given

        // When
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        // Then
        verify(delegate).taskType = TASK_TYPE;
    }

    @Test
    public void test_initTargetTaskInstanceId_throwsException_whenTaskTypeNotFound() {
        // Given
        when(flowTaskInstance.getTaskType()).thenReturn(null);

        // When and Then
        assertThrows(ServiceTaskError.class, delegate::initTargetTaskInstanceId);
        verify(delegate, never()).taskType = any(TaskType.class);
    }

    @Test
    public void test_initTargetTaskInstanceId_setsStrategyConfig_whenSuccess() {
        // Given

        // When
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        // Then
        verify(delegate).strategyConfig = STRATEGORY_CONFIG;
    }

    @Test
    public void test_initTargetTaskInstanceId_throwsException_whenStrategyConfigNotFound() {
        // Given
        when(flowTaskInstance.getStrategyConfig()).thenReturn(null);

        // When and Then
        assertThrows(ServiceTaskError.class, delegate::initTargetTaskInstanceId);
        verify(delegate, never()).strategyConfig = any(ExecutionStrategyConfig.class);
    }

    @Test
    public void test_run_publishesServiceTaskStartedEvent_whenSuccess() {
        // Given
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);

        // When
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(Mockito.mock(Callable.class));
        delegate.activityId = ACTIVITY_ID;
        delegate.execute(Mockito.mock(DelegateExecution.class));

        // Then
        verify(delegate).eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void test_run_doesNotPublishServiceTaskStartedEvent_whenExceptionThrown() {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenThrow(new RuntimeException("Test Exception"));

        // When
        delegate.activityId = ACTIVITY_ID;
        delegate.execute(Mockito.mock(DelegateExecution.class));

        // Then
        verify(delegate, never()).eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void test_run_updatesFlowInstanceStatus_whenSuccess() {
        // Given

        // When
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(Mockito.mock(Callable.class));
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(FLOW_INSTANCE_REPOSITORY).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @Test
    public void test_run_doesNotUpdateFlowInstanceStatus_whenExceptionThrown() {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenThrow(new RuntimeException("Test Exception"));

        // When
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(FLOW_INSTANCE_REPOSITORY, never()).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @Test
    public void test_run_bindsToTaskInstance_whenSuccess() {
        // Given

        // When
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        // Then
        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void test_get_returnsCorrectValue_whenSuccess() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(Mockito.mock(Callable.class));
        Callable<Integer> callable = delegate.initCallable(Mockito.mock(DelegateExecution.class));

        // When
        delegate.activityId = ACTIVITY_ID;
        T result = delegate.get();

        // Then
        assertEquals(callable.call(), result);
    }

    @Test
    public void test_get_throwsException_whenTimeout() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(Mockito.mock(Callable.class));

        // When and Then
        assertThrows(TimeoutException.class, delegate::get, 1000L);
    }

    @Test
    public void test_get_throwsException_whenExceptionThrown() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenThrow(new RuntimeException("Test Exception"));

        // When and Then
        assertThrows(ExecutionException.class, delegate::get, 1000L);
        verify(delegate.thrown).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void test_get_returnsNull_whenDoneAndNoValue() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(null);

        // When
        delegate.activityId = ACTIVITY_ID;
        T result = delegate.get();

        // Then
        assertNull(result);
        assertTrue(delegate.done);
    }

    @Test
    public void test_isDone_returnsTrue_whenDone() throws Exception {
        // Given

        // When
        delegate.activityId = ACTIVITY_ID;
        delegate.done = true;

        // Then
        assertTrue(delegate.isDone());
    }

    @Test
    public void test_isDone_returnsFalse_whenNotDone() throws Exception {
        // Given

        // When
        delegate.activityId = ACTIVITY_ID;
        delegate.done = false;

        // Then
        assertFalse(delegate.isDone());
    }

    @Test
    public void test_getFuture_returnsCorrectValue_whenSuccess() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(Mockito.mock(Callable.class));
        Callable<Integer> callable = delegate.initCallable(Mockito.mock(DelegateExecution.class));

        // When
        delegate.activityId = ACTIVITY_ID;
        Future<Integer> future = delegate.getFuture();

        // Then
        assertSame(callable, future.getCallable());
    }

    @Test
    public void test_getFuture_returnsNull_whenDoneAndNoValue() throws Exception {
        // Given
        when(delegate.initCallable(any(DelegateExecution.class))).thenReturn(null);

        // When
        delegate.activityId = ACTIVITY_ID;
        Future<Integer> future = delegate.getFuture();

        // Then
        assertNull(future.get());
        assertTrue(delegate.done);
    }

    private void assertThrows(Class<? extends Exception> expectedExceptionClass, Runnable runnable) throws Exception {
        assertThrows(expectedExceptionClass, runnable);
    }

    private void assertThrows(Class<? extends Exception> expectedExceptionClass, Runnable runnable, long timeout) throws Exception {
        try {
            runnable.run();
            fail("Exception was not thrown");
        } catch (Exception e) {
            if (expectedExceptionClass.isInstance(e)) {
                return;
            }
            throw e;
        }
        fail("Timed out waiting for exception to be thrown");
    }
}