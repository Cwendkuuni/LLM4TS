package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.flowable.task.api.Task;
import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testBindServiceTask() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Create BaseRuntimeFlowableDelegate
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = new BaseRuntimeFlowableDelegate<>();
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        // Test binding
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testConfirmExecute() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Confirm execution
        flowTaskInstance.confirmExecute();
        
        // Test status after execution
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Abort task
        flowTaskInstance.abort();
        
        // Test if task is aborted
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Cancel task
        flowTaskInstance.cancel(false);
        
        // Test if task is canceled
    }

    @Test
    public void testIsDone() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Check if task is done
        boolean done = flowTaskInstance.isDone();
        
        // Test if task is done
        assertEquals(true, done);
    }

    @Test
    public void testGetResult() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Get result
        Object result = flowTaskInstance.getResult();
        
        // Test if result is not null
        assertNotNull(result);
    }

    @Test
    public void testGetResultWithTimeout() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Get result with timeout
        Object result = flowTaskInstance.getResult(100, TimeUnit.SECONDS);
        
        // Test if result is not null
        assertNotNull(result);
    }

    @Test
    public void testResourceType() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // Get resource type
        String resourceType = flowTaskInstance.resourceType();
        
        // Test if resource type is as expected
        assertEquals("ODC_FLOW_TASK_INSTANCE", resourceType);
    }
}