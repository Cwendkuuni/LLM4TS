package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

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

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @org.junit.Before
    public void setup() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = mock(RetryExecutor.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "success";
                    }
                };
            }
        };

        testee.init(eventPublisher);
        testee.setFlowableAdaptor(flowableAdaptor);
        testee.setEventPublisher(eventPublisher);
        testee.setFlowInstanceRepository(flowInstanceRepository);
        testee.setRetryExecutor(retryExecutor);
        testee.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        testee.setActiveTaskStatisticsListener(activeTaskStatisticsListener);

        given(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).answers(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        given(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).willReturn(mock(FlowTaskInstance.class));
        given(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).willAnswer(invocation -> 1L);

        given(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).willAnswer(invocation -> true);
    }

    @org.junit.Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willReturn("success");

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        testee.run(execution);

        ArgumentCaptor<FlowStatus> flowStatusCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        verify(flowInstanceRepository).updateStatusById(anyLong(), flowStatusCaptor.capture());
        assertEquals(FlowStatus.STARTED, flowStatusCaptor.getValue());
    }

    @org.junit.Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willThrow(new RuntimeException("Exception in callable"));

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        testee.run(execution);

        verify(testee).throwException(any(RuntimeException.class));
    }

    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willReturn("success");

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        Future<String> future = testee.getFuture();
        assertEquals("success", future.get());
    }

    @org.junit.Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willReturn("success");

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        Thread.currentThread().interrupt();
        testee.get();
    }

    @org.junit.Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willThrow(new RuntimeException("Exception in callable"));

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        Exception exception = new RuntimeException("Mock exception");
        given(callable.call()).willThrow(exception);

        Future<String> future = testee.getFuture();
        future.get(1, TimeUnit.SECONDS);
    }

    @org.junit.Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        given(callable.call()).willReturn("success");

        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };

        testee.get(1, TimeUnit.MILLISECONDS);
    }

    @org.junit.Test
    public void testIsDone() throws Exception {
        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>();
        assertFalse(testee.isDone());

        testee.done = true;
        assertTrue(testee.isDone());
    }

    @org.junit.Test
    public void testGetFuture() {
        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>();
        assertEquals(testee, testee.getFuture().getDelegate());
    }

    @org.junit.Test
    public void testGetFlowInstanceId() {
        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void run(DelegateExecution execution) throws Exception {
                initTargetTaskInstanceId(execution);
            }
        };
        assertEquals(1L, testee.getFlowInstanceId());
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void run(DelegateExecution execution) throws Exception {
                initTargetTaskInstanceId(execution);
                bindToFlowTaskInstance(flowTaskInstance);
            }
        };

        verify(flowTaskInstance).bindServiceTask(testee);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        BaseRuntimeFlowableDelegate<String> testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void updateFlowInstanceStatus(FlowStatus flowStatus) {
                super.updateFlowInstanceStatus(flowStatus);
            }
        };

        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }
}