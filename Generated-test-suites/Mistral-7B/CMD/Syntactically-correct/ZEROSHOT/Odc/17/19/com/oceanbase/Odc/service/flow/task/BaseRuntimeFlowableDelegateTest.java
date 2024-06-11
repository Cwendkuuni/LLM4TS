package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private CountDownLatch latch;

    @Captor
    private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

    @Captor
    private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerCaptor;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        latch = new CountDownLatch(1);

        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void run(DelegateExecution execution) throws Exception {
                super.run(execution);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return mock(Callable.class);
            }
        };

        MockitoAnnotations.initMocks(this);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowableTaskInstance()));
    }

    @Test
    public void testInitialization() {
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListenerCaptor.capture());
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistenerCaptor.capture());
    }

    @Test
    public void testRunWithoutException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable).call();
        verify(execution).setVariable("result", "result");
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
        assertFalse(latch.await(1, TimeUnit.SECONDS));
        assertSame(Optional.of("result"), Optional.ofNullable(delegate.get()));
        assertTrue(delegate.isDone());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("exception"));

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable).call();
        assertNull(delegate.get());
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);

        Thread.sleep(100);
        Optional<String> result = delegate.get();
        assertTrue(result.isPresent());
        assertEquals("result", result.get());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);

        Thread.sleep(500);
        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowableTaskInstance taskInstance = mock(FlowableTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }
}