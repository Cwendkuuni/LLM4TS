package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;
    @Mock
    private TaskService taskService;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                () -> Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);
    }

    @Test
    void testGetFlowInstanceIdByProcessInstanceId() {
        Long processInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(result.isPresent(), true);
        assertEquals(result.get(), 1L);
    }

    @Test
    void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                () -> Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(affectRows, 1);
    }

    @Test
    void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(result.isPresent(), true);
        assertEquals(result.get(), 1L);
    }

    @Test
    void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId("processInstanceId");

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(result.isPresent(), true);
        assertEquals(result.get(), "processInstanceId");
    }

    @Test
    void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.OUTPUT).setActivityId("activityId1")
                        .setName("name1"),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.INPUT).setActivityId("activityId2")
                        .setName("name2"));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities.stream().filter(n -> n.getFlowableElementType()
                        .equals(flowableElementType) || n.getActivityId().equals("activityId2")).collect(Collectors.toList()));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(new FlowableElement("activityId2", "name2", flowableElementType)));
    }

    @Test
    void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<ServiceTaskInstanceEntity> optionalServiceTaskInstanceEntity = Optional.of(serviceTaskInstanceEntity);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(optionalServiceTaskInstanceEntity);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertInstanceOf(FlowTaskInstance.class, result.get());
    }

    @Test
    void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(optionalUserTaskInstanceEntity);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceEntity.setActivityId("activityId");

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(optionalUserTaskInstanceEntity);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        Optional<GateWayInstanceEntity> optionalGateWayInstanceEntity = Optional.of(gateWayInstanceEntity);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(optionalGateWayInstanceEntity);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertInstanceOf(FlowGatewayInstance.class, result.get());
    }

    @Test
    void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setFlowInstanceId(1L);
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.USER_TASK);

        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.INPUT);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.INPUT))
                .thenReturn(Collections.emptyList());

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity()
                .setInstanceId(nodeInstance.getId()).setInstanceType(nodeInstance.getNodeType())
                .setFlowableElementType(FlowableElementType.INPUT).setActivityId(flowableElement.getActivityId())
                .setName(flowableElement.getName()));

        verify(nodeInstanceRepository).saveAll(nodeInstanceEntities);
    }
}