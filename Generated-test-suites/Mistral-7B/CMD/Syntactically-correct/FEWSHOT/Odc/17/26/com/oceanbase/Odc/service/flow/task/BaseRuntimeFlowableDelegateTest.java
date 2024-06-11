package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;

    @Before
    public void setup() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        execution = mock(DelegateExecution.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = Mockito.spy(RetryExecutor.builder().build());
    }

    @Test
    public void testInit() {
        when(delegate.initCallable(execution)).thenReturn((Callable<String>) () -> "Result");

        delegate.init(eventPublisher);

        verify(eventPublisher, times(1)).addEventListener(delegate.activeTaskStatisticsListener);
        verify(eventPublisher, times(1)).addEventListener(delegate.taskInstanceCreatedlistener);
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("ActivityId");
        when(execution.getProcessDefinitionId()).thenReturn("ProcessDefinitionId");

        doReturn(Optional.of(1L)).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("ProcessDefinitionId");
        doReturn(Optional.of(new FlowTaskInstance()))
                .when(flowableAdaptor)
                .getTaskInstanceByActivityId("ActivityId", 1L);

        delegate.activityId = "ActivityId";
        delegate.initTargetTaskInstanceId(execution);

        assertEquals("ActivityId", delegate.activityId);
        assertEquals(1L, delegate.flowInstanceId);
        assertEquals(TaskType.TASK_TYPE_1, delegate.taskType);

        Callable<String> callable = delegate.initCallable(execution);
        assertEquals("Result", callable.call());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getCurrentActivityId()).thenReturn("ActivityId");
        when(execution.getProcessDefinitionId()).thenReturn("ProcessDefinitionId");

        doReturn(Optional.of(1L)).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("ProcessDefinitionId");
        doReturn(Optional.of(new FlowTaskInstance()))
                .when(flowableAdaptor)
                .getTaskInstanceByActivityId("ActivityId", 1L);

        Callable<String> callable = delegate.initCallable(execution);
        delegate.returnObject = "Result";

        delegate.run(execution);
        delegate.latch.await();

        assertEquals("Result", delegate.get());
    }

    @Test
    public void testException() throws InterruptedException, ExecutionException {
        when(execution.getCurrentActivityId()).thenReturn("ActivityId");
        when(execution.getProcessDefinitionId()).thenReturn("ProcessDefinitionId");

        doThrow(new RuntimeException("Test Exception"))
                .when(delegate)
                .initCallable(execution);

        delegate.init(eventPublisher);

        delegate.run(execution);
        delegate.latch.await();

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskError.class));
    }

    @Test
    public void testPreHandle() {
        delegate.preHandle(execution);
        verify(delegate, times(0)).updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(retryExecutor, times(1)).run(any(), any());
    }
}