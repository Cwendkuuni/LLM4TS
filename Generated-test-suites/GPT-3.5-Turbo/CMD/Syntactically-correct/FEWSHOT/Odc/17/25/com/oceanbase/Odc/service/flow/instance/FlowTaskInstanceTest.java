package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testConstructorWithEntity() {
        // Create a mock ServiceTaskInstanceEntity for testing
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(100L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(200L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.EMAIL);
        entity.setTargetTaskId(2L);
        entity.setWaitExecExpireIntervalSeconds(30);
        entity.setExecutionTime(10);

        // Create a mock RuntimeDelegateMapper for testing
        RuntimeDelegateMapper mapper = new RuntimeDelegateMapper();

        // Create a mock FlowableAdaptor for testing
        FlowableAdaptor adaptor = new FlowableAdaptor();

        // Create a mock EventPublisher for testing
        EventPublisher publisher = new EventPublisher();

        // Create a mock TaskService for testing
        TaskService taskService = new TaskService();

        // Create a mock NodeInstanceEntityRepository for testing
        NodeInstanceEntityRepository nodeRepository = new NodeInstanceEntityRepository();

        // Create a mock SequenceInstanceRepository for testing
        SequenceInstanceRepository sequenceRepository = new SequenceInstanceRepository();

        // Create a mock ServiceTaskInstanceRepository for testing
        ServiceTaskInstanceRepository serviceTaskRepository = new ServiceTaskInstanceRepository();

        // Create a FlowTaskInstance using the constructor with entity
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, mapper, adaptor, publisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        // Assert the properties of the FlowTaskInstance object
        assertEquals(1L, flowTaskInstance.getId());
        assertEquals(100L, flowTaskInstance.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(200L, flowTaskInstance.getFlowInstanceId());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertNotNull(flowTaskInstance.getStrategyConfig());
        assertEquals(2L, flowTaskInstance.getTargetTaskId().longValue());
        assertEquals(TaskType.EMAIL, flowTaskInstance.getTaskType());
    }

    @Test
    public void testConstructorWithTaskType() {
        // Create a mock RuntimeDelegateMapper for testing
        RuntimeDelegateMapper mapper = new RuntimeDelegateMapper();

        // Create a mock FlowableAdaptor for testing
        FlowableAdaptor adaptor = new FlowableAdaptor();

        // Create a mock EventPublisher for testing
        EventPublisher publisher = new EventPublisher();

        // Create a mock TaskService for testing
        TaskService taskService = new TaskService();

        // Create a mock NodeInstanceEntityRepository for testing
        NodeInstanceEntityRepository nodeRepository = new NodeInstanceEntityRepository();

        // Create a mock SequenceInstanceRepository for testing
        SequenceInstanceRepository sequenceRepository = new SequenceInstanceRepository();

        // Create a mock ServiceTaskInstanceRepository for testing
        ServiceTaskInstanceRepository serviceTaskRepository = new ServiceTaskInstanceRepository();

        // Create an ExecutionStrategyConfig for testing
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 20, 5);

        // Create a FlowTaskInstance using the constructor with task type
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.HTTP, 150L, 250L, strategyConfig, true, false, mapper, adaptor, publisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        // Assert the properties of the FlowTaskInstance object
        assertEquals(150L, flowTaskInstance.getOrganizationId());
        assertEquals(250L, flowTaskInstance.getFlowInstanceId());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertNotNull(flowTaskInstance.getStrategyConfig());
        assertEquals(TaskType.HTTP, flowTaskInstance.getTaskType());
    }

    // Add more test cases to cover other methods in FlowTaskInstance class

}