import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor.eventPublisher = mock(EventPublisher.class);
        flowableAdaptor.nodeRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.sequenceRepository = mock(SequenceInstanceRepository.class);
        flowableAdaptor.flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        flowableAdaptor.taskService = mock(TaskService.class);
        flowableAdaptor.formService = mock(FormService.class);
        flowableAdaptor.gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
    }

    @Test
    public void testSetProcessInstanceId() {
        // Create mock data
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity());
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        // Test the method
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify the interactions
        verify(flowableAdaptor.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Create mock data
        String processInstanceId = "12345";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);
        Specification<FlowInstanceEntity> specification = Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        when(flowableAdaptor.flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        // Test the method
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().longValue());
    }

    // Add more test methods for other functions in FlowableAdaptorImpl

}