
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private RuntimeDelegateMapper mapper;
    
    @Mock
    private FlowableAdaptor flowableAdaptor;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    
    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Test
    public void testCreateFlowTaskInstance() {
        // Given
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        boolean startEndpoint = true;
        boolean endEndPoint = false;
        TaskType taskType = TaskType.USER_TASK;
        
        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId,
                                                                strategyConfig, startEndpoint, endEndPoint,
                                                                mapper, flowableAdaptor, eventPublisher,
                                                                taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository);
        
        // Then
        assertNotNull(flowTaskInstance);
        assertEquals(taskType, flowTaskInstance.getTaskType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testBatchCreateFlowTaskInstance() {
        // Given
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(new FlowTaskInstance());
        
        // When
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        
        // Then
        assertNotNull(createdInstances);
        assertEquals(1, createdInstances.size());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // When
        boolean present = flowTaskInstance.isPresentOnThisMachine();
        
        // Then
        assertTrue(!present);
    }

    @Test
    public void testBindServiceTask() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // When
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<Object>());
        
        // Then
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testIsDone() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<Object>());
        
        // When
        boolean done = flowTaskInstance.isDone();
        
        // Then
        assertTrue(done);
    }

    @Test
    public void testResourceType() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        // When
        String resourceType = flowTaskInstance.resourceType();
        
        // Then
        assertNotNull(resourceType);
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), resourceType);
    }
}
