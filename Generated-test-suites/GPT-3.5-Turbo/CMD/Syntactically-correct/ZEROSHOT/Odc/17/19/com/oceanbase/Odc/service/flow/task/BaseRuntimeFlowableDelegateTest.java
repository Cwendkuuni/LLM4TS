package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Object expected = new Object();
        baseRuntimeFlowableDelegate.returnObject = expected;
        assertEquals(expected, baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expected = new Object();
        baseRuntimeFlowableDelegate.returnObject = expected;
        assertEquals(expected, baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.thrown = new Exception("Test Exception");
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.done = false;
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getActivityId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(Mockito.mock(FlowInstanceRepository.class));
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);
    }

    @Test
    public void testInitCallable() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);
        assertNotNull(callable);
    }

}