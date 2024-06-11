import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    public void setUp() {
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
        flowableAdaptor.setNodeInstanceEntityRepository(nodeInstanceRepository);
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

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";

        when(flowInstanceRepository.findAll(any())).thenReturn(new ArrayList<>() {
            {
                add(new FlowInstanceEntity());
            }
        });

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(flowInstanceId.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def123";

        when(flowInstanceRepository.findAll(any())).thenReturn(new ArrayList<>() {
            {
                add(new FlowInstanceEntity());
            }
        });

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(flowInstanceId.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> processInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(processInstanceId.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT;

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(new ArrayList<>() {
            {
                add(new NodeInstanceEntity());
            }
        });

        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertFalse(flowableElements.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "task123";
        Long flowInstanceId = 1L;

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(), any(), any()))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> taskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(taskInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "approval456";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(), any(), any()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(approvalInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approvalTask";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(), any(), any()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(approvalInstance.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "gateway123";
        Long flowInstanceId = 1L;

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(), any(), any()))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> gatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(gatewayInstance.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activity123", "elementName", FlowableElementType.TASK);

        when(nodeInstance.getId()).thenReturn(1L);
        when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.TASK);
        when(nodeInstance.getFlowInstanceId()).thenReturn(1L);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository).save(any());
    }
}