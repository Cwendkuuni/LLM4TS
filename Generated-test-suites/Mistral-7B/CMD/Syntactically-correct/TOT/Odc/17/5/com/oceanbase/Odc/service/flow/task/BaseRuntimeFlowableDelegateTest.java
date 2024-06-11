package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        activeTaskStatisticsListener = new ActiveTaskStatisticsListener(delegate);

        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected void init(EventPublisher eventPublisher) {
                this.eventPublisher = eventPublisher;
                this.flowableAdaptor = flowableAdaptor;
                this.flowInstanceRepository = flowInstanceRepository;
                this.retryExecutor = retryExecutor;
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "test result";
            }
        };

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
    }

    @Test
    public void testDelegateExecution() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        delegate.init(eventPublisher);
        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
        assertEquals("test result", delegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testDelegateExecutionWithError() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        delegate.init(eventPublisher);
        doThrow(new RuntimeException("Test error")).when(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowInstanceRepository, never()).updateStatusById(1L, FlowStatus.COMPLETED);
        throw new ServiceTaskError("Test error");
    }

    @Test
    public void testTaskInstanceCreatedListener() {
        ServiceTaskStartedEvent event = new ServiceTaskStartedEvent(delegate, Thread.currentThread());
        eventPublisher.publishEvent(event);

        verify(taskInstanceCreatedlistener, times(1)).onEvent(event);
    }

    @Test
    public void testActiveTaskStatisticsListener() {
        ArgumentCaptor<ActiveTaskStatisticsEvent> argumentCaptor = ArgumentCaptor.forClass(ActiveTaskStatisticsEvent.class);
        doNothing().when(eventPublisher).publishEvent(argumentCaptor.capture());

        delegate.run(new Execution("executionId"));

        ActiveTaskStatisticsEvent event = argumentCaptor.getValue();
        assertEquals(delegate, event.getServiceTask());
        assertEquals("executionId", event.getExecutionId());
    }

}