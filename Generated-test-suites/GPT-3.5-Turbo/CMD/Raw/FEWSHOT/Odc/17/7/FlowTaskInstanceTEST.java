package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testConstructorWithEntity() {
        // Create FlowTaskInstance with ServiceTaskInstanceEntity
        // Test if all fields are correctly initialized
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(200L);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setTargetTaskId(2L);
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.TYPE_A);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertNotNull(flowTaskInstance.getId());
        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(100L, flowTaskInstance.getOrganizationId().longValue());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(200L, flowTaskInstance.getFlowInstanceId().longValue());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertEquals(2L, flowTaskInstance.getTargetTaskId().longValue());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(TaskType.TYPE_A, flowTaskInstance.getTaskType());

    }

    @Test
    public void testConstructorWithTaskType() {
        // Create FlowTaskInstance with TaskType
        // Test if all fields are correctly initialized
        Long organizationId = 100L;
        Long flowInstanceId = 200L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        boolean startEndpoint = true;
        boolean endEndpoint = false;
        TaskType taskType = TaskType.TYPE_A;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint, endEndpoint, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId().longValue());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(taskType, flowTaskInstance.getTaskType());
    }

    @Test
    public void testBatchCreate() {
        // Test batchCreate method
        // Verify that batchCreate returns correct instances
        List<FlowTaskInstance> instances = new ArrayList<>();
        // Add instances to the list

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertEquals(instances.size(), result.size());
        for (int i = 0; i < instances.size(); i++) {
            assertEquals(instances.get(i).getId(), result.get(i).getId());
            assertNotNull(result.get(i).getCreateTime());
            assertNotNull(result.get(i).getUpdateTime());
        }
    }

    // Add more test cases for other methods in FlowTaskInstance class

}