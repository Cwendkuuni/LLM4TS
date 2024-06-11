package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.junit.Test;

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

    @Test
    public void testRun() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        };

        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        delegate.init(Mockito.mock(EventPublisher.class));
        delegate.run(execution);

        verify(execution).getCurrentActivityId();
        verify(execution).dealloc();
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        };

        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        Callable<String> callable = delegate.initCallable(execution);

        verify(execution).getCurrentActivityId();
        verify(execution, times(1)).getCurrentActivityId();
        verify(delegate).eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class));
        assertNotNull(callable);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new RuntimeException();
            }
        };

        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        delegate.init(Mockito.mock(EventPublisher.class));
        delegate.run(execution);

        verify(execution).getCurrentActivityId();
        verify(execution).dealloc();
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);

        thrown = delegate.thrown;
        assertTrue(thrown instanceof ServiceTaskError);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "test";
                    }
                };
            }
        };

        delegate.init(Mockito.mock(EventPublisher.class));

        CountDownLatch latch = new CountDownLatch(1);

        String result = delegate.get();

        assertEquals("test", result);
        verify(delegate.latch).await();
        verify(delegate.latch, times(1)).await();
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(1500);
                        return "test";
                    }
                };
            }
        };

        delegate.init(Mockito.mock(EventPublisher.class));

        CountDownLatch latch = new CountDownLatch(1);

        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            fail("Timeout exception should not be thrown");
        }

        verify(delegate.latch).await(eq(1), eq(TimeUnit.SECONDS));
        verify(delegate.latch, times(1)).await(eq(1), eq(TimeUnit.SECONDS));
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(2000);
                        return "test";
                    }
                };
            }
        };

        delegate.init(Mockito.mock(EventPublisher.class));

        CountDownLatch latch = new CountDownLatch(1);

        delegate.get(1, TimeUnit.SECONDS);

        verify(delegate.latch).await(eq(1), eq(TimeUnit.SECONDS));
        verify(delegate.latch, times(1)).await(eq(1), eq(TimeUnit.SECONDS));
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);

        thrown = delegate.thrown;
        assertTrue(thrown instanceof TimeoutException);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "test";
                    }
                };
            }
        };

        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
        verify(taskInstance, times(1)).bindServiceTask(eq(delegate));
        verifyZeroInteractions(delegate.eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(delegate.flowInstanceRepository);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "test";
                    }
                };
            }
        };

        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);

        delegate.init(eventPublisher);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(eq(delegate.flowInstanceId), eq(FlowStatus.COMPLETED));
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(delegate.flowInstanceId), eq(FlowStatus.COMPLETED));
        verifyZeroInteractions(eventPublisher);
        verifyZeroInteractions(delegate.flowableAdaptor);
        verifyZeroInteractions(retryExecutor);
    }
}