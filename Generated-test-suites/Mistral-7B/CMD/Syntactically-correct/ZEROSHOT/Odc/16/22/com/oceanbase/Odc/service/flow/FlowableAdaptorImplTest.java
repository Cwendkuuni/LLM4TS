package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementTypeTestUtils;
import com.oceanbase.odc.service.flow.service.FlowServiceTestUtils;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
@SkipAuthorize
public class FlowableAdaptorImplTest {

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

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(eq(flowInstanceId),
                eq(processInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId("processDefinitionId");
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(
                List.of(flowInstanceEntity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(eq(flowInstanceId),
                eq(processDefinitionId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(
                List.of(flowInstanceEntity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId("processInstanceId");
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        assertTrue(result.isPresent());
        assertEquals("processInstanceId", result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementTypeTestUtils.getServiceTaskElementType();
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(new NodeInstanceEntity(), new NodeInstanceEntity());
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("activityId");
        nodeInstanceEntity.setName("name");
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowInstanceId(instanceId);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(1, result.size());
        assertEquals("name", result.get(0).getName());
        assertEquals("activityId", result.get(0).getActivityId());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertEquals(serviceTaskInstanceEntity.getId(), flowTaskInstance.getId());
        assertEquals(serviceTaskInstanceEntity.getActivityId(), flowTaskInstance.getActivityId());
        assertEquals(serviceTaskInstanceEntity.getName(), flowTaskInstance.getName());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(userTaskInstanceEntity.getId(), flowApprovalInstance.getId());
        assertEquals(userTaskInstanceEntity.getActivityId(), flowApprovalInstance.getActivityId());
        assertEquals(userTaskInstanceEntity.getName(), flowApprovalInstance.getName());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(userTaskInstanceEntity.getId(), flowApprovalInstance.getId());
        assertEquals(userTaskInstanceEntity.getActivityId(), flowApprovalInstance.getActivityId());
        assertEquals(userTaskInstanceEntity.getName(), flowApprovalInstance.getName());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertEquals(gateWayInstanceEntity.getId(), flowGatewayInstance.getId());
        assertEquals(gateWayInstanceEntity.getActivityId(), flowGatewayInstance.getActivityId());
        assertEquals(gateWayInstanceEntity.getName(), flowGatewayInstance.getName());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("name", "activityId", FlowableElementType.APPROVAL);
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        nodeInstanceEntity.setFlowInstanceId(1L);
        PreConditions.checkArgument(nodeInstanceEntity.getFlowableElementType() == null,
                "NodeInstanceEntity should not have a flowableElementType when testing setFlowableElement");
        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeInstanceEntity,
                new FlowServiceTestUtils().getFlowService()), flowableElement);
        Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeInstanceEntity.getInstanceType(),
                        FlowableElementType.APPROVAL);
        assertTrue(optionalNodeInstanceEntity.isPresent());
        NodeInstanceEntity nodeInstance = optionalNodeInstanceEntity.get();
        assertEquals(flowableElement.getName(), nodeInstance.getName());
        assertEquals(flowableElement.getActivityId(), nodeInstance.getActivityId());
        assertEquals(flowableElement.getType(), nodeInstance.getFlowableElementType());

        NodeInstanceEntity existingNodeInstance = new NodeInstanceEntity();
        existingNodeInstance.setInstanceId(nodeInstanceId);
        existingNodeInstance.setInstanceType(nodeInstanceEntity.getInstanceType());
        existingNodeInstance.setFlowableElementType(FlowableElementType.APPROVAL);
        existingNodeInstance.setName("existingName");
        existingNodeInstance.setActivityId("existingActivityId");
        existingNodeInstance.setFlowInstanceId(nodeInstanceEntity.getFlowInstanceId());
        nodeInstanceRepository.save(existingNodeInstance);
        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeInstanceEntity,
                new FlowServiceTestUtils().getFlowService()), flowableElement);
        Optional<NodeInstanceEntity> optionalExistingNodeInstance = nodeInstanceRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeInstanceEntity.getInstanceType(),
                        FlowableElementType.APPROVAL);
        assertTrue(optionalExistingNodeInstance.isPresent());
        NodeInstanceEntity updatedNodeInstance = optionalExistingNodeInstance.get();
        assertEquals(flowableElement.getName(), updatedNodeInstance.getName());
        assertEquals(flowableElement.getActivityId(), updatedNodeInstance.getActivityId());
        assertEquals(flowableElement.getType(), updatedNodeInstance.getFlowableElementType());
    }
}