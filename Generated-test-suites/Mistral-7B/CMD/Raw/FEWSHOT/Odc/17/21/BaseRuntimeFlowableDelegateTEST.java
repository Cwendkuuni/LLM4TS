package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test case for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        execution = mock(DelegateExecution.class);

        delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected void preHandle(DelegateExecution execution) {
            }

            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> 42;
            }
        };
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
    }

    @Test
    public void shouldInitializeWhenRunSuccessfully() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        // When
        delegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(any(FlowTaskInstance.class));
        verify(activeTaskStatisticsListener).onActivate(any(BaseRuntimeFlowableDelegate.class));
        assertFalse(delegate.isDone());
    }

    @Test
    public void shouldThrowExceptionWhenInitializationFails() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenThrow(new RuntimeException());

        // When/Then
        assertThrows(ServiceTaskError.class, () -> delegate.run(execution));
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void shouldReturnFutureValue() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        // When
        Future<Integer> future = delegate.get();

        // Then
        assertTrue(future.isDone());
        assertEquals(42, future.get());
    }

    @Test
    public void shouldTimeoutWhenFutureValueIsNotAvailable() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void shouldBindToFlowTaskInstance() throws Exception {
        // Given
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(taskInstance));

        // When
        delegate.bindToFlowTaskInstance(taskInstance);

        // Then
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void shouldUpdateFlowInstanceStatus() throws Exception {
        // Given
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        // When
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
    }

    @Test
    public void shouldRetryWhenUpdateFlowInstanceStatusFails() throws Exception {
        // Given
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenThrow(new RuntimeException());

        // When
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
        verify(retryExecutor, times(2)).run(any(), any());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateFlowInstanceStatusFailsAndNoRetry() throws Exception {
        // Given
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenThrow(new RuntimeException());
        when(retryExecutor.build()).thenReturn(RetryExecutor.builder().retryIntervalMillis(0).build());

        // When/Then
        assertThrows(RuntimeException.class, () -> delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));
    }

    @Test
    public void shouldNotUpdateFlowInstanceStatusWhenNullFlowInstanceId() throws Exception {
        // Given
        when(retryExecutor.build()).thenReturn(RetryExecutor.builder().build());

        // When
        delegate.updateFlowInstanceStatus(null);

        // Then
        verify(retryExecutor, never()).run(any(), any());
    }
}