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
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private CountDownLatch latch;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setup() {
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = mock(RetryExecutor.class);
        this.latch = new CountDownLatch(1);
        this.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(null);
        this.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(null);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowableAdaptor.getFlowInstanceById(anyLong()))
                .thenReturn(mock(ProcessInstance.class));

        this.delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }

            @Override
            public void bindToFlowTaskInstance(FlowTaskInstance taskInstance) {}

            @Override
            public void updateFlowInstanceStatus(FlowStatus flowStatus) {}
        };

        delegate.init(eventPublisher);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;
        delegate.latch = latch;
        delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;
    }

    @Test
    public void testInit() {
        assertNotNull(delegate.getActivityId());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.run(mock(DelegateExecution.class));
        delegate.setDone(true);
        delegate.setThrown(new RuntimeException());
        delegate.setReturnObject("test");

        assertEquals("test", delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException {
        delegate.run(mock(DelegateExecution.class));
        Thread.currentThread().interrupt();
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        delegate.run(mock(DelegateExecution.class));
        delegate.setThrown(new RuntimeException());
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(mock(DelegateExecution.class));
        delegate.setDone(false);
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testIsDone() throws Exception {
        delegate.run(mock(DelegateExecution.class));
        delegate.setDone(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetReturnObject() throws Exception {
        delegate.run(mock(DelegateExecution.class));
        delegate.setReturnObject("test");
        assertEquals("test", delegate.get());
    }

    @Test
    public void testGetThrown() throws Exception {
        delegate.run(mock(DelegateExecution.class));
        delegate.setThrown(new RuntimeException());
        assertSame(new RuntimeException(), delegate.get());
    }

    @Test
    public void testInitializeTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.initTargetTaskInstanceId(execution);

        ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TaskType> taskTypeCaptor = ArgumentCaptor.forClass(TaskType.class);
        ArgumentCaptor<ExecutionStrategyConfig> strategyConfigCaptor = ArgumentCaptor.forClass(ExecutionStrategyConfig.class);

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), flowInstanceIdCaptor.capture());
        FlowTaskInstance flowTaskInstance = (FlowTaskInstance) flowableAdaptor.getTaskInstanceByActivityId("test", 1L).get();
        verify(flowableAdaptor).getFlowInstanceById(flowInstanceIdCaptor.getValue());

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals(TaskType.TEST_TASK_TYPE, delegate.getTaskType());
        assertEquals(ExecutionStrategyConfig.DEFAULT_EXECUTION_STRATEGY, delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(any());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED),
                Objects::nonNull);
    }
}