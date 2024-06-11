import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Test
    public void testCreate() {
        // Test the create method of FlowTaskInstance
        // Create a mock ServiceTaskInstanceEntity
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        // Create a mock RuntimeDelegateMapper
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        // Create a mock FlowableAdaptor
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        // Create a mock EventPublisher
        EventPublisher eventPublisher = mock(EventPublisher.class);
        // Create a mock TaskService
        TaskService taskService = mock(TaskService.class);
        // Create a mock NodeInstanceEntityRepository
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        // Create a mock SequenceInstanceRepository
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        // Create a mock ServiceTaskInstanceRepository
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        // Create a FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher,
            taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        // Test the create method
        flowTaskInstance.create();
        
        // Verify the interaction with serviceTaskRepository
        verify(serviceTaskRepository, times(1)).save(entity);
        // Verify the id, createTime, and updateTime fields of the FlowTaskInstance object
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        // Test the update method of FlowTaskInstance
        // Create a mock ServiceTaskInstanceEntity
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        // Create a mock RuntimeDelegateMapper
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        // Create a mock FlowableAdaptor
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        // Create a mock EventPublisher
        EventPublisher eventPublisher = mock(EventPublisher.class);
        // Create a mock TaskService
        TaskService taskService = mock(TaskService.class);
        // Create a mock NodeInstanceEntityRepository
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        // Create a mock SequenceInstanceRepository
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        // Create a mock ServiceTaskInstanceRepository
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        
        // Create a FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher,
            taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        // Test the update method
        flowTaskInstance.update();
        
        // Verify the interaction with serviceTaskRepository
        verify(serviceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testCancel() {
        // Test the cancel method of FlowTaskInstance
        // Create a mock BaseRuntimeFlowableDelegate
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        // Create a FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        // Set the targetTaskHandle
        flowTaskInstance.setTargetTaskHandle(delegate);
        
        // Test the cancel method with mayInterruptIfRunning set to false
        flowTaskInstance.cancel(false);
        
        // Verify the interaction with the BaseRuntimeFlowableDelegate
        verify(delegate, times(1)).cancel(false);
    }
}