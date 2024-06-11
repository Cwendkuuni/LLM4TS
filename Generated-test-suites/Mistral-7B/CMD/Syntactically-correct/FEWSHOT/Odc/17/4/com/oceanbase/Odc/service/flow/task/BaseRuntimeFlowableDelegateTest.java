package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testBaseRuntimeFlowableDelegate() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate() {
            @Override
            protected Callable<T> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        delegate.init(eventPublisher);

        verify(eventPublisher, times(1)).addEventListener(isA(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(isA(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
            }
        };

        delegate.init(eventPublisher);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getFlowInstanceId()).thenReturn("flowInstanceId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId")))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L)))
                .thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
            }
        };

        delegate.init(eventPublisher);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getFlowInstanceId()).thenReturn("flowInstanceId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId")))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L)))
                .thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.returnObject = "result";
        delegate.thrown = new RuntimeException("test exception");
        delegate.done = true;

        CountDownLatch latch = new CountDownLatch(1);
        when(callable.call()).thenReturn("result");
        delegate.latch = latch;

        String result = delegate.get();

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(eventPublisher);
        assertEquals("result", result);
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
            }
        };

        delegate.init(eventPublisher);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getFlowInstanceId()).thenReturn("flowInstanceId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId")))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L)))
                .thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.returnObject = "result";
        delegate.thrown = new RuntimeException("test exception");
        delegate.done = true;

        CountDownLatch latch = new CountDownLatch(1);
        when(callable.call()).thenThrow(new RuntimeException("test exception"));
        delegate.latch = latch;

        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
            fail("Should throw TimeoutException");
        } catch (TimeoutException e) {
            // expected
        }

        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>();

        assertTrue(delegate.isDone());
        delegate.done = false;
        assertFalse(delegate.isDone());
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        delegate.init(eventPublisher);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getFlowInstanceId()).thenReturn("flowInstanceId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId")))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("activityId"), eq(1L)))
                .thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        delegate.initTargetTaskInstanceId(execution);

        assertEquals("flowInstanceId", delegate.flowInstanceId);
        assertEquals("activityId", delegate.activityId);
        assertEquals("processDefinitionId", delegate.processDefinitionId);
        assertEquals("flowInstanceId", delegate.flowInstanceId);
        assertEquals("activityId", delegate.activityId);
        assertEquals("1", String.valueOf(delegate.targetTaskInstanceId));
        assertEquals("activityId", delegate.activityId);
        assertEquals("processDefinitionId", delegate.processDefinitionId);
        assertEquals("1", String.valueOf(delegate.flowInstanceId));
        assertEquals(TaskType.TASK, delegate.taskType);
        assertEquals(null, delegate.strategyConfig);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.init(eventPublisher);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        when(flowInstanceRepository.updateStatusById(eq("flowInstanceId"), eq(FlowStatus.COMPLETED)))
                .thenReturn(true);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(eq("flowInstanceId"), eq(FlowStatus.COMPLETED));
    }
}