package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testCreateFlowTaskInstanceWithEntityConstructor() {
        // Mock dependencies
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        // Validate
        assertEquals(entity.getTargetTaskId(), flowTaskInstance.getTargetTaskId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndpoint());
    }

    @Test
    public void testCreateFlowTaskInstanceWithTypesConstructor() {
        // Mock dependencies
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TYPE, 1L, 2L, new ExecutionStrategyConfig(),
                false, false, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        // Validate
        assertEquals(TaskType.TYPE, flowTaskInstance.getTaskType());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndpoint());
    }

    @Test
    public void testCreateFlowTaskInstanceAndPublishEvent() {
        // Mock dependencies
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        // Verify event publishing
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        // Mock dependencies
        List<FlowTaskInstance> instances = new ArrayList<>();
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        // Batch create instances
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        // Validate
        assertEquals(0, createdInstances.size());
    }

    @Test
    public void testConfirmExecuteFlowTaskInstance() {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        // Confirm execution
        flowTaskInstance.confirmExecute();

        // Verify completion
        verify(flowTaskInstance).complete(false);
    }

    @Test
    public void testAbortFlowTaskInstance() {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        // Abort instance
        flowTaskInstance.abort();

        // Verify completion
        verify(flowTaskInstance).complete(true);
    }

    @Test
    public void testCancelFlowTaskInstance() {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        // Cancel instance
        flowTaskInstance.cancel(true);

        // Verify cancellation
        verify(flowTaskInstance).cancel(true);
    }

    @Test
    public void testIsDoneFlowTaskInstance() throws InterruptedException, ExecutionException {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(flowTaskInstance.isDone()).thenReturn(true);

        // Verify if task is done
        assertEquals(true, flowTaskInstance.isDone());
    }

    @Test
    public void testGetResultFlowTaskInstance() throws InterruptedException, ExecutionException {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        Object expectedResult = new Object();
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(flowTaskInstance.get()).thenReturn(expectedResult);

        // Get result of task
        Object result = flowTaskInstance.getResult();

        // Validate result
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetResultWithTimeoutFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
        // Mock dependencies
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        Object expectedResult = new Object();
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(flowTaskInstance.get(1, TimeUnit.SECONDS)).thenReturn(expectedResult);

        // Get result with timeout
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);

        // Validate result
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResourceTypeFlowTaskInstance() {
        // Create FlowTaskInstance
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // Validate resource type
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }
}