import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
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
        MockitoAnnotations.initMocks(this);

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
        String processInstanceId = "testProcessInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetProcessInstanceId_InvalidFlowInstance() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        flowableAdaptor.setProcessInstanceId(flowInstanceId, "testProcessInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = 1L;
        Long anotherFlowInstanceId = 2L;

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(List.of(new FlowInstanceEntity(flowInstanceId), new FlowInstanceEntity(anotherFlowInstanceId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assert.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_EmptyList() {
        String processInstanceId = "testProcessInstanceId";

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFlowInstanceIdByProcessInstanceId_MultipleFlowInstances() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId1 = 1L;
        Long flowInstanceId2 = 2L;

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(List.of(new FlowInstanceEntity(flowInstanceId1), new FlowInstanceEntity(flowInstanceId2)));

        flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetProcessDefinitionId_InvalidFlowInstance() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, "testProcessDefinitionId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;
        Long anotherFlowInstanceId = 2L;

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(List.of(new FlowInstanceEntity(flowInstanceId), new FlowInstanceEntity(anotherFlowInstanceId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Assert.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_EmptyList() {
        String processDefinitionId = "testProcessDefinitionId";

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Assert.assertFalse(result.isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFlowInstanceIdByProcessDefinitionId_MultipleFlowInstances() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId1 = 1L;
        Long flowInstanceId2 = 2L;

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(List.of(new FlowInstanceEntity(flowInstanceId1), new FlowInstanceEntity(flowInstanceId2)));

        flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(processInstanceId)));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assert.assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId_EmptyOptional() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(new NodeInstanceEntity(instanceId, instanceType, flowableElementType, "testName"));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("testName", result.get(0).getName());
    }

    @Test
    public void testGetFlowableElementByType_EmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(Collections.emptyList());

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Long taskInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity(taskInstanceId, flowInstanceId, activityId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertEquals(taskInstanceId, result.get().getId());
    }

    @Test
    public void testGetTaskInstanceByActivityId_EmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(approvalInstanceId, flowInstanceId, activityId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertEquals(approvalInstanceId, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId_EmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(approvalInstanceId, flowInstanceId, name);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assert.assertEquals(approvalInstanceId, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByName_EmptyOptional() {
        String name = "testName";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Long gatewayInstanceId = 1L;
        GateWayInstanceEntity gatewayInstanceEntity = new GateWayInstanceEntity(gatewayInstanceId, flowInstanceId, activityId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gatewayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertEquals(gatewayInstanceId, result.get().getId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId_EmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        String name = "testName";
        String activityId = "testActivityId";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(instanceId, nodeType, flowableElementType, name, activityId);

        when(nodeInstanceRepository.save(Mockito.any(NodeInstanceEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(instanceId, nodeType),
                new FlowableElement(activityId, name, flowableElementType));

        Mockito.verify(nodeInstanceRepository).save(Mockito.any(NodeInstanceEntity.class));
    }
}