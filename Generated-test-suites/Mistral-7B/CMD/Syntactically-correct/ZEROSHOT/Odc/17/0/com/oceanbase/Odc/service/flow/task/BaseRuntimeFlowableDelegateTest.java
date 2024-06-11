package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableTestConfig;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventArgumentCaptor;

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private CountDownLatch latch;
    private Callable<Integer> callableMock;
    private DelegateExecution executionMock;
    private EventPublisher eventPublisherMock;
    private FlowableAdaptor flowableAdaptorMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private RetryExecutor retryExecutorMock;
    private TaskInstanceCreatedListener taskInstanceCreatedlistenerMock;
    private ActiveTaskStatisticsListener activeTaskStatisticsListenerMock;

    @Before
    public void setUp() {
        callableMock = mock(Callable.class);
        executionMock = mock(Execution.class);
        eventPublisherMock = mock(EventPublisher.class);
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        flowInstanceRepositoryMock = mock(FlowInstanceRepository.class);
        retryExecutorMock = mock(RetryExecutor.class);
        taskInstanceCreatedlistenerMock = mock(TaskInstanceCreatedListener.class);
        activeTaskStatisticsListenerMock = mock(ActiveTaskStatisticsListener.class);
        delegate = new BaseRuntimeFlowableDelegate<Integer>() {{
            this.retryExecutor = retryExecutorMock;
            this.latch = latch = new CountDownLatch(1);
            this.taskInstanceCreatedlistener = taskInstanceCreatedlistenerMock;
            this.activeTaskStatisticsListener = activeTaskStatisticsListenerMock;
            this.flowableAdaptor = flowableAdaptorMock;
            this.eventPublisher = eventPublisherMock;
            this.flowInstanceRepository = flowInstanceRepositoryMock;
        }};
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        delegate.init(eventPublisherMock);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisherMock);
        delegate.run(executionMock);
        delegate.get();
        assertNull(delegate.thrown);
        assertSame(delegate.returnObject, 0);
        verify(eventPublisherMock).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgumentCaptor.getValue();
        assertTrue(serviceTaskStartedEvent.getTask() instanceof BaseRuntimeFlowableDelegate);
        assertSame(serviceTaskStartedEvent.getThread(), Thread.currentThread());
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisherMock);
        delegate.run(executionMock);
        delegate.get(2, TimeUnit.SECONDS);
    }

    @Test
    public void testInitCallable() {
        delegate.initCallable(executionMock);
        verify(flowableAdaptorMock).getFlowInstanceIdByProcessDefinitionId(anyString());
    }

    @Test
    public void testPreHandle() {
        delegate.preHandle(executionMock);
    }

    @Test
    public void testExecute() {
        delegate.execute(executionMock);
        eventPublisherMock.removeEventListener(taskInstanceCreatedlistenerMock);
        eventPublisherMock.removeEventListener(activeTaskStatisticsListenerMock);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunException() {
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException());
        delegate.init(eventPublisherMock);
        delegate.run(executionMock);
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisherMock).publishEvent(any(ServiceTaskError.class));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstanceMock = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstanceMock);
        verify(flowTaskInstanceMock).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(retryExecutorMock).run(any(Callable.class), any(Consumer.class));
    }
}