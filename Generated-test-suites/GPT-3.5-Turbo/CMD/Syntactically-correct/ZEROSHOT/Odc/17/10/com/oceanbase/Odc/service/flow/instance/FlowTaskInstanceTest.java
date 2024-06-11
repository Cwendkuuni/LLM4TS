import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Test
    public void testBindServiceTask() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        // Exercise
        flowTaskInstance.bindServiceTask(target);

        // Verify
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        flowTaskInstance.confirmExecute();

        // Verify
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        flowTaskInstance.abort();

        // Verify
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        flowTaskInstance.cancel(false);

        // Verify
        verify(target).cancel(false);
    }

    @Test
    public void testIsDone() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        boolean result = false;
        try {
            result = flowTaskInstance.isDone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify
        verify(target).isDone();
        assertTrue(result);
    }

    @Test
    public void testGetResult() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        Object result = null;
        try {
            result = flowTaskInstance.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify
        verify(target).get();
        assertNull(result);
    }

    @Test
    public void testGetResultWithTimeout() {
        // Setup
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(123L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(456L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SERVICE);
        entity.setTargetTaskId(789L);
        entity.setWaitExecExpireIntervalSeconds(60);
        entity.setExecutionTime(120);

        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Exercise
        Object result = null;
        try {
            result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify
        verify(target).get(1, TimeUnit.SECONDS);
        assertNull(result);
    }

}