package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private FlowInstanceRepository flowInstanceRepository;
    private EventPublisher eventPublisher;
    private CountDownLatch latch;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private FlowableAdaptor flowableAdaptor;
    private DelegateExecution execution;
    private Callable<String> callable;

    @Before
    public void setup() {
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.latch = new CountDownLatch(1);
        this.retryExecutor = RetryExecutor.builder().retryIntervalMillis(100).retryTimes(3).build();
        this.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(mock(BaseRuntimeFlowableDelegate.class));
        this.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(mock(BaseRuntimeFlowableDelegate.class));
        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.execution = mock(DelegateExecution.class);
        this.callable = mock(Callable.class);
        this.delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }

            @Override
            protected void preHandle(DelegateExecution execution) {}
        };
        this.delegate.init(eventPublisher);
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        String result = "result";
        delegate.returnObject = result;
        delegate.latch.countDown();
        delegate.done = true;
        assertEquals(result, delegate.get());

        String exceptionMessage = "exception";
        delegate.thrown = new RuntimeException(exceptionMessage);
        assertThrows(ExecutionException.class, () -> delegate.get());
    }

    @Test
    public void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        delegate.latch.await(1, TimeUnit.MILLISECONDS);
        assertFalse(delegate.isDone());
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testConstructor() {
        assertEquals(0, delegate.getLatch().getCount());
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testInit() {
        assertEquals("", delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testRunExecute() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.run(execution);

        ArgumentCaptor<ServiceTaskStartedEvent> startEventArgCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher).publishEvent(startEventArgCaptor.capture());

        ServiceTaskStartedEvent startEvent = startEventArgCaptor.getValue();
        assertSame(delegate, startEvent.getServiceTask());
        assertSame(Thread.currentThread(), startEvent.getThread());
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);
    }

    @Test
    public void testExecute() throws Exception {
        delegate.execute(execution);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = mock(Callable.class);
        assertEquals(callable, delegate.initCallable(execution));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertTrue(Objects.equals(TaskType.TASK_TYPE_1, delegate.getTaskType()));
        assertNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.RUNNING;
        delegate.updateFlowInstanceStatus(flowStatus);
        verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(delegate.getFlowInstanceId(), flowStatus),
                Objects::nonNull);
    }

}