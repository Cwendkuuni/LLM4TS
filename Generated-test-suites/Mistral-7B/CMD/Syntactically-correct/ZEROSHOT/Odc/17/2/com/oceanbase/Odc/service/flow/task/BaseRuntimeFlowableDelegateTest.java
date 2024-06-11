package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Tests for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private CountDownLatch latch;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "test result";
            }
        };
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        execution = mock(DelegateExecution.class);
        latch = new CountDownLatch(1);
    }

    @Test
    public void testRunSuccess() throws Exception {
        delegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        assertEquals("test result", delegate.get());
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunFailure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException());
        delegate.run(execution);
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        throw new ServiceTaskError(new RuntimeException());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        delegate.get();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        latch.countDown();
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(1, TimeUnit.SECONDS);
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testIsDone() throws Exception {
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testInitTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance(1L, TaskType.TASK_TYPE_1));

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals(TaskType.TASK_TYPE_1, delegate.getTaskType());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitTaskInstanceIdWithDifferentId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance(2L, TaskType.TASK_TYPE_1));

        delegate.initTargetTaskInstanceId(execution);

        fail("Should throw an exception");
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_1);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }
}