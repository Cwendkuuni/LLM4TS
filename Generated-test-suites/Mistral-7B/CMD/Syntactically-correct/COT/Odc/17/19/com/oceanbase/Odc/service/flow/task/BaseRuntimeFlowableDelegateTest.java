package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

/**
 * JUnit test file for BaseRuntimeFlowableDelegate class
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private CountDownLatch latch;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void init() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);
        latch = new CountDownLatch(1);
        taskInstanceCreatedlistener = new TaskInstanceCreatedListener(null);
        activeTaskStatisticsListener = new ActiveTaskStatisticsListener(null);

        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        delegate = new BaseRuntimeFlowableDelegate<String>() {

            @Override
            protected void initTargetTaskInstanceId(DelegateExecution execution) throws Exception {
                // no-op
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> null;
            }

            @Override
            protected void preHandle(DelegateExecution execution) throws Exception {
                // no-op
            }

            @Override
            protected void execute(DelegateExecution execution) {
                // no-op
            }

            @Override
            public void bindToFlowTaskInstance(FlowTaskInstance taskInstance) {
                // no-op
            }

            @Override
            protected void updateFlowInstanceStatus(FlowStatus flowStatus) {
                // no-op
            }
        };

        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;
        delegate.latch = latch;
        delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;

        delegate.init(eventPublisher);
    }

    @Test
    public void testInit() {
        delegate.init(null);
        verify(eventPublisher, never()).removeEventListener(any());
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListener);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullEventPublisher() {
        delegate.init(null);
    }

    @Test
    public void testRun_normal() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_exception() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new Exception());

        delegate.run(execution);

        ArgumentCaptor<ServiceTaskError> errorCaptor = ArgumentCaptor.forClass(ServiceTaskError.class);
        verify(eventPublisher).publishEvent(errorCaptor.capture());
        ServiceTaskError error = errorCaptor.getValue();
        assertSame("processDefinitionId", error.getCause().getMessage());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());

        delegate.get();

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test(expected = InterruptedException.class)
    public void testGet_interrupted() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());

        latch.countDown();
        Thread.currentThread().interrupt();

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_exception() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new ExecutionException(new Exception()));

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());

        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());

        delegate.done = true;

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet_normal() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());

        delegate.get();

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
        assertEquals(null, delegate.get());
    }

    @Test
    public void testGet_exception() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new ExecutionException(new Exception()));

        try {
            delegate.get();
            fail();
        } catch (ExecutionException e) {
            assertSame(e.getCause(), new Exception());
        }

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testGet_timeout() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(new FlowTaskInstance());

        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Optional<Boolean> result = Optional.of(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testUpdateFlowInstanceStatus_failure() {
        Optional<Boolean> result = Optional.of(false);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

}