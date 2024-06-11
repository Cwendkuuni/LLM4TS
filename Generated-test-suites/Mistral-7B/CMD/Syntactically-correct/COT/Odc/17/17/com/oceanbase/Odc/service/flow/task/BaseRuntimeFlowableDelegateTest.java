package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> testee;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.flowableAdaptorMock = mock(FlowableAdaptor.class);
        this.eventPublisherMock = mock(EventPublisher.class);
        this.flowInstanceRepositoryMock = mock(FlowInstanceRepository.class);

        RetryExecutor retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        this.testee = new BaseRuntimeFlowableDelegate<String>() {
            {
                this.retryExecutor = retryExecutor;
                this.latch = new CountDownLatch(1);
                this.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(this);
                this.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(this);
                this.flowableAdaptor = this.flowableAdaptorMock;
                this.eventPublisher = this.eventPublisherMock;
                this.flowInstanceRepository = this.flowInstanceRepositoryMock;
            }

            @Override
            protected void init(EventPublisher eventPublisher) {
                eventPublisherMock.addMockEventListener(this.activeTaskStatisticsListener);
                eventPublisherMock.addMockEventListener(this.taskInstanceCreatedlistener);
            }
        };
    }

    @Test
    public void testRun() throws Exception {
        // Given
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);

        String activityId = "activityId";
        FlowTaskInstance flowTaskInstanceMock = mock(FlowTaskInstance.class);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Optional.of(flowTaskInstanceMock));
        when(flowTaskInstanceMock.getId()).thenReturn(1L);
        when(flowTaskInstanceMock.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstanceMock.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(activityId);

        Callable<String> callableMock = mock(Callable.class);

        when(testee.initCallable(executionMock)).thenReturn(callableMock);

        // When
        testee.run(executionMock);

        // Then
        verify(flowableAdaptorMock, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptorMock, times(1)).getTaskInstanceByActivityId(activityId, 1L);
        verify(eventPublisherMock, times(1)).publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
        verify(testee.taskInstanceCreatedlistener, times(1)).onEvent(Mockito.any(FlowTaskInstanceCreatedEvent.class));
        verify(testee.activeTaskStatisticsListener, times(1)).onEvent(Mockito.any(ActiveTaskStatisticsEvent.class));
        verifyNoMoreInteractions(flowableAdaptorMock);
        verifyNoMoreInteractions(eventPublisherMock);
        verifyNoMoreInteractions(flowInstanceRepositoryMock);
        verify(callableMock).call();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);

        String activityId = "activityId";
        FlowTaskInstance flowTaskInstanceMock = mock(FlowTaskInstance.class);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Optional.of(flowTaskInstanceMock));

        Callable<String> callableMock = mock(Callable.class);
        when(testee.initCallable(Mockito.mock(DelegateExecution.class))).thenReturn(callableMock);

        ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisherMock, times(1)).publishEvent(eventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = eventCaptor.getValue();

        // When
        Future<String> future = testee.get();

        // Then
        verify(flowableAdaptorMock, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptorMock, times(1)).getTaskInstanceByActivityId(activityId, 1L);
        verify(eventPublisherMock, times(1)).publishEvent(eventCaptor.capture());
        verify(callableMock).call();
        assertEquals("serviceTaskStartedEvent", serviceTaskStartedEvent.getTaskId());

        future.get();

        verify(testee).preHandle(Mockito.eq(Mockito.mock(DelegateExecution.class)));
        verify(testee).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowableAdaptorMock, times(1)).getTaskInstanceByActivityId(activityId, 1L);
        verify(flowInstanceRepositoryMock, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
        verifyNoMoreInteractions(flowableAdaptorMock);
        verifyNoMoreInteractions(eventPublisherMock);
        verifyNoMoreInteractions(flowInstanceRepositoryMock);
    }

    @Test
    public void testException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenThrow(new RuntimeException());

        String activityId = "activityId";
        when(flowableAdaptorMock.getTaskInstanceByActivityId(activityId, 1L)).thenThrow(new RuntimeException());

        Callable<String> callableMock = mock(Callable.class);
        when(testee.initCallable(Mockito.mock(DelegateExecution.class))).thenReturn(callableMock);

        ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisherMock, times(1)).publishEvent(eventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = eventCaptor.getValue();

        // When
        try {
            testee.get();
            fail();
        } catch (ServiceTaskError e) {
            // Then
            verify(flowableAdaptorMock, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
            verify(flowableAdaptorMock, times(1)).getTaskInstanceByActivityId(activityId, 1L);
            verify(eventPublisherMock, times(1)).publishEvent(eventCaptor.capture());
            assertEquals("Flowable error occurred", e.getMessage());
            assertEquals("serviceTaskStartedEvent", serviceTaskStartedEvent.getTaskId());
        }

        verify(callableMock, never()).call();
        verify(testee, never()).preHandle(Mockito.any(DelegateExecution.class));
        verify(testee, never()).updateFlowInstanceStatus(FlowStatus.FAILED);
        verify(flowableAdaptorMock, times(2)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptorMock, times(2)).getTaskInstanceByActivityId(activityId, 1L);
        verify(flowInstanceRepositoryMock, never()).updateStatusById(1L, FlowStatus.FAILED);
        verify(flowInstanceRepositoryMock, never()).updateStatusById(1L, FlowStatus.COMPLETED);
        verifyNoMoreInteractions(flowableAdaptorMock);
        verifyNoMoreInteractions(eventPublisherMock);
        verifyNoMoreInteractions(flowInstanceRepositoryMock);
    }
}