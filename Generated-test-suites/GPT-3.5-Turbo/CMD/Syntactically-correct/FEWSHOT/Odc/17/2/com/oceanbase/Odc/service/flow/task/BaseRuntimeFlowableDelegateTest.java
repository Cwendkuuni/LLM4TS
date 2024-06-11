package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.initTargetTaskInstanceId(execution);
        assertNotNull(delegate.getFlowInstanceId());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstanceImpl();
        taskInstance.setId(1L);
        taskInstance.setActivityId("activity1");
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.setActivityId("activity1");
        delegate.setTargetTaskInstanceId(1L);
        delegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.COMPLETED;
        FlowInstanceRepository flowInstanceRepository = new FlowInstanceRepositoryImpl();
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        delegate.setFlowInstanceId(1L);
        delegate.setRetryExecutor(new RetryExecutor());
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.updateFlowInstanceStatus(flowStatus);
        assertEquals(flowStatus, flowInstanceRepository.getStatusById(1L));
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        Future<Object> future = Mockito.mock(Future.class);
        Mockito.when(future.get()).thenReturn("result");
        delegate.setLatch(new CountDownLatch(1));
        delegate.setReturnObject("result");
        delegate.get();
        assertEquals("result", delegate.getReturnObject());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        Future<Object> future = Mockito.mock(Future.class);
        Mockito.when(future.get()).thenReturn(null);
        delegate.setLatch(new CountDownLatch(1));
        delegate.get(100, TimeUnit.MILLISECONDS);
        assertNull(delegate.getReturnObject());
    }
}