package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
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
    private Execution execution;
    private DelegateExecution mockExecution;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    @Before
    public void init() {
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = mock(RetryExecutor.class);
        this.execution = mock(Execution.class);
        this.mockExecution = mock(DelegateExecution.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId((String) anyObject())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId((String) anyObject(), anyLong())).thenReturn(new FlowTaskInstance());
        when(eventPublisher.publishEvent((EventPublisher) anyObject(), (Object) anyObject())).thenReturn(null);
        when(flowInstanceRepository.updateStatusById((long) anyObject(), (FlowStatus) anyObject())).thenReturn(1);

        delegate = new BaseRuntimeFlowableDelegate() {
            @Override
            protected void initTargetTaskInstanceId(DelegateExecution execution) {
                initTargetTaskInstanceId(mockExecution);
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "success";
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
    }

    @Test
    public void testRun() throws InterruptedException, ExecutionException, TimeoutException {
        when(mockExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(mockExecution.getCurrentActivityId()).thenReturn("activityId");

        delegate.run(mockExecution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture(), any());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertEquals("BaseRuntimeFlowableDelegate", serviceTaskStartedEvent.getTaskName());
        assertEquals(this.delegate, serviceTaskStartedEvent.getTask());

        assertEquals(1, delegate.getFlowInstanceId());
        assertEquals(TaskType.BASE_RUNTIME, delegate.getTaskType());
        assertEquals(FlowStatus.RUNNING, delegate.getStatus());
        assertNull(delegate.getThrown());
        assertEquals("success", delegate.get());
        assertEquals(FlowStatus.COMPLETED, delegate.getStatus());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithError() throws InterruptedException, ExecutionException, TimeoutException {
        when(mockExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(mockExecution.getCurrentActivityId()).thenReturn("activityId");

        when(flowableAdaptor.getTaskInstanceByActivityId((String) anyObject(), anyLong())).thenThrow(new RuntimeException());

        delegate.run(mockExecution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture(), any());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertEquals("BaseRuntimeFlowableDelegate", serviceTaskStartedEvent.getTaskName());
        assertEquals(this.delegate, serviceTaskStartedEvent.getTask());

        assertEquals(1, delegate.getFlowInstanceId());
        assertEquals(TaskType.BASE_RUNTIME, delegate.getTaskType());
        assertEquals(FlowStatus.ERROR, delegate.getStatus());
        assertNotNull(delegate.getThrown());
    }

}