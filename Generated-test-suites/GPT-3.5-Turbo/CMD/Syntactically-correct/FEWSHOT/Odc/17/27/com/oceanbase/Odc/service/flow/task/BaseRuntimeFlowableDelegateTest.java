package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        };
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        // Verify the update status logic
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(execution);
        String result = baseRuntimeFlowableDelegate.get();
        assertEquals("Test Result", result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        String result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertEquals("Test Result", result);
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.run(execution);
        boolean done = baseRuntimeFlowableDelegate.isDone();
        assertEquals(true, done);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        // Verify the execution logic
    }
}