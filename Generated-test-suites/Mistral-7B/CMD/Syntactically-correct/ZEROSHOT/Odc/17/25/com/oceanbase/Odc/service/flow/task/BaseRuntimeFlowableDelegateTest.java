package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableTestUtil;
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

    @org.junit.Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.Test
    public void testRun_shouldCallInitTaskInstanceId_whenInitializeCallable() {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        // When
        delegate.run(execution);

        // Then
        ArgumentCaptor<String> processDefinitionIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionIdCaptor.capture());
        String processDefinitionId = processDefinitionIdCaptor.getValue();

        ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(processDefinitionId, delegate.getFlowInstanceId());

        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.RUNNING);
    }

    @org.junit.Test
    public void testGet_shouldReturnResult_whenTaskIsDone() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.initCallable(execution) = callable;

        String result = "result";
        when(callable.call()).thenReturn(result);

        // When
        Future<String> future = delegate;
        String resultFromFuture = future.get();

        // Then
        assertEquals(result, resultFromFuture);
    }

    @org.junit.Test
    public void testGet_shouldThrowExecutionException_whenTaskIsNotDone() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.initCallable(execution) = callable;

        String result = "result";
        when(callable.call()).thenThrow(new Exception("test exception"));

        // When - Then
        try {
            Future<String> future = delegate;
            future.get();
            fail("Expected ExecutionException to be thrown");
        } catch (ExecutionException e) {
            assertEquals("test exception", e.getCause().getMessage());
        }
    }

    @org.junit.Test
    public void testGet_shouldThrowInterruptedException_whenThreadIsInterrupted() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.initCallable(execution) = callable;

        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;

        // When - Then
        Thread thread = new Thread(() -> {
            try {
                delegate.get();
            } catch (InterruptedException e) {
                fail("Expected InterruptedException to not be thrown");
            }
        });

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // ignore
        }

        assertTrue(latch.getCount() == 0);
    }

    @org.junit.Test
    public void testGet_shouldThrowTimeoutException_whenTimeoutIsReached() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.initCallable(execution) = callable;

        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;

        // When - Then
        Thread thread = new Thread(() -> {
            try {
                delegate.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                fail("Expected InterruptedException to not be thrown");
            } catch (TimeoutException e) {
                // expected
            }
        });

        thread.join();
        assertTrue(latch.getCount() == 0);
    }

    @org.junit.Test
    public void testPreHandle_shouldNotThrowException() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        // When
        delegate.preHandle(execution);

        // Then
        verifyNoMoreInteractions(execution);
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance_shouldBindTaskToInstance() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        // When
        delegate.bindToFlowTaskInstance(taskInstance);

        // Then
        verify(taskInstance).bindServiceTask(delegate);
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance_shouldNotBindTaskToInstance_whenTaskInstanceIdDoesNotMatch() {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        // When
        delegate.bindToFlowTaskInstance(taskInstance);

        // Then
        verify(taskInstance, never()).bindServiceTask(delegate);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus_shouldUpdateFlowInstanceStatus() throws Exception {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();

        // When
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
    }

    @org.junit.Test
    public void testInitCallable_shouldInitializeCallable() {
        // Given
        BaseRuntimeFlowableDelegate<String> delegate = new MyBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        // When
        Callable<String> callable = delegate.initCallable(execution);

        // Then
        assertNotNull(callable);
    }

    private static class MyBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected void preHandle(DelegateExecution execution) {
            // no-op
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return Mockito.mock(Callable.class);
        }

    }

}