package com.oceanbase.odc.service.flow.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.iam.auth.AuthenticationFacade;
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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;
import com.oceanbase.odc.service.iam.auth.Principal;
import com.oceanbase.odc.service.iam.auth.User;

/**
 * Test class for {@link FlowableAdaptorImpl}.
 */
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
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
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private Principal principal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.getAuthenticatedUser()).thenReturn(new User());
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(eq(flowInstanceId), eq(processInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Long flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).get();

        assertEquals(flowInstanceId, flowInstanceEntity.getId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Long flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get();

        assertEquals(flowInstanceId, flowInstanceEntity.getId());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(eq(flowInstanceId))).thenReturn(Optional.of(new FlowInstanceEntity()));

        String actualProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get();

        assertEquals(processInstanceId, actualProcessInstanceId);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;

        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(eq(instanceId), eq(nodeType),
                eq(flowableElementType))).thenReturn(nodeInstanceEntities);

        List<FlowableElement> actualFlowableElements = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(1, actualFlowableElements.size());
        assertEquals(nodeInstanceEntities.get(0).getName(), actualFlowableElements.get(0).getName());
        assertEquals(nodeInstanceEntities.get(0).getActivityId(), actualFlowableElements.get(0).getActivityId());
        assertEquals(FlowableElementType.INPUT, actualFlowableElements.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.SERVICE_TASK), eq(activityId),
                eq(flowInstanceId))).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> actualFlowTaskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(actualFlowTaskInstance.isPresent());
        assertEquals(activityId, actualFlowTaskInstance.get().getActivityId());
        assertEquals(flowInstanceId, actualFlowTaskInstance.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL_TASK), eq(activityId),
                eq(flowInstanceId))).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> actualFlowApprovalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(actualFlowApprovalInstance.isPresent());
        assertEquals(activityId, actualFlowApprovalInstance.get().getActivityId());
        assertEquals(flowInstanceId, actualFlowApprovalInstance.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(FlowNodeType.APPROVAL_TASK), eq(name),
                eq(flowInstanceId))).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> actualFlowApprovalInstance = flowableAdaptor.getApprovalInstanceByName(name,
                flowInstanceId);

        assertTrue(actualFlowApprovalInstance.isPresent());
        assertEquals(name, actualFlowApprovalInstance.get().getName());
        assertEquals(flowInstanceId, actualFlowApprovalInstance.get().getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.GATEWAY), eq(activityId),
                eq(flowInstanceId))).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> actualFlowGatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(actualFlowGatewayInstance.isPresent());
        assertEquals(activityId, actualFlowGatewayInstance.get().getActivityId());
        assertEquals(flowInstanceId, actualFlowGatewayInstance.get().getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        String name = "name";
        String activityId = "activityId";

        FlowableElement flowableElement = new FlowableElement(activityId, name, flowableElementType);

        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        when(nodeInstance.getId()).thenReturn(nodeInstanceId);
        when(nodeInstance.getNodeType()).thenReturn(nodeType);
        when(nodeInstance.getFlowInstanceId()).thenReturn(1L);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository).save(argThat(node -> equals(node.getActivityId(), activityId) &&
                equals(node.getName(), name) && equals(node.getFlowableElementType(), flowableElementType) &&
                equals(node.getInstanceType(), nodeType) && equals(node.getInstanceId(), nodeInstanceId) &&
                equals(node.getFlowInstanceId(), 1L)));
    }
}