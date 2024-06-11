import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private FlowInstanceRepository flowInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Before
    public void setup() {
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setNodeRepository(nodeRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);

        when(flowInstanceRepository.findAll(any())).thenReturn(List.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def123";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);

        when(flowInstanceRepository.findAll(any())).thenReturn(List.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> entityList = new ArrayList<>();
        entityList.add(new NodeInstanceEntity("123", "Task1", FlowableElementType.TASK));
        entityList.add(new NodeInstanceEntity("456", "Task2", FlowableElementType.TASK));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(entityList);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(entityList.size(), result.size());
    }

    // Add more test methods for other methods in FlowableAdaptorImpl

}