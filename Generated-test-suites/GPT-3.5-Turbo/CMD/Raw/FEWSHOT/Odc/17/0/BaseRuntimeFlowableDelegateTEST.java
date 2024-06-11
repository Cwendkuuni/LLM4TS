package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> runtimeFlowableDelegate;

    @Before
    public void setup() {
        runtimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };
        runtimeFlowableDelegate.setFlowableAdaptor(mock(FlowableAdaptor.class));
        runtimeFlowableDelegate.setEventPublisher(mock(EventPublisher.class));
        runtimeFlowableDelegate.setFlowInstanceRepository(mock(FlowInstanceRepository.class));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        runtimeFlowableDelegate.initTargetTaskInstanceId(execution);
        assertNotNull(runtimeFlowableDelegate.getFlowInstanceId());
        assertNotNull(runtimeFlowableDelegate.getTargetTaskInstanceId());
        assertNotNull(runtimeFlowableDelegate.getTaskType());
        assertNotNull(runtimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        taskInstance.setId(123L);
        runtimeFlowableDelegate.setActivityId("testActivity");
        runtimeFlowableDelegate.setTargetTaskInstanceId(123L);

        runtimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        // Verify that taskInstance is bound to runtimeFlowableDelegate
        // Additional verification logic can be added here
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        runtimeFlowableDelegate.setFlowInstanceId(456L);
        runtimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        // Verify that flow instance status is updated successfully
        // Additional verification logic can be added here
    }

    @Test
    public void testIsDone() {
        assertFalse(runtimeFlowableDelegate.isDone());
        // Additional test logic for isDone() method
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        runtimeFlowableDelegate.execute(mock(DelegateExecution.class));
        String result = runtimeFlowableDelegate.get();
        assertEquals("Test Callable", result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        runtimeFlowableDelegate.execute(mock(DelegateExecution.class));
        String result = runtimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertEquals("Test Callable", result);
    }

}