package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            {
                this.eventPublisher = eventPublisher;
                this.flowableAdaptor = flowableAdaptor;
                this.flowInstanceRepository = flowInstanceRepository;
                this.activeTaskStatisticsListener = activeTaskStatisticsListener;
                this.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
            }
        };
        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testRun() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(new FlowTaskInstance());

        // When
        delegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, activeTaskStatisticsListener,
                taskInstanceCreatedlistener);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(new FlowTaskInstance());

        // When
        delegate.get();

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, activeTaskStatisticsListener,
                taskInstanceCreatedlistener);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithError() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Initialization task failed"));

        // When
        delegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(log).warn(anyString(), any(Throwable.class));
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, activeTaskStatisticsListener,
                taskInstanceCreatedlistener);
        throw new ServiceTaskError(any(Throwable.class));
    }

    @Test
    public void testInitCallable() {
        // Given
        Callable<Object> callable = delegate.initCallable(execution);

        // Then
        assertNotNull(callable);
    }

    @Test(expected = IllegalStateException.class)
    public void testBindToFlowTaskInstanceWithNullTaskInstance() {
        // Given
        FlowTaskInstance nullTaskInstance = null;

        // When
        delegate.bindToFlowTaskInstance(nullTaskInstance);

        // Then
        throw new IllegalStateException("taskInstance cannot be null");
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Given
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        // Given
        FlowTaskInstance taskInstance = new FlowTaskInstance();

        // When
        delegate.bindToFlowTaskInstance(taskInstance);

        // Then
        verify(taskInstance).bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
        verify(log).info("Monitor task instance creation and bind tasks, taskInstanceId={}, activityId={}",
                taskInstance.getId(), delegate.getActivityId());
    }

    @Test
    public void testPreHandle() {
        // Given

        // When
        delegate.preHandle(execution);

        // Then
        verifyNoInteractions(flowableAdaptor, flowInstanceRepository, activeTaskStatisticsListener,
                taskInstanceCreatedlistener);
    }

    @Test
    public void testExecute() {
        // Given

        // When
        delegate.execute(execution);

        // Then
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testIsDone() {
        // Given

        // When
        boolean done = delegate.isDone();

        // Then
        assertTrue(done);
    }
}