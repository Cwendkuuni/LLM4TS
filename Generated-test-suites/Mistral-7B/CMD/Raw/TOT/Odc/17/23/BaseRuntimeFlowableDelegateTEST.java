package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> testee;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private DelegateExecution execution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testee.setEventPublisher(eventPublisher);
        testee.setFlowableAdaptor(flowableAdaptor);
        testee.setFlowInstanceRepository(flowInstanceRepository);
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testInitialization() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        testee.initCallable(execution);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertEquals(FlowStatus.STARTED, testee.getFlowStatus());
        assertEquals(Long.MAX_VALUE, testee.getFlowInstanceId());
        assertEquals(null, testee.getTargetTaskInstanceId());
        assertNull(testee.getReturnObject());
        assertNull(testee.getThrown());
        assertFalse(testee.isDone());
        Mockito.verifyNoInteractions(eventPublisher, flowInstanceRepository);
    }

    @Test
    public void testExecutionWithSuccess() throws Exception {
        Callable<String> callable = () -> "Test result";
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Mockito.mock(FlowTaskInstance.class));
        Mockito.when(callable.call()).thenReturn("Test result");

        testee.initCallable(execution);
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        testee.preHandle(execution);
        testee.run(execution);
        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
        assertEquals("Test result", testee.get());
        assertTrue(testee.isDone());
        verify(eventPublisher, never()).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, never()).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testExecutionWithException() throws Exception {
        Callable<String> callable = () -> { throw new RuntimeException("Test error"); };
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Mockito.mock(FlowTaskInstance.class));
        Mockito.when(callable.call()).thenThrow(new RuntimeException("Test error"));

        testee.initCallable(execution);
        testee.preHandle(execution);
        testee.run(execution);
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.FAILED);
        assertNull(testee.get());
        assertTrue(testee.isDone());
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, never()).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testInterruption() throws Exception {
        Callable<String> callable = () -> { Thread.sleep(1000); return "Test result"; };
        testee.initCallable(execution);
        testee.preHandle(execution);
        testee.run(execution);
        testee.get();
        assertTrue(testee.isDone());
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, never()).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testTimeout() throws Exception {
        Callable<String> callable = () -> { Thread.sleep(2000); return "Test result"; };
        testee.initCallable(execution);
        testee.preHandle(execution);
        testee.run(execution);
        try {
            testee.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // expected
        }
        assertTrue(testee.isDone());
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, never()).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }
}