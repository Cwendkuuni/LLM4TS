import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateMapper;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private ServiceTaskInstanceEntity entity;

    @Before
    public void setUp() {
        entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(1L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.USER_DEFINED);
        entity.setTargetTaskId(2L);
        entity.setWaitExecExpireIntervalSeconds(30);
        entity.setExecutionTime(100);

        flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        when(flowTaskInstance.mapToTaskEntity(flowTaskInstance)).thenReturn(entity);
        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(entity));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(1, createdInstances.size());
        assertEquals(entity.getId(), createdInstances.get(0).getId());
        assertEquals(entity.getCreateTime(), createdInstances.get(0).getCreateTime());
        assertEquals(entity.getUpdateTime(), createdInstances.get(0).getUpdateTime());
    }

    // Add more test cases for each method with typical use cases, edge cases, and error scenarios

}