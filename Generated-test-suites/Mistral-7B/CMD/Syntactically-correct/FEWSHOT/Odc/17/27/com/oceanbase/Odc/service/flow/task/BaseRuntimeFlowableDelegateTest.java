package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        assertFalse(delegate.isDone());

        delegate.run(Mockito.mock(DelegateExecution.class));
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        String result = delegate.get();
        assertEquals("result", result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        String result = delegate.get(1, TimeUnit.SECONDS);
        assertEquals("result", result);
    }

    @Test
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "result";
                };
            }
        };

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
            // Expected
        }
    }

    @Test
    public void testGetWithExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Test Exception");
            }
        };

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        try {
            delegate.get();
            fail("Expected ExecutionException");
        } catch (ExecutionException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);

        delegate.init(eventPublisher);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        ArgumentCaptor<String> processDefinitionIdCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionIdCaptor.capture());
        assertEquals("processDefinitionId", processDefinitionIdCaptor.getValue());

        Optional<Long> flowInstanceIdOptional = Optional.of(1L);
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionCaptor.getValue()))
                .thenReturn(flowInstanceIdOptional);

        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        Optional<FlowTaskInstance> flowTaskInstanceOptional = Optional.of(flowTaskInstance);
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(flowTaskInstanceOptional);

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals("processDefinitionId", delegate.getActivityId());
        assertEquals("activityId", delegate.getTargetTaskInstanceId());
        assertEquals(TaskType.TASK_TYPE_1, delegate.getTaskType());
        assertEquals(ExecutionStrategyConfig.builder().build(), delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBindToFlowTaskInstanceWithInvalidTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        FlowTaskInstance taskInstance = null;
        delegate.bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);

        delegate.init(eventPublisher);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
    }

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };

        EventPublisher eventPublisher = mock(EventPublisher.class);

        delegate.init(eventPublisher);

        verify(eventPublisher).addEventListener(Mockito.isInstance(delegate.getActiveTaskStatisticsListener()));
        verify(eventPublisher).addEventListener(Mockito.isInstance(delegate.getTaskInstanceCreatedlistener()));
    }

    private ArgumentCaptor<String> processDefinitionCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    public void testRun() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // preHandle should not be called in test case
            }
        };

        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);

        delegate.init(eventPublisher);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Mockito.when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        Mockito.when(execution.getCurrentActivityId()).thenReturn("activityId");

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        Mockito.verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        assertEquals("activityId", serviceTaskStartedEventCaptor.getValue().getTaskId());
        assertEquals(Thread.currentThread().getName(), serviceTaskStartedEventCaptor.getValue().getThreadName());

        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionCaptor.capture()))
                .thenReturn(Optional.of(1L));

        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(Optional.of(flowTaskInstance));

        delegate.run(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals("activityId", delegate.getActivityId());
        assertEquals("activityId", delegate.getTargetTaskInstanceId());
        assertEquals(TaskType.TASK_TYPE_1, delegate.getTaskType());
        assertEquals(ExecutionStrategyConfig.builder().build(), delegate.getStrategyConfig());

        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.RUNNING);
        verify(flowTaskInstance).dealloc();
    }
}