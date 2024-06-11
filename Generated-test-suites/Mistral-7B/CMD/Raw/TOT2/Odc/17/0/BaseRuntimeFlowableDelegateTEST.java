package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.doThrow;
import org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for BaseRuntimeFlowableDelegate.
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private Execution execution;
    @Mock
    private Callable<String> callable;
    @Mock
    private Future<String> future;

    private BaseRuntimeFlowableDelegate<String> delegate;
    private CountDownLatch latch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        delegate.init(eventPublisher);
        latch = new CountDownLatch(1);
        delegate.retryExecutor = new RetryExecutor.Builder().retryIntervalMillis(0).retryTimes(0).build();
        when(callable.call()).thenReturn("success");
        when(callable.getClass()).thenReturn(String.class.getDeclaredConstructor().getMethod("call"));
        delegate.flowableAdaptor = new FlowableAdaptor() {};
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.eventPublisher = eventPublisher;
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("testActivityId");
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(execution);
        verify(flowInstanceRepository).getById(eq(flowInstanceId), eq(FlowStatus.RUNNING));
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("testActivityId", flowInstanceId);
    }

    @Test
    public void testRun_success() throws Exception {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.run(execution);
        assertTrue(delegate.isDone());
        assertEquals("success", delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testRun_exception() throws Exception {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        doThrow(new RuntimeException("test exception")).when(callable).call();
        delegate.run(execution);
        assertTrue(delegate.isDone());
        assertNotNull(delegate.getThrown());
        assertEquals("test exception", delegate.getThrown().getMessage());
        verify(eventPublisher).publishEvent(any(ServiceTaskError.class));
    }

    @Test
    public void testGet_success() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.run(execution);
        latch.await();
        assertEquals("success", delegate.get());
        verify(callable).call();
    }

    @Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        when(callable.call()).thenThrow(new RuntimeException("test exception"));
        delegate.run(execution);
        latch.await();
        assertFalse(delegate.isDone());
        assertTrue(delegate.getThrown() instanceof TimeoutException);
        verify(callable, never()).call();
    }

    @Test(expected = ServiceTaskError.class)
    public void testGet_error() throws InterruptedException, ExecutionException {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.run(execution);
        assertEquals(FlowStatus.ERROR, delegate.getFlowStatus());
        latch.await();
        throw new ServiceTaskError(delegate.getThrown());
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.preHandle(execution);
        verify(flowInstanceRepository).updateStatusById(eq(flowInstanceId), eq(FlowStatus.RUNNING));
    }

    @Test(expected = IllegalStateException.class)
    public void testRun_alreadyDone() {
        delegate.activityId = "testActivityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.taskType = TaskType.TEST;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();
        delegate.done = true;
        delegate.run(execution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        assertEquals(delegate, flowTaskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.RUNNING);
        verify(flowInstanceRepository).updateStatusById(eq(flowInstanceId), eq(FlowStatus.RUNNING));
    }

    @Test
    public void testInit_alreadyInitialized() {
        delegate.init(eventPublisher);
        assertFalse(delegate.isInitialized());
        delegate.init(eventPublisher);
        assertTrue(delegate.isInitialized());
    }
}