package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        assertEquals(RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build(), delegate.retryExecutor);
        assertEquals(1, delegate.latch.getCount());
        assertNull(delegate.taskInstanceCreatedlistener);
        assertNull(delegate.activeTaskStatisticsListener);
    }

    @Test
    public void testInitWithEventPublisher() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(delegate.taskInstanceCreatedlistener);
        verify(eventPublisher).addEventListener(delegate.activeTaskStatisticsListener);
    }

    @Test
    public void testRun() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                preHandleMock(execution);
            }
        };

        doReturn("activityId").when(execution).getCurrentActivityId();
        doReturn(1L).when(execution).getId();
        doReturn(new Long(12345)).when(execution).getProcessDefinitionId();

        delegate.run(execution);

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventArgumentCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(delegate.eventPublisher).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent event = serviceTaskStartedEventArgumentCaptor.getValue();
        assertEquals(Thread.currentThread(), event.getThread());
        assertEquals("activityId", event.getTaskInstanceId().getActivityId());
        assertEquals(1L, event.getTaskInstanceId().getId());
        assertEquals(12345, event.getFlowInstanceId());
    }

    @Test
    public void testGet() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> 1;
            }
        };
        delegate.init(null);
        assertEquals(1, delegate.get());
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetWithTimeout() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.init(null);
        assertNull(delegate.get(1, TimeUnit.SECONDS));
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetWithTimeoutException() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.init(null);
        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
        assertTrue(delegate.isDone());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };

        doReturn("activityId").when(execution).getCurrentActivityId();
        doReturn("processDefinitionId").when(execution).getProcessDefinitionId();
        doReturn(new FlowTaskInstance(1L, "taskType", FlowStatus.READY, 12345L, "TaskInstanceId", null, null, null, null, null, null)).when(flowableAdaptor).getTaskInstanceByActivityId("activityId", 12345L);
        doReturn(12345L).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(12345L, delegate.getFlowInstanceId());
        assertEquals("TaskInstanceId", delegate.getTargetTaskInstanceId());
        assertEquals("taskType", delegate.getTaskType());
        assertEquals(12345L, delegate.getFlowInstanceId());
        assertEquals("activityId", delegate.getActivityId());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.initTargetTaskInstanceId(null);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
        assertEquals(delegate, taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.flowInstanceRepository = flowInstanceRepository;

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(12345, FlowStatus.COMPLETED);
    }

    private void preHandleMock(DelegateExecution execution) {
        // empty implementation
    }
}