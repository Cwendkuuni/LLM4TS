package com.oceanbase.odc.service.flow;

import static com.oceanbase.odc.service.flow.FlowableAdaptor.FlowableElementType.APPROVAL_GATEWAY;
import static com.oceanbase.odc.service.flow.FlowableAdaptor.FlowableElementType.APPROVAL_TASK;
import static com.oceanbase.odc.service.flow.FlowableAdaptor.FlowableElementType.GATEWAY;
import static com.oceanbase.odc.service.flow.FlowableAdaptor.FlowableElementType.SERVICE_TASK;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private Long flowInstanceId = 1L;
    private Long processInstanceId = 2L;
    private Long processDefinitionId = 3L;
    private String activityId = "activityId";
    private String processInstanceId2 = "processInstanceId2";
    private String processDefinitionId2 = "processDefinitionId2";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, () -> Optional.of(new FlowInstanceEntity()));
        assertEquals(1, flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId).get().get().intValue());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        assertEquals(Optional.of(flowInstanceId), flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, () -> Optional.of(new FlowInstanceEntity()));
        assertEquals(1, flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId).get().get().intValue());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        assertEquals(Optional.of(flowInstanceId), flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        assertEquals(Optional.of(processInstanceId), flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testGetFlowableElementByType() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.SERVICE_TASK);
        nodeInstanceEntity1.setActivityId("activityId1");
        nodeInstanceEntity1.setFlowableElementType(SERVICE_TASK);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(1L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.APPROVAL_TASK);
        nodeInstanceEntity2.setActivityId("activityId2");
        nodeInstanceEntity2.setFlowableElementType(APPROVAL_TASK);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SERVICE_TASK, SERVICE_TASK))
                .thenReturn(List.of(nodeInstanceEntity1));
        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.APPROVAL_TASK, APPROVAL_TASK))
                .thenReturn(List.of(nodeInstanceEntity2));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, SERVICE_TASK);
        assertEquals(1, result.size());
        assertEquals("activityId1", result.get(0).getActivityId());

        result = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, APPROVAL_TASK);
        assertEquals(1, result.size());
        assertEquals("activityId2", result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName("name");
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "name", flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName("name", flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals("name", flowApprovalInstance.getName());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertEquals(flowInstanceId, flowGatewayInstance.getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        nodeInstanceEntity.setActivityId("activityId");
        nodeInstanceEntity.setFlowableElementType(APPROVAL_TASK);

        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(nodeInstanceEntity, flowableAdaptor, taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);

        NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();
        newNodeInstanceEntity.setInstanceId(2L);
        newNodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_GATEWAY);
        newNodeInstanceEntity.setActivityId("activityId2");
        newNodeInstanceEntity.setFlowableElementType(APPROVAL_GATEWAY);

        FlowApprovalInstance newFlowApprovalInstance = new FlowApprovalInstance(newNodeInstanceEntity, flowableAdaptor, taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);

        Mockito.doAnswer((InvocationOnMock invocation) -> {
            NodeInstanceEntity entity = (NodeInstanceEntity) invocation.getArguments()[0];
            entity.setName(newFlowApprovalInstance.getName());
            entity.setActivityId(newFlowApprovalInstance.getActivityId());
            entity.setFlowableElementType(newFlowApprovalInstance.getFlowableElementType());
            return null;
        }).when(nodeInstanceRepository).save(any(NodeInstanceEntity.class));

        flowableAdaptor.setFlowableElement(flowApprovalInstance, newFlowApprovalInstance);

        Mockito.verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
    }
}