package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.EventPublisherImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowInstanceRepositoryImplTest;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.TestBaseFlowableDelegate;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.TestRetryExecutor;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setUp() {
        eventPublisher = new EventPublisherImpl();
        flowInstanceRepository = new FlowInstanceRepositoryImpl();
        retryExecutor = new TestRetryExecutor();
        flowableAdaptor = new FlowableAdaptorImpl(eventPublisher, flowInstanceRepository, retryExecutor);
        delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
    }

    @Test
    public void shouldInitializeDelegateWithNullFlowableAdaptor() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        assertNull(delegate.getFlowableAdaptor());
    }

    @Test
    public void shouldInitializeDelegateWithEmptyEventPublisher() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        assertNotNull(delegate.getEventPublisher());
        verify(eventPublisher).addEventListener(Mockito.isInstance(delegate.getActiveTaskStatisticsListener()));
        verify(eventPublisher).addEventListener(Mockito.isInstance(delegate.getTaskInstanceCreatedlistener()));
    }

    @Test
    public void shouldInitializeDelegateWithNullFlowInstanceRepository() {
        FlowInstanceRepository flowInstanceRepository = null;
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        assertNull(delegate.getFlowInstanceRepository());
    }

    @Test
    public void shouldInitializeDelegateWithEmptyFlowInstanceRepository() {
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        assertNotNull(delegate.getFlowInstanceRepository());
        verify(flowInstanceRepository).save(Mockito.isInstance(Mockito.any(FlowTaskInstance.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldInitializeDelegateWithNullRetryExecutor() {
        RetryExecutor retryExecutor = null;
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        delegate.setRetryExecutor(retryExecutor);
    }

    @Test
    public void shouldInitializeDelegateWithEmptyRetryExecutor() {
        RetryExecutor retryExecutor = new RetryExecutor() {
            @Override
            public <T> Future<T> submit(Callable<T> callable) {
                return null;
            }
        };
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        delegate.setRetryExecutor(retryExecutor);
        assertNotNull(delegate.getRetryExecutor());
    }

    @Test
    public void shouldInitializeDelegateWithNullFlowableAdaptorAndThrowException() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        try {
            delegate.init(eventPublisher);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("FlowableAdaptor cannot be null"));
        }
    }

    @Test
    public void shouldInitializeDelegateWithEventPublisherAndThrowException() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        try {
            delegate.init(null);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("EventPublisher cannot be null"));
        }
    }

    @Test
    public void shouldInitializeDelegateWithFlowInstanceRepositoryAndThrowException() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        try {
            delegate.init(eventPublisher);
            delegate.setFlowInstanceRepository(null);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("FlowInstanceRepository cannot be null"));
        }
    }

    @Test
    public void shouldInitializeDelegateWithRetryExecutorAndThrowException() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        try {
            delegate.init(eventPublisher);
            delegate.setRetryExecutor(null);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("RetryExecutor cannot be null"));
        }
    }

    @Test
    public void shouldCallPreHandleMethodBeforeCallableExecution() {
        ArgumentCaptor<DelegateExecution> executionArgumentCaptor = ArgumentCaptor.forClass(DelegateExecution.class);
        delegate.initCallable(executionArgumentCaptor.capture(Mockito.mock(DelegateExecution.class)));
        delegate.preHandle(executionArgumentCaptor.getValue());
        delegate.call();
        verify(executionArgumentCaptor.getValue()).setVariableLocal("RETURN_OBJECT", Mockito.any());
    }

    @Test
    public void shouldReturnResultWhenCallableExecutesSuccessfully() {
        String result = "Test result";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn(result);
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        assertEquals(result, delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void shouldThrowInterruptedExceptionWhenGetCalledAndNotDone() {
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.latch.countDown();
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void shouldThrowExecutionExceptionWhenCallableThrowsException() {
        String exceptionMessage = "Test exception message";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException(exceptionMessage));
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        verify(delegate).updateFlowInstanceStatus(Mockito.eq(FlowStatus.FAILED));
        assertEquals(exceptionMessage, delegate.getThrown().getMessage());
    }

    @Test(expected = TimeoutException.class)
    public void shouldThrowTimeoutExceptionWhenGetCalledAndTimeout() {
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.latch.await(1, TimeUnit.MILLISECONDS);
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void shouldUpdateFlowInstanceStatusWhenCallableThrowsException() {
        String exceptionMessage = "Test exception message";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException(exceptionMessage));
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        verify(delegate).updateFlowInstanceStatus(Mockito.eq(FlowStatus.FAILED));
    }

    @Test
    public void shouldBindTaskInstanceToDelegate() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(Mockito.isInstance(delegate));
    }

    @Test
    public void shouldThrowServiceTaskErrorWhenInitializationFails() {
        Exception initializationException = new RuntimeException("Test initialization exception");
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        try {
            delegate.call();
        } catch (ServiceTaskError e) {
            assertEquals(initializationException.getMessage(), e.getCause().getMessage());
        }
    }

    @Test
    public void shouldUpdateFlowInstanceStatusWithSuccessWhenTaskExecutesSuccessfully() {
        String result = "Test result";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn(result);
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        verify(delegate).updateFlowInstanceStatus(Mockito.eq(FlowStatus.COMPLETED));
    }

    @Test
    public void shouldUpdateFlowInstanceStatusWithErrorWhenTaskExecutesWithException() {
        String exceptionMessage = "Test exception message";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException(exceptionMessage));
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        verify(delegate).updateFlowInstanceStatus(Mockito.eq(FlowStatus.FAILED));
    }

    @Test
    public void shouldReleaseListenersWhenExecutionFinishes() {
        delegate.execute(Mockito.mock(Execution.class));
        verify(eventPublisher).removeEventListener(delegate.getActiveTaskStatisticsListener());
        verify(eventPublisher).removeEventListener(delegate.getTaskInstanceCreatedlistener());
    }

    @Test
    public void shouldNotReturnResultWhenTaskIsNotDone() {
        assertFalse(delegate.isDone());
    }

    @Test
    public void shouldReturnResultWhenTaskIsDone() {
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void shouldThrowExceptionWhenCalledBeforeExecution() {
        try {
            delegate.get();
        } catch (InterruptedException e) {
            fail("Unexpected InterruptedException");
        } catch (ExecutionException e) {
            fail("Unexpected ExecutionException");
        } catch (TimeoutException e) {
            fail("Unexpected TimeoutException");
        }

        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail("Unexpected InterruptedException");
        } catch (ExecutionException e) {
            fail("Unexpected ExecutionException");
        } catch (TimeoutException e) {
            fail("Unexpected TimeoutException");
        } catch (ServiceTaskError e) {
            fail("Unexpected ServiceTaskError");
        }
    }

    @Test
    public void shouldReturnNullWhenTaskIsNull() {
        delegate.bindToFlowTaskInstance(null);
        assertNull(delegate.getTaskInstance());
    }

    @Test
    public void shouldUpdateTaskInstanceStatusWithSuccessWhenTaskExecutesSuccessfully() {
        String result = "Test result";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn(result);
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getStatus()).thenReturn(FlowStatus.IN_PROGRESS);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).updateStatus(Mockito.eq(FlowStatus.COMPLETED));
    }

    @Test
    public void shouldUpdateTaskInstanceStatusWithErrorWhenTaskExecutesWithException() {
        String exceptionMessage = "Test exception message";
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException(exceptionMessage));
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        delegate.initCallable(Mockito.mock(DelegateExecution.class), callable);
        delegate.call();
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getStatus()).thenReturn(FlowStatus.IN_PROGRESS);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).updateStatus(Mockito.eq(FlowStatus.FAILED));
    }

    @Test
    public void shouldCallPreHandleMethodTwiceWhenDelegateIsReused() {
        ArgumentCaptor<DelegateExecution> executionArgumentCaptor = ArgumentCaptor.forClass(DelegateExecution.class);
        delegate.initCallable(executionArgumentCaptor.capture(Mockito.mock(DelegateExecution.class)));
        delegate.preHandle(executionArgumentCaptor.getValue());
        delegate.initCallable(executionArgumentCaptor.capture(Mockito.mock(DelegateExecution.class)));
        delegate.preHandle(executionArgumentCaptor.getValue());
        verify(executionArgumentCaptor.getValue(), Mockito.times(2)).setVariableLocal("RETURN_OBJECT", Mockito.any());
    }
}