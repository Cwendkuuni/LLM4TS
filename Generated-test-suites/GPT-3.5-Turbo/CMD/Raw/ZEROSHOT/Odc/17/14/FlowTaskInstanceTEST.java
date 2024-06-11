import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Test
    public void testCreate() {
        // Mock dependencies
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        // Test create method
        flowTaskInstance.create();

        // Verify if the serviceTaskRepository.save method is called
        verify(serviceTaskRepository, times(1)).save(entity);
    }

    @Test
    public void testUpdate() {
        // Mock dependencies
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        // Create FlowTaskInstance object
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        // Test update method
        flowTaskInstance.update();

        // Verify if the serviceTaskRepository.update method is called
        verify(serviceTaskRepository, times(1)).update(entity);
    }

    // Add more test methods for other functions in FlowTaskInstance class

}