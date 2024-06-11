
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        Mockito.when(execution.getProcessDefinitionId()).thenReturn("testDefinitionId");
        delegate.initTargetTaskInstanceId(execution);

        assertEquals("testDefinitionId", delegate.getFlowInstanceId().toString());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };
        delegate.setActivityId("testActivityId");
        delegate.setTargetTaskInstanceId(123L);

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(123L);

        delegate.bindToFlowTaskInstance(taskInstance);

        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };
        delegate.setFlowInstanceId(456L);

        assertEquals(456L, delegate.getFlowInstanceId().longValue());

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Mock the repository and verify the update
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.run(execution);

        assertEquals("Test Callable", delegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.run(execution);

        assertEquals("Test Callable", delegate.get(1, TimeUnit.SECONDS));
    }
}
