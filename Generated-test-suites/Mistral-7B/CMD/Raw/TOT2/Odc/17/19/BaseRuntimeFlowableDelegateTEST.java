package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<Object> callable;

    @Mock
    private Future<Object> future;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(flowTaskInstance));
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowTaskInstance.getExecution()).thenReturn(execution);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        when(eventPublisher.removeEventListener(any())).thenReturn(true);
        when(eventPublisher.removeEventListener(any())).thenReturn(true);
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.STARTED)).thenReturn(true);
        when(flowTaskInstance.dealloc()).thenReturn(true);
        when(callable.call()).thenReturn(new Object());
        when(future.get()).thenReturn(new Object());
        when(future.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
        when(future.isDone()).thenReturn(true);
        when(future.isCancelled()).thenReturn(false);
        when(future.get()).thenReturn(new Object());
    }

    @Test
    public void testRunMethod() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        delegate.run(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.STARTED);
        verify(flowTaskInstance, times(1)).dealloc();
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunMethodFailure() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        when(callable.call()).thenThrow(new RuntimeException());
        delegate.run(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowTaskInstance, never()).bindServiceTask(delegate);
        verify(flowInstanceRepository, never()).updateStatusById(1L, FlowStatus.STARTED);
        verify(flowTaskInstance, times(1)).dealloc();
        verify(execution, times(1)).getExecutionStatus();
        verify(execution, times(1)).setVariableLocal("returnObject", new Object());
        verify(execution, times(1)).setVariableLocal("thrown", new RuntimeException());
        verify(execution, times(1)).setVariableLocal("done", true);
        verifyThrownWithMessage(ServiceTaskError.class, "Task execution failed with exception", new RuntimeException());
    }

    @Test
    public void testGetMethod() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        when(callable.call()).thenReturn(new Object());
        delegate.run(execution);
        assertEquals(new Object(), delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetMethodInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        when(callable.call()).thenReturn(new Object());
        delegate.run(execution);
        Thread.currentThread().interrupt();
        delegate.get();
        verify(execution, times(1)).isInterrupted();
    }

    @Test(expected = ExecutionException.class)
    public void testGetMethodExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        when(callable.call()).thenThrow(new RuntimeException());
        delegate.run(execution);
        delegate.get();
        verifyThrownWithMessage(ExecutionException.class, "Task execution failed with exception", new RuntimeException());
    }

    @Test(expected = TimeoutException.class)
    public void testGetMethodTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        delegate.run(execution);
        delegate.get(1, TimeUnit.SECONDS);
        verifyThrownWithMessage(TimeoutException.class, "Task execution took more than 1 second");
    }

    @Test
    public void testPreHandleMethod() throws Exception {
        delegate.preHandle(execution);
        verifyZeroInteractions(flowableAdaptor);
        verifyZeroInteractions(eventPublisher);
        verifyZeroInteractions(flowTaskInstance);
        verifyZeroInteractions(flowInstanceRepository);
    }

    @Test
    public void testExecuteMethod() throws Exception {
        delegate.execute(execution);
        verifyZeroInteractions(flowableAdaptor);
        verifyZeroInteractions(eventPublisher);
        verifyZeroInteractions(flowTaskInstance);
        verifyZeroInteractions(flowInstanceRepository);
    }

    @Test
    public void testGetExecutionStatusMethod() throws Exception {
        when(execution.isDone()).thenReturn(false);
        when(execution.getExecutionStatus()).thenReturn(null);
        assertNull(delegate.getExecutionStatus());
        when(execution.isDone()).thenReturn(true);
        when(execution.getExecutionStatus()).thenReturn("COMPLETED");
        assertEquals("COMPLETED", delegate.getExecutionStatus());
    }

    @Test
    public void testIsDoneMethod() throws Exception {
        when(execution.isDone()).thenReturn(false);
        assertFalse(delegate.isDone());
        when(execution.isDone()).thenReturn(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testInitTargetTaskInstanceIdMethod() throws Exception {
        delegate.initTargetTaskInstanceId(execution);
        verifyZeroInteractions(flowableAdaptor);
        verifyZeroInteractions(eventPublisher);
        verifyZeroInteractions(flowTaskInstance);
        verifyZeroInteractions(flowInstanceRepository);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
    }

    @Test
    public void testBindToFlowTaskInstanceMethod() throws Exception {
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatusMethod() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.STARTED);
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.STARTED);
    }

    @Test
    public void testInitCallableMethod() {
        // This method cannot be easily mocked, as it depends on user-defined implementation
    }

    private void verifyThrownWithMessage(Class<?> expectedException, String expectedMessage, Throwable expectedThrowable) {
        try {
            delegate.get();
            fail("Expected exception not thrown");
        } catch (Throwable throwable) {
            assertEquals(expectedException, throwable.getClass());
            assertEquals(expectedMessage, throwable.getMessage());
        }
    }

}