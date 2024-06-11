import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @BeforeAll
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        Mockito.reset(nodeInstanceRepository, serviceTaskInstanceRepository, eventPublisher, nodeRepository,
                sequenceRepository, flowInstanceRepository, userTaskInstanceRepository, taskService, formService,
                authenticationFacade, gateWayInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        Mockito.doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);

        Executable executable = () -> flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        executable.invoke();

        ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> processInstanceIdCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceIdCaptor.capture(),
                processInstanceIdCaptor.capture());

        assertEquals(flowInstanceId, flowInstanceIdCaptor.getValue());
        assertEquals(processInstanceId, processInstanceIdCaptor.getValue());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = 1L;

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of(new FlowInstanceEntity(1L, processInstanceId, null, null, null)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(List.of(new FlowInstanceEntity(1L, null, processDefinitionId, null, null)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(1L, processInstanceId, null, null, null)));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity(1L, NodeType.SERVICE_TASK, FlowableElementType.ACTIVITY, instanceId),
                new NodeInstanceEntity(2L, NodeType.APPROVAL_TASK, FlowableElementType.ACTIVITY, instanceId),
                new NodeInstanceEntity(3L, NodeType.GATEWAY, FlowableElementType.ACTIVITY, instanceId)
        );

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(FlowableElementType.ACTIVITY, result.get(0).getType());
        assertEquals("testActivityId1", result.get(0).getActivityId());
        assertEquals("testActivityName1", result.get(0).getName());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity(1L, flowInstanceId, activityId, null, null);

        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> result.get().getServiceTaskInstance());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, flowInstanceId, activityId, null, null, null, null, null);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> result.get().getUserTaskInstance());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testApprovalName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, flowInstanceId, null, name, null, null, null, null);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> result.get().getUserTaskInstance());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity(1L, flowInstanceId, activityId, null, null);

        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> result.get().getGateWayInstance());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        Long nodeInstanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testActivityName", flowableElementType);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setNodeType(NodeType.SERVICE_TASK);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, NodeType.SERVICE_TASK, flowableElementType))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, instanceId, NodeType.SERVICE_TASK), flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }
}