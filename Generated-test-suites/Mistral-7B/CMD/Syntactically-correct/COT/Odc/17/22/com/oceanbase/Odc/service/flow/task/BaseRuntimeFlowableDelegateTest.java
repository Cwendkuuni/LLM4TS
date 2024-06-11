package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.TestUtils;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 * @date 2023-03-31 10:21
 * @since ODC_release_3.3.0
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private CountDownLatch latch;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        execution = mock(DelegateExecution.class);
        latch = new CountDownLatch(1);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        activeTaskStatisticsListener = new ActiveTaskStatisticsListener(delegate);

        when(delegate.getActivityId()).thenReturn("activityId");
        when(delegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(delegate.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(delegate.getFlowInstanceId()).thenReturn(1L);
        when(delegate.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(delegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
        when(delegate.getEventPublisher()).thenReturn(eventPublisher);
        when(delegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance());
    }

    @Test
    public void testInitCallable() throws InterruptedException, ExecutionException {
        Callable<String> callable = delegate.initCallable(execution);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
        assertNull(callable);
    }

    @Test
    public void testBindToFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance taskInstance = new FlowTaskInstance();

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        assertSame(delegate.getTargetTaskInstanceId(), taskInstance.getId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws InterruptedException, ExecutionException {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testInitializeTaskFailed() throws InterruptedException, ExecutionException {
        when(delegate.initCallable(execution)).thenThrow(new Exception());

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegate).thrown = any(Exception.class);
        verify(delegate).done = true;
        verify(delegate).latch.countDown();
        throw new ServiceTaskError(any(Exception.class));
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        String result = "result";
        when(delegate.initCallable(execution)).thenReturn(() -> result);

        delegate.run(execution);

        assertEquals(result, delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        delegate.latch.countDown();
        delegate.done = true;
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        String exceptionMessage = "exceptionMessage";
        when(delegate.initCallable(execution)).thenThrow(new ExecutionException(new Exception(exceptionMessage)));

        delegate.run(execution);

        verify(delegate).thrown = any(Exception.class);
        throw new ExecutionException(any(Exception.class));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.latch.await(1, TimeUnit.SECONDS);
        delegate.done = false;
        delegate.get(1, TimeUnit.SECONDS);
        TestUtils.sleep(200);
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);

        verify(delegate).preHandle(execution);
        verify(delegate).initTargetTaskInstanceId(execution);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegate).initCallable(execution);
        verify(delegate).preHandle(execution);
        verify(delegate).returnObject = any();
        verify(delegate).thrown = null;
        verify(delegate).done = true;
        verify(delegate).latch.countDown();
    }

    @Test
    public void testExecute() throws Exception {
        delegate.execute(execution);

        verify(delegate).execute(execution);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

}