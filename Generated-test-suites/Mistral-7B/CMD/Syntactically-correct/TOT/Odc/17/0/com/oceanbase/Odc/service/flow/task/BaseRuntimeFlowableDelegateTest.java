package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
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

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setup() {
        this.execution = mock(DelegateExecution.class);
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = mock(RetryExecutor.class);
        this.taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        this.activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(this.flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(
                new FlowTaskInstance(1L, TaskType.TASK_TYPE_1, new ExecutionStrategyConfig()));

        this.delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "Test Result";
                    }
                };
            }
        };

        this.delegate.init(this.eventPublisher);
        this.delegate.flowableAdaptor = this.flowableAdaptor;
        this.delegate.eventPublisher = this.eventPublisher;
        this.delegate.flowInstanceRepository = this.flowInstanceRepository;
        this.delegate.retryExecutor = this.retryExecutor;
        this.delegate.taskInstanceCreatedlistener = this.taskInstanceCreatedlistener;
        this.delegate.activeTaskStatisticsListener = this.activeTaskStatisticsListener;
    }

    @Test
    public void testRun() throws Exception {
        this.delegate.run(this.execution);

        ArgumentCaptor<ServiceTaskStartedEvent> captor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(this.eventPublisher).publishEvent(captor.capture());
        ServiceTaskStartedEvent event = captor.getValue();
        assertEquals(this.delegate, event.getDelegate());
        assertEquals(Thread.currentThread(), event.getThread());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(this.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Test Exception"));
        this.delegate.run(this.execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        this.delegate.run(this.execution);
        this.delegate.get();
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        this.delegate.run(this.execution);
        Thread.currentThread().interrupt();
        this.delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        this.delegate.run(this.execution);
        this.delegate.thrown = new RuntimeException("Test Exception");
        this.delegate.done = true;
        this.delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        this.delegate.run(this.execution);
        Thread.currentThread().sleep(1001);
        this.delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_1, new ExecutionStrategyConfig());
        this.delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(this.delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        this.delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(this.flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

}