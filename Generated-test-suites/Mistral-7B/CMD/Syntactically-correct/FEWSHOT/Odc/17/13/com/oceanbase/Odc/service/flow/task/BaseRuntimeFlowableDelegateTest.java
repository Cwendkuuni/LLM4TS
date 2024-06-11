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

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private FlowableAdaptor flowableAdaptor;

    @org.junit.Before
    public void setUp() {
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        retryExecutor = Mockito.mock(RetryExecutor.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);

        delegate = new BaseRuntimeFlowableDelegate<Object>() {{
            this.eventPublisher = eventPublisher;
            this.flowInstanceRepository = flowInstanceRepository;
            this.retryExecutor = retryExecutor;
            this.flowableAdaptor = flowableAdaptor;
        }};
        delegate.init(eventPublisher);
    }

    @org.junit.Test
    public void testInitTargetTaskInstanceId() {
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(1L));

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), eq(FlowStatus.COMPLETED))).thenReturn(true);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), eq(FlowStatus.COMPLETED));
    }

    @org.junit.Test
    public void testRun() throws Exception {
        Callable<Object> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.run(execution);

        verify(eventPublisher).publishEvent(instanceOf(ServiceTaskStartedEvent.class));
        verify(callable).call();
    }

    @org.junit.Test
    public void testPreHandle() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.preHandle(execution);

        verifyNoInteractions(execution);
    }

    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TASK_TYPE_1;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.initCallable = () -> callable;

        delegate.run(Mockito.mock(DelegateExecution.class));

        CountDownLatch latch = Mockito.mock(CountDownLatch.class);
        when(latch.await()).thenReturn(true);

        Object result = delegate.get();

        verify(latch).await();
        verify(callable).call();
        assertEquals(new Object(), result);
    }

    @org.junit.Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TASK_TYPE_1;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.initCallable = () -> callable;

        CountDownLatch latch = Mockito.mock(CountDownLatch.class);
        when(latch.await(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.SECONDS));
        verify(latch).await(eq(1), eq(TimeUnit.SECONDS));
        verifyZeroInteractions(callable);
    }

    @org.junit.Test
    public void testIsDone() {
        assertTrue(delegate.isDone());

        delegate.done = false;
        assertFalse(delegate.isDone());

        delegate.done = true;
        assertTrue(delegate.isDone());
    }
}