package com.oceanbase.odc.service.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;
    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepository; // Assuming you have a test implementation or mock
    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository; // Assuming you have a test implementation or mock
    @Autowired
    private EventPublisher eventPublisher; // Assuming you have a test implementation or mock
    @Autowired
    private NodeInstanceEntityRepository nodeRepository; // Assuming you have a test implementation or mock
    @Autowired
    private SequenceInstanceRepository sequenceRepository; // Assuming you have a test implementation or mock
    @Autowired
    private FlowInstanceRepository flowInstanceRepository; // Assuming you have a test implementation or mock
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository; // Assuming you have a test implementation or mock
    @Autowired
    private TaskService taskService; // Assuming you have a test implementation or mock
    @Autowired
    private FormService formService; // Assuming you have a test implementation or mock
    @Autowired
    private AuthenticationFacade authenticationFacade; // Assuming you have a test implementation or mock
    @Autowired
    private GateWayInstanceRepository gateWayInstanceRepository; // Assuming you have a test implementation or mock

    @Test
    void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        MockitoAnnotations.initMocks(this);
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = 1L;
        MockitoAnnotations.initMocks(this);
        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId, processInstanceId, null)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;
        MockitoAnnotations.initMocks(this);
        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId, null, processDefinitionId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        MockitoAnnotations.initMocks(this);
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(flowInstanceId, processInstanceId, null)));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<NodeInstanceEntity> nodes = List.of(
                new NodeInstanceEntity(instanceId, instanceType, flowableElementType, null, null, null),
                new NodeInstanceEntity(instanceId, instanceType, FlowableElementType.OUTPUT, null, null, null));
        MockitoAnnotations.initMocks(this);
        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodes.stream().filter(n -> n.getFlowableElementType() == flowableElementType).collect(Collectors.toList()));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(new ServiceTaskInstanceEntity());
        MockitoAnnotations.initMocks(this);
        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(serviceTaskInstanceEntityOptional);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(serviceTaskInstanceEntityOptional.get(), result.get());
    }

    @Test
    void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        MockitoAnnotations.initMocks(this);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(userTaskInstanceEntityOptional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(userTaskInstanceEntityOptional.get(), result.get());
    }

    @Test
    void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        MockitoAnnotations.initMocks(this);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(userTaskInstanceEntityOptional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assertions.assertEquals(userTaskInstanceEntityOptional.get(), result.get());
    }

    @Test
    void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(new GateWayInstanceEntity());
        MockitoAnnotations.initMocks(this);
        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(gateWayInstanceEntityOptional);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(gateWayInstanceEntityOptional.get(), result.get());
    }

    @Test
    void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.INPUT);
        MockitoAnnotations.initMocks(this);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1))
                .save(Mockito.argThat(e -> e.getActivityId().equals(flowableElement.getActivityId()) &&
                        e.getName().equals(flowableElement.getName()) && e.getFlowableElementType() == flowableElement.getType()));
    }

    @Test
    void testExceptionWhenSettingNullFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();

        assertThrows(NullPointerException.class, () -> flowableAdaptor.setFlowableElement(nodeInstance, null));
    }

    @Test
    void testExceptionWhenSettingDuplicateFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.INPUT);
        MockitoAnnotations.initMocks(this);
        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        assertThrows(IllegalStateException.class, () -> flowableAdaptor.setFlowableElement(nodeInstance, flowableElement));
    }
}