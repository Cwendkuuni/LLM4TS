package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.exception.PreConditionsException;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableTestUtil;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        this.eventPublisher = mock(EventPublisher.class);
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = RetryExecutor.builder().build();
        this.delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException();
            }
        };
        this.delegate.init(eventPublisher);
        this.execution = mock(DelegateExecution.class);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(FlowableTestUtil.createFlowTaskInstance());

        delegate.run(execution);

        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, any()));
        verify(this.delegate).preHandle(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(FlowableTestUtil.createFlowTaskInstance());

        delegate.initTargetTaskInstanceId(execution);
        delegate.bindToFlowTaskInstance(FlowableTestUtil.createFlowTaskInstance());

        delegate.get();

        verify(eventPublisher).removeEventListener(argThat(listener -> listener instanceof TaskInstanceCreatedListener));
        verify(eventPublisher).removeEventListener(argThat(listener -> listener instanceof ActiveTaskStatisticsListener));
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(FlowableTestUtil.createFlowTaskInstance());

        CountDownLatch latch = new CountDownLatch(1);
        delegate.init(eventPublisher);
        delegate.initTargetTaskInstanceId(execution);
        delegate.bindToFlowTaskInstance(FlowableTestUtil.createFlowTaskInstance());
        delegate.get(1, TimeUnit.MILLISECONDS);

        verify(eventPublisher).removeEventListener(argThat(listener -> listener instanceof TaskInstanceCreatedListener));
        verify(eventPublisher).removeEventListener(argThat(listener -> listener instanceof ActiveTaskStatisticsListener));
        assertTrue(latch.await(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testIsDone() {
        delegate.isDone();
        assertFalse(delegate.isDone());

        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testExecute() throws Exception {
        delegate.execute(execution);

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(this.delegate).preHandle(execution);
    }

    @Test
    public void testGetReturnObject() throws Exception {
        Callable<Object> callable = mock(Callable.class);
        Object result = "result";
        when(callable.call()).thenReturn(result);

        delegate.returnObject = result;
        assertEquals(result, delegate.get());
    }

    @Test
    public void testGetThrowException() throws Exception {
        Callable<Object> callable = mock(Callable.class);
        RuntimeException exception = new RuntimeException("test");
        when(callable.call()).thenThrow(exception);

        delegate.thrown = exception;
        assertThrows(RuntimeException.class, delegate::get);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(retryExecutor).run(() -> {
            try {
                flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED);
            } catch (Exception e) {
                return null;
            }
            return true;
        }, any());
    }

    @Test(expected = PreConditionsException.class)
    public void testPreconditionsException() {
        delegate.preHandle(execution);

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId", "processDefinitionId",
                () -> Optional.empty(), () -> {});
    }
}