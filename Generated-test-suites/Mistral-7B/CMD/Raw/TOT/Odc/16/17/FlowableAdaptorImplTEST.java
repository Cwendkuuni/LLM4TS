package com.oceanbase.odc.service.flow.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.specific.FlowableAdaptor;
import com.oceanbase.odc.metadb.flow.specific.FlowableAdaptorImpl;
import com.oceanbase.odc.metadb.flow.specific.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.specific.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.specific.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.auth.Principal;

public class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.getPrincipal()).thenReturn(new AuthenticationFacadeMock());
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(eq(flowInstanceId), eq(processInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1))
                .updateProcessDefinitionIdById(eq(flowInstanceId), eq(processDefinitionId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("serviceTaskActivityId");

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals("serviceTaskActivityId", result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.SERVICE_TASK),
                eq(activityId), eq(flowInstanceId))).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL_TASK),
                eq(activityId), eq(flowInstanceId))).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result =
                flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(FlowNodeType.APPROVAL_TASK), eq(name),
                eq(flowInstanceId))).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result =
                flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.GATEWAY), eq(activityId),
                eq(flowInstanceId))).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long id = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL_TASK;
        String activityId = "activityId";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(activityId);
        nodeInstanceEntity.setInstanceId(id);
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setFlowInstanceId(id);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setName("name");

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, nodeType,
                flowableElementType)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        FlowableElement flowableElement = new FlowableElement();
        flowableElement.setType(flowableElementType);
        flowableElement.setActivityId(activityId);
        flowableElement.setName("name");

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(id, nodeType), flowableElement);

        verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
    }
}