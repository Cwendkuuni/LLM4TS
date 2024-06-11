package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @org.junit.Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        assertNull(delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
        assertNull(delegate.getFlowableAdaptor());
        assertNull(delegate.getEventPublisher());
        assertNull(delegate.getFlowInstanceRepository());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
        assertNull(delegate.getLatch());
        assertNull(delegate.getRetryExecutor());
        assertNull(delegate.getTaskInstanceCreatedlistener());
        assertNull(delegate.getActiveTaskStatisticsListener());
    }

    @org.junit.Test
    public void testSetters() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();

        String activityId = "activityId";
        delegate.setActivityId(activityId);
        assertSame(activityId, delegate.getActivityId());

        Long targetTaskInstanceId = 1L;
        delegate.setTargetTaskInstanceId(targetTaskInstanceId);
        assertSame(targetTaskInstanceId, delegate.getTargetTaskInstanceId());

        TaskType taskType = TaskType.TASK_TYPE_1;
        delegate.setTaskType(taskType);
        assertSame(taskType, delegate.getTaskType());

        Long flowInstanceId = 1L;
        delegate.setFlowInstanceId(flowInstanceId);
        assertSame(flowInstanceId, delegate.getFlowInstanceId());

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        delegate.setStrategyConfig(strategyConfig);
        assertSame(strategyConfig, delegate.getStrategyConfig());

        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class, Answers.RETURNS_DEEP_STUBS);
        delegate.setFlowableAdaptor(flowableAdaptor);
        verify(flowableAdaptor).getDefaultFlowableEngine();

        EventPublisher eventPublisher = mock(EventPublisher.class);
        delegate.setEventPublisher(eventPublisher);

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        delegate.setFlowInstanceRepository(flowInstanceRepository);

        T returnObject = "returnObject";
        delegate.setReturnObject(returnObject);
        assertSame(returnObject, delegate.getReturnObject());

        Exception thrown = new Exception("thrown");
        delegate.setThrown(thrown);
        assertSame(thrown, delegate.getThrown());

        boolean done = true;
        delegate.setDone(done);
        assertTrue(delegate.isDone());

        CountDownLatch latch = new CountDownLatch(1);
        delegate.setLatch(latch);

        RetryExecutor retryExecutor = RetryExecutor.builder().build();
        delegate.setRetryExecutor(retryExecutor);

        TaskInstanceCreatedListener taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);

        ActiveTaskStatisticsListener activeTaskStatisticsListener = new ActiveTaskStatisticsListener(delegate);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
    }

    @org.junit.Test
    public void testRun_success() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = mock(DelegateExecution.class);

        String activityId = "activityId";
        when(execution.getCurrentActivityId()).thenReturn(activityId);

        Long targetTaskInstanceId = 1L;
        when(execution.getId()).thenReturn(targetTaskInstanceId);

        TaskType taskType = TaskType.TASK_TYPE_1;
        when(execution.getVariable("taskType")).thenReturn(taskType.name());

        Long flowInstanceId = 1L;
        when(execution.getProcessInstanceId()).thenReturn(flowInstanceId);

        Callable<String> callable = mock(Callable.class, Answers.RETURNS_DEEP_STUBS);

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenAnswer((Answer<Void>) invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.setTaskInstance(delegate);
            event.setThread(Thread.currentThread());
            return null;
        });

        delegate.setFlowableAdaptor(mock(FlowableAdaptor.class));
        delegate.setEventPublisher(eventPublisher);

        delegate.init(eventPublisher);

        delegate.run(execution);

        String result = delegate.get();
        assertNull(delegate.getThrown());
        assertSame(result, delegate.getReturnObject());
        assertTrue(delegate.isDone());
    }

    @org.junit.Test
    public void testRun_failure() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = mock(DelegateExecution.class);

        String activityId = "activityId";
        when(execution.getCurrentActivityId()).thenReturn(activityId);

        Long targetTaskInstanceId = 1L;
        when(execution.getId()).thenReturn(targetTaskInstanceId);

        Long flowInstanceId = 1L;
        when(execution.getProcessInstanceId()).thenReturn(flowInstanceId);

        Callable<String> callable = mock(Callable.class, Answers.RETURNS_DEEP_STUBS);
        when(callable.call()).thenThrow(new Exception("testException"));

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenAnswer((Answer<Void>) invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.setTaskInstance(delegate);
            event.setThread(Thread.currentThread());
            return null;
        });

        delegate.setFlowableAdaptor(mock(FlowableAdaptor.class));
        delegate.setEventPublisher(eventPublisher);

        delegate.init(eventPublisher);

        try {
            delegate.run(execution);
            fail();
        } catch (ServiceTaskError e) {
            assertSame("testException", e.getCause());
            assertSame("testException", delegate.getThrown().getCause());
            assertTrue(delegate.isDone());
        }
    }

    @org.junit.Test
    public void testGet_success() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = mock(DelegateExecution.class);

        String activityId = "activityId";
        when(execution.getCurrentActivityId()).thenReturn(activityId);

        Long targetTaskInstanceId = 1L;
        when(execution.getId()).thenReturn(targetTaskInstanceId);

        Long flowInstanceId = 1L;
        when(execution.getProcessInstanceId()).thenReturn(flowInstanceId);

        Callable<String> callable = mock(Callable.class, Answers.RETURNS_DEEP_STUBS);
        when(callable.call()).thenReturn("testResult");

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenAnswer((Answer<Void>) invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.setTaskInstance(delegate);
            event.setThread(Thread.currentThread());
            return null;
        });

        delegate.setFlowableAdaptor(mock(FlowableAdaptor.class));
        delegate.setEventPublisher(eventPublisher);

        delegate.init(eventPublisher);

        delegate.run(execution);

        String result = delegate.get();
        assertSame("testResult", result);
        assertTrue(delegate.isDone());
    }

    @org.junit.Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = mock(DelegateExecution.class);

        String activityId = "activityId";
        when(execution.getCurrentActivityId()).thenReturn(activityId);

        Long targetTaskInstanceId = 1L;
        when(execution.getId()).thenReturn(targetTaskInstanceId);

        Long flowInstanceId = 1L;
        when(execution.getProcessInstanceId()).thenReturn(flowInstanceId);

        Callable<String> callable = mock(Callable.class, Answers.RETURNS_DEEP_STUBS);

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenAnswer((Answer<Void>) invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.setTaskInstance(delegate);
            event.setThread(Thread.currentThread());
            return null;
        });

        delegate.setFlowableAdaptor(mock(FlowableAdaptor.class));
        delegate.setEventPublisher(eventPublisher);

        delegate.init(eventPublisher);

        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            assertTrue(delegate.isDone());
        }
    }

    @org.junit.Before
    public void initMocks() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
    }

    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

}