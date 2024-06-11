package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, "activityId", TaskType.TASK_TYPE_1, ExecutionStrategyConfig.builder().build())));

        when(flowInstanceRepository.updateStatusById(anyLong(), eq(FlowStatus.COMPLETED)))
                .thenReturn(true);

        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };

        delegate.init(eventPublisher);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);

        assertSame(eventPublisher, delegate.eventPublisher);
        assertSame(flowableAdaptor, delegate.flowableAdaptor);
        assertSame(flowInstanceRepository, delegate.flowInstanceRepository);
        assertNull(delegate.returnObject);
        assertNull(delegate.thrown);
        assertFalse(delegate.done);
        assertEquals(1, delegate.latch.getCount());
        verify(eventPublisher).addEventListener(isA(TaskInstanceCreatedListener.class));
        verify(eventPublisher).addEventListener(isA(ActiveTaskStatisticsListener.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Error getting flow instance id"));

        delegate.init(eventPublisher);
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(execution).setVariableLocal("thrown", eq(new ServiceTaskError("Error getting flow instance id")));
    }

    @Test
    public void testRunSuccess() throws Exception {
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));
        String result = callable.call();

        delegate.init(eventPublisher);
        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getVariableLocal("returnObject")).thenReturn(result);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(isA(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(isA(ActiveTaskStatisticsListener.class));
        verify(execution).setVariableLocal("thrown", null);
        verify(execution).setVariableLocal("done", eq(true));
        verify(execution).setVariableLocal("returnObject", eq(result));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));

        delegate.init(eventPublisher);

        delegate.get();

        verify(delegate.latch, never()).await();
        verify(delegate.latch, never()).await(anyLong(), any(TimeUnit.class));
        verify(delegate.latch, never()).countDown();
        verify(delegate).preHandle(any(DelegateExecution.class));
        verify(delegate).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));

        delegate.init(eventPublisher);

        delegate.get(1, TimeUnit.SECONDS);

        verify(delegate.latch, never()).await();
        verify(delegate.latch, never()).await(anyLong(), any(TimeUnit.class));
        verify(delegate.latch, never()).countDown();
        verify(delegate).preHandle(any(DelegateExecution.class));
        verify(delegate).updateFlowInstanceStatus(eq(FlowStatus.FAILED));
    }

    @Test
    public void testIsDone() {
        delegate.init(eventPublisher);

        assertFalse(delegate.isDone());

        delegate.done = true;

        assertTrue(delegate.isDone());
    }
}