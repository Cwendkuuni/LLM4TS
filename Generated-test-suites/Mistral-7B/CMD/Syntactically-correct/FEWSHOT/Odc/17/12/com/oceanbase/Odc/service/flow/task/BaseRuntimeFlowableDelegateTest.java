package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

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
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;
    @Captor
    private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerCaptor;
    @Captor
    private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

    private BaseRuntimeFlowableDelegate<String> delegate;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private CountDownLatch latch;

    @org.junit.Before
    public void setup() {
        this.taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        this.activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = RetryExecutor.builder().build();
        this.latch = new CountDownLatch(1);
        this.delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "result";
                    }
                };
            }
        };
        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
    }

    @org.junit.Test
    public void testInit() {
        verify(eventPublisher, never()).removeEventListener(taskInstanceCreatedlistenerCaptor.capture());
        verify(eventPublisher, never()).removeEventListener(activeTaskStatisticsListenerCaptor.capture());
        verify(flowableAdaptor, never()).getFlowInstanceIdByProcessDefinitionId(any(String.class));
    }

    @org.junit.Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), any(Long.class))).thenReturn(mock(FlowTaskInstance.class));

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getId()).thenReturn(1L);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertSame(delegate, event.getTask());
        assertEquals("activityId", event.getThreadName());

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId"));
        verify(flowableAdaptor).getTaskInstanceByActivityId(eq("activityId"), eq(1L));
        verify(taskInstanceCreatedlistener).handleTaskInstanceCreated(eq(mock(FlowTaskInstance.class)));
        verify(activeTaskStatisticsListener).handleTaskStarted(any());

        // Test done flag
        assertTrue(delegate.isDone());
    }

    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.get();

        verify(latch).await();
        verify(latch).countDown();
    }

    @org.junit.Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(3, TimeUnit.MILLISECONDS);

        verify(latch, never()).await();
        verify(latch).countDown();
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        PreConditions.stubValidate(null, null, null, null);
        delegate.updateFlowInstanceStatus(FlowStatus.IN_PROGRESS);
        verify(flowInstanceRepository).updateStatusById(eq(1L), eq(FlowStatus.IN_PROGRESS));
    }
}