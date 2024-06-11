package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
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

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private EventPublisher eventPublisherMock;
    private FlowableAdaptor flowableAdaptorMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private RetryExecutor retryExecutorMock;
    private CountDownLatch latch;

    @Before
    public void setUp() {
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
        flowInstanceRepositoryMock = Mockito.mock(FlowInstanceRepository.class);
        retryExecutorMock = Mockito.mock(RetryExecutor.class);
        latch = new CountDownLatch(1);

        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected FlowableAdaptor flowableAdaptor() {
                return flowableAdaptorMock;
            }

            @Override
            protected FlowInstanceRepository flowInstanceRepository() {
                return flowInstanceRepositoryMock;
            }

            @Override
            protected RetryExecutor retryExecutor() {
                return retryExecutorMock;
            }

            @Override
            protected EventPublisher eventPublisher() {
                return eventPublisherMock;
            }

            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException();
            }
        };
        delegate.init(eventPublisherMock);
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisherMock);
        Mockito.verify(eventPublisherMock).addEventListener(Mockito.any(ActiveTaskStatisticsListener.class));
        Mockito.verify(eventPublisherMock).addEventListener(Mockito.any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRunWithSuccess() throws Exception {
        DelegateExecution executionMock = Mockito.mock(DelegateExecution.class);
        Callable<Object> callableMock = Mockito.mock(Callable.class);
        Mockito.when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);

        delegate.activityId = "activityId";
        delegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
        delegate.preHandle(executionMock);
        Mockito.when(callableMock.call()).thenReturn("result");

        Future<Object> future = delegate.call();
        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        Object result = future.get();
        assertEquals("result", result);
        Mockito.verify(flowInstanceRepositoryMock).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testRunWithException() throws Exception {
        DelegateExecution executionMock = Mockito.mock(DelegateExecution.class);
        Callable<Object> callableMock = Mockito.mock(Callable.class);
        Mockito.when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("test exception"));

        delegate.activityId = "activityId";

        try {
            delegate.run(executionMock);
            fail();
        } catch (Exception e) {
            assertEquals("test exception", e.getCause().getMessage());
            Mockito.verify(flowInstanceRepositoryMock).updateStatusById(1L, FlowStatus.FAILED);
        }
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callableMock = Mockito.mock(Callable.class);
        Mockito.when(callableMock.call()).thenReturn("result");
        delegate.returnObject = "result";

        Future<Object> future = delegate.call();
        Object result = future.get(1, TimeUnit.SECONDS);
        assertEquals("result", result);
        Mockito.verify(latch).await(1, TimeUnit.SECONDS);
        Mockito.verifyNoMoreInteractions(callableMock);
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callableMock = Mockito.mock(Callable.class);
        Mockito.when(callableMock.call()).thenReturn("result");

        try {
            delegate.get(2, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            Mockito.verifyNoMoreInteractions(callableMock);
        }
    }

    @Test
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        Callable<Object> callableMock = Mockito.mock(Callable.class);
        Mockito.when(callableMock.call()).thenThrow(new RuntimeException("test exception"));

        try {
            delegate.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals("test exception", e.getCause().getMessage());
            Mockito.verifyNoMoreInteractions(callableMock);
        }
    }

    @Test
    public void testIsDone() {
        assertTrue(delegate.isDone());
    }
}