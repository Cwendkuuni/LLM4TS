import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance createFlowTaskInstance() {
        // Mock dependencies
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        return new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = createFlowTaskInstance();
        instance.create();
        // Assert the creation process
        assertNotNull(instance.getId());
        assertNotNull(instance.getCreateTime());
        assertNotNull(instance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = createFlowTaskInstance();
        instance.update();
        // Assert the update process
        // Need to find a way to verify the affectRows value
    }

    @Test
    public void testBatchCreate() {
        // Mock instances list
        List<FlowTaskInstance> instances = new ArrayList<>();
        // Add instances to the list
        FlowTaskInstance instance1 = createFlowTaskInstance();
        instances.add(instance1);
        FlowTaskInstance instance2 = createFlowTaskInstance();
        instances.add(instance2);
        // Call batchCreate method
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances);
        // Assert the batch creation process
        assertEquals(createdInstances.size(), instances.size());
        for (int i = 0; i < createdInstances.size(); i++) {
            assertNotNull(createdInstances.get(i).getId());
            assertNotNull(createdInstances.get(i).getCreateTime());
            assertNotNull(createdInstances.get(i).getUpdateTime());
        }
    }

    // Add more test methods for other functionalities in FlowTaskInstance class

}