package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnitRunner;

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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
@MockitoJUnitRunner
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
    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        doNothing().when(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId))
                .thenReturn(Optional.ofNullable(new FlowInstanceEntity()).map(entity -> {
                    entity.setProcessInstanceId(processInstanceId);
                    return entity;
                }));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType elementType = FlowableElementType.START;
        List<NodeInstanceEntity> nodes = Arrays.asList(
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(nodeType).setFlowNodeType(FlowNodeType.START)
                        .setActivityId("activityId1").setName("name1").setFlowableElementType(elementType),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(nodeType).setFlowNodeType(FlowNodeType.END)
                        .setActivityId("activityId2").setName("name2").setFlowableElementType(elementType),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(nodeType).setFlowNodeType(FlowNodeType.USER_TASK)
                        .setActivityId("activityId3").setName("name3").setFlowableElementType(FlowableElementType.END)
        );

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, elementType))
                .thenReturn(nodes.stream().filter(node -> Objects.equals(node.getFlowableElementType(), elementType))
                        .collect(Collectors.toList()));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, elementType);

        assertEquals(1, result.size());
        FlowableElement element = result.get(0);
        assertEquals("activityId1", element.getActivityId());
        assertEquals("name1", element.getName());
        assertEquals(elementType, element.getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertNotNull(flowTaskInstance.getRuntimeDelegateMapper());
        assertNotNull(flowTaskInstance.getFlowService());
        assertNotNull(flowTaskInstance.getEventPublisher());
        assertNotNull(flowTaskInstance.getTaskService());
        assertNotNull(flowTaskInstance.getNodeRepository());
        assertNotNull(flowTaskInstance.getSequenceRepository());
        assertNotNull(flowTaskInstance.getServiceTaskInstanceRepository());
        assertEquals("activityId", flowTaskInstance.getActivityId());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getEventPublisher());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
        assertEquals("activityId", flowApprovalInstance.getActivityId());
        assertEquals(FlowNodeType.APPROVAL_TASK, flowApprovalInstance.getNodeType());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setName(name)
                .setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getEventPublisher());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
        assertEquals(name, flowApprovalInstance.getName());
        assertEquals(FlowNodeType.APPROVAL_TASK, flowApprovalInstance.getNodeType());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertNotNull(flowGatewayInstance.getFlowService());
        assertNotNull(flowGatewayInstance.getNodeRepository());
        assertNotNull(flowGatewayInstance.getSequenceRepository());
        assertNotNull(flowGatewayInstance.getGateWayInstanceRepository());
        assertEquals("activityId", flowGatewayInstance.getActivityId());
        assertEquals(FlowNodeType.GATEWAY, flowGatewayInstance.getNodeType());
        assertEquals(flowInstanceId, flowGatewayInstance.getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType elementType = FlowableElementType.START;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", elementType);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId).setInstanceType(nodeType).setFlowNodeType(FlowNodeType.START)
                .setActivityId("activityId").setName("name").setFlowableElementType(elementType);

        doNothing().when(nodeInstanceRepository).save(nodeInstanceEntity);

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(instanceId, nodeType), flowableElement);

        verify(nodeInstanceRepository, times(1)).save(nodeInstanceEntity);
    }
}