package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<T> callable;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    @Captor
    private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

    @Captor
    private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerCaptor;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private CountDownLatch latch;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getFlowInstanceById(1L)).thenReturn(flowInstanceRepository);
        when(flowInstanceRepository.updateStatusById(1L, any(FlowStatus.class))).thenReturn(true);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(Optional.ofNullable(flowTaskInstance));
        when(flowTaskInstance.getId()).thenReturn(2L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        baseRuntimeFlowableDelegate.setRetryExecutor(retryExecutor);
        baseRuntimeFlowableDelegate.init(eventPublisher);
    }

    @After
    public void tearDown() {
        baseRuntimeFlowableDelegate = null;
    }

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListenerCaptor.capture());
        ActiveTaskStatisticsListener activeTaskStatisticsListener = activeTaskStatisticsListenerCaptor.getValue();
        assertSame(baseRuntimeFlowableDelegate, activeTaskStatisticsListener.getDelegate());
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistenerCaptor.capture());
        TaskInstanceCreatedListener taskInstanceCreatedlistener = taskInstanceCreatedlistenerCaptor.getValue();
        assertSame(baseRuntimeFlowableDelegate, taskInstanceCreatedlistener.getDelegate());
    }

    @Test
    public void testInitCallable() {
        when(callable.call()).thenReturn("Test Callable Result");
        baseRuntimeFlowableDelegate.initCallable(execution);
        assertNull(baseRuntimeFlowableDelegate.get());
        verify(execution).getCurrentActivityId();
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        Verify.notNull(baseRuntimeFlowableDelegate.getFlowInstanceRepository(), "FlowInstanceRepository");
        Verify.notNull(baseRuntimeFlowableDelegate.getFlowableAdaptor(), "FlowableAdaptor");
    }

    @Test
    public void testRun() throws Exception {
        when(callable.call()).thenReturn("Test Callable Result");
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertSame(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getDelegate());
        Verify.notNull(baseRuntimeFlowableDelegate.get(), "Get Object Result");
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertFalse(baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS));
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(callable.call()).thenThrow(new RuntimeException());
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertSame(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getDelegate());
        Verify.notNull(baseRuntimeFlowableDelegate.getThrown(), "Get Thrown Result");
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertFalse(baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS));
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FAILED);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn("Test Callable Result");
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test Callable Result", baseRuntimeFlowableDelegate.get());
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertFalse(baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS));
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.latch.countDown();
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenThrow(new RuntimeException());
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.latch.countDown();
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.activityId = "TestActivityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        baseRuntimeFlowableDelegate.preHandle(execution);
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

}