package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class FlowTaskInstanceTest {

    @Test
    public void testConstructor() {
        // Test constructor with all parameters
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint, endEndPoint, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(flowTaskInstance);
        
        // Test constructor with entity and converters
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testCreateAndBatchCreate() {
        // Test create method
        flowTaskInstance.create();
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());

        // Test batchCreate method
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        // Test update method
        flowTaskInstance.setStatus(FlowNodeStatus.RUNNING);
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.RUNNING, flowTaskInstance.getStatus());
    }

    @Test
    public void testBindServiceTask() {
        // Test bindServiceTask method
        flowTaskInstance.bindServiceTask(targetTask);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecuteAndAbort() {
        // Test confirmExecute method
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());

        // Test abort method
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        // Test cancel method
        flowTaskInstance.bindServiceTask(targetTask);
        flowTaskInstance.cancel(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() {
        // Test getResult method
        flowTaskInstance.bindServiceTask(targetTask);
        Object result = flowTaskInstance.getResult();
        assertNotNull(result);
    }

    @Test
    public void testGetResultWithTimeout() {
        // Test getResult with timeout method
        flowTaskInstance.bindServiceTask(targetTask);
        Object result = flowTaskInstance.getResult(10, TimeUnit.SECONDS);
        assertNotNull(result);
    }
}