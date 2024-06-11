package com.oceanbase.odc.service.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get().getProcessInstanceId()).isEqualTo(processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> flowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertThat(flowInstanceId.isPresent()).isTrue();
        assertThat(flowInstanceId.get()).isEqualTo(1L);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get().getProcessDefinitionId()).isEqualTo(processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> flowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertThat(flowInstanceId.isPresent()).isTrue();
        assertThat(flowInstanceId.get()).isEqualTo(1L);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> processInstanceIdOptional = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertThat(processInstanceIdOptional.isPresent()).isTrue();
        assertThat(processInstanceIdOptional.get()).isEqualTo(processInstanceId);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType elementType = FlowableElementType.START_EVENT;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setInstanceId(instanceId);
        serviceTaskInstanceEntity.setActivityId("activityId");
        serviceTaskInstanceEntity.setInstanceType(nodeType);
        serviceTaskInstanceEntity.setFlowableElementType(elementType);

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setInstanceId(instanceId);
        userTaskInstanceEntity.setActivityId("activityId");
        userTaskInstanceEntity.setInstanceType(nodeType);
        userTaskInstanceEntity.setFlowableElementType(elementType);

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setInstanceId(instanceId);
        gateWayInstanceEntity.setActivityId("activityId");
        gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);
        gateWayInstanceEntity.setFlowableElementType(FlowableElementType.EXCLUSIVE_GATEWAY);

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(serviceTaskInstanceEntity, userTaskInstanceEntity, gateWayInstanceEntity);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, elementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> flowableElements = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType, elementType);
        assertThat(flowableElements.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setInstanceId(1L);
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> flowTaskInstance = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertThat(flowTaskInstance.isPresent()).isTrue();
        assertThat(flowTaskInstance.get()).isInstanceOf(FlowTaskInstance.class);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setInstanceId(1L);
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertThat(flowApprovalInstance.isPresent()).isTrue();
        assertThat(flowApprovalInstance.get()).isInstanceOf(FlowApprovalInstance.class);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setInstanceId(1L);
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertThat(flowApprovalInstance.isPresent()).isTrue();
        assertThat(flowApprovalInstance.get()).isInstanceOf(FlowApprovalInstance.class);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setInstanceId(1L);
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> flowGatewayInstance = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertThat(flowGatewayInstance.isPresent()).isTrue();
        assertThat(flowGatewayInstance.get()).isInstanceOf(FlowGatewayInstance.class);
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, flowableElementType))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        flowableAdaptorImpl.setFlowableElement(new FlowNodeInstance(instanceId, nodeType), flowableElement);

        NodeInstanceEntity updatedNodeInstanceEntity = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, flowableElementType).get();
        assertThat(updatedNodeInstanceEntity.getName()).isEqualTo(flowableElement.getName());
        assertThat(updatedNodeInstanceEntity.getActivityId()).isEqualTo(flowableElement.getActivityId());
    }
}