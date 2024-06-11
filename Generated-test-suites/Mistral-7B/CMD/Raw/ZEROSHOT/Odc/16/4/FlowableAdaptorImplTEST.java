package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;

@Slf4j
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

    @Autowired
    private FlowableAdaptorImpl flowableAdaptor;

    @InjectMocks
    private FlowableAdaptor flowableAdaptorImpl;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findAll(specification).stream().findFirst();
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, optional::isPresent);
        assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "ProcessInstanceId", processInstanceId,
                result::isPresent);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));
        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findAll(specification).stream().findFirst();
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, optional::isPresent);
        assertEquals(processDefinitionId, optional.get().getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "ProcessDefinitionId", processDefinitionId,
                result::isPresent);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, result::isPresent);
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(), new NodeInstanceEntity());
        nodeInstanceEntities.get(0).setInstanceId(instanceId);
        nodeInstanceEntities.get(0).setInstanceType(instanceType);
        nodeInstanceEntities.get(0).setFlowableElementType(flowableElementType);
        nodeInstanceEntities.get(0).setActivityId("testActivityId1");
        nodeInstanceEntities.get(0).setName("testName1");

        nodeInstanceRepository.findAll(any(Specification.class)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        FlowableElement flowableElement = result.get(0);
        assertEquals("testActivityId1", flowableElement.getActivityId());
        assertEquals("testName1", flowableElement.getName());
        assertEquals(FlowableElementType.INPUT, flowableElement.getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
                flowInstanceId);

        PreConditions.validExists(FlowNodeType.SERVICE_TASK, "ActivityId", activityId, result::isPresent);
        FlowTaskInstance flowTaskInstance = result.get();
        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertEquals(1, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        PreConditions.validExists(FlowNodeType.APPROVAL_TASK, "ActivityId", activityId, result::isPresent);
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertEquals(1, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name,
                flowInstanceId);

        PreConditions.validExists(FlowNodeType.APPROVAL_TASK, "Name", name, result::isPresent);
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(name, flowApprovalInstance.getName());
        assertEquals(1, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        PreConditions.validExists(FlowNodeType.GATEWAY, "ActivityId", activityId, result::isPresent);
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertEquals(1, flowGatewayInstance.getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName",  flowableElementType);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
                nodeType, flowableElementType)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeType), flowableElement);

        NodeInstanceEntity result = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                nodeInstanceId, nodeType, flowableElementType).get(0);
        assertEquals(flowableElement.getActivityId(), result.getActivityId());
        assertEquals(flowableElement.getName(), result.getName());
        assertEquals(flowableElementType, result.getFlowableElementType());
    }
}