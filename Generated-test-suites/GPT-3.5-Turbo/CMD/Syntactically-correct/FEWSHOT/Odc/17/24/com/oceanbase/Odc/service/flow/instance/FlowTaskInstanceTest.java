package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testGetCoreFlowableElementType() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = new ServiceTaskInstanceRepository();
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertNotNull(createdInstances);
        assertNotEquals(0, createdInstances.size());
    }

    @Test
    public void testCreate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.update();
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testDoDelete() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.delete(1L);
        assertNull(flowTaskInstance.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.alloc();
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.bindServiceTask(target);
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.confirmExecute();
        assertNotEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.abort();
        assertNotEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.alloc();
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(true));
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.alloc();
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::isDone);
    }

    @Test
    public void testGetResult() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.alloc();
        assertThrows(UnsupportedOperationException.class, flowTaskInstance::getResult);
    }

    @Test
    public void testGetResultWithTimeout() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.alloc();
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        RuntimeDelegateMapper convertor = new RuntimeDelegateMapper();
        FlowableAdaptor flowableAdapter = new FlowableAdaptor();
        EventPublisher eventPublisher = new EventPublisher();
        TaskService taskService = new TaskService();
        NodeInstanceEntityRepository nodeRepository = new NodeInstanceEntityRepository();
        SequenceInstanceRepository sequenceRepository = new SequenceInstanceRepository();
        ServiceTaskInstanceRepository serviceTaskRepository = new ServiceTaskInstanceRepository();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_A, 1L, 1L, new ExecutionStrategyConfig(), true, false, convertor, flowableAdapter, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }

}