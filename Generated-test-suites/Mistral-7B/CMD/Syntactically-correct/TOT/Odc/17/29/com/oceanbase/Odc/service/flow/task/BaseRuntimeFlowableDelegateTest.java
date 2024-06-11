package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}.
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private CountDownLatch latch;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setup() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        latch = new CountDownLatch(1);
        retryExecutor = Mockito.mock(RetryExecutor.class);
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

        when(delegate.getFlowInstanceId()).thenReturn(1L);
        when(delegate.getActivityId()).thenReturn("activityId");
        when(delegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(delegate.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(delegate.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        when(eventPublisher.publishEvent(any())).thenReturn(null);
        when(eventPublisher.removeEventListener(any()))
                .thenReturn(true);

        when(retryExecutor.run(any(), any()))
                .thenAnswer(i -> {
                    if (i.getArgument(0) instanceof Callable) {
                        Callable<Object> callable = (Callable<Object>) i.getArgument(0);
                        return callable.call();
                    }
                    return null;
                });

        delegate.init(eventPublisher);
        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testRun() throws Exception {
        when(delegate.preHandle(execution)).thenReturn();

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        ArgumentCaptor<Long> flowInstanceIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowInstanceRepository).updateStatusById(flowInstanceIdArgumentCaptor.capture(), eq(FlowStatus.STARTED));
        assertEquals(1L, flowInstanceIdArgumentCaptor.getValue());
    }

    @Test
    public void testExecute() throws Exception {
        executeDelegate(delegate, execution);
    }

    @Test
    public void testIsDone() throws Exception {
        when(delegate.isDone()).thenReturn(false);
        assertEquals(false, delegate.isDone());

        when(delegate.isDone()).thenReturn(true);
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.isDone()).thenReturn(false);
        delegate.get();
        verify(latch).await();

        when(delegate.isDone()).thenReturn(true);
        delegate.get();
        verifyNoMoreInteractions(latch);

        when(delegate.isDone()).thenReturn(false);
        delegate.get(1, TimeUnit.SECONDS);
        verify(latch).await(1, TimeUnit.SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.isDone()).thenReturn(false);
        delegate.get(1, TimeUnit.SECONDS);
        verify(latch).await(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetThrowsException() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.isDone()).thenReturn(true);
        doThrow(new ServiceTaskError("Exception")).when(delegate).call();
        latch.countDown();

        delegate.get();
        verify(delegate).get();
        verify(delegate).get();
        verifyThrows(ServiceTaskError.class, () -> delegate.get());
    }

    @Test
    public void testGetReturnsValue() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.isDone()).thenReturn(true);
        when(delegate.call()).thenReturn("VALUE");
        latch.countDown();

        assertEquals("VALUE", delegate.get());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.STARTED);
        verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(anyLong(), eq(FlowStatus.STARTED)),
                isA(Callable.class));
    }

    private void executeDelegate(BaseRuntimeFlowableDelegate<Object> delegate, DelegateExecution execution) throws Exception {
        delegate.execute(execution);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }
}