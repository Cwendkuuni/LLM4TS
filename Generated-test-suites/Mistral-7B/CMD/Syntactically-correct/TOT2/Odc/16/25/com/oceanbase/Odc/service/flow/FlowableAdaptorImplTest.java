package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementImpl;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setup() {
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        this.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        this.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        this.userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        this.taskService = mock(TaskService.class);
        this.formService = mock(FormService.class);
        this.authenticationFacade = mock(AuthenticationFacade.class);
        this.odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);
        this.flowableAdaptor = new FlowableAdaptorImpl(flowInstanceRepository, gateWayInstanceRepository,
                nodeInstanceRepository, sequenceRepository, serviceTaskInstanceRepository, userTaskInstanceRepository,
                taskService, formService, authenticationFacade, odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceId() {
        flowableAdaptor.setProcessInstanceId(null, "processInstanceId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullProcessInstanceId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdWithEmptyResult() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullFlowInstanceId() {
        flowableAdaptor.setProcessDefinitionId(null, "processDefinitionId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullProcessDefinitionId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionIdWithEmptyResult() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceIdWithEmptyResult() {
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(
                new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstances);
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof FlowableElementImpl);
        assertTrue(result.get(1) instanceof FlowableElementImpl);
    }

    @Test
    public void testGetFlowableElementByTypeWithEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(Collections.emptyList());
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetTaskInstanceByActivityIdWithEmptyResult() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityIdWithEmptyResult() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByNameWithEmptyResult() {
        String name = "name";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityIdWithEmptyResult() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElementImpl("activityId", "name", FlowableElementType.END_EVENT);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        verify(nodeInstanceRepository).save(nodeInstance);
    }

    @Test
    public void testSetFlowableElementWithNullNodeInstance() {
        FlowableElement flowableElement = new FlowableElementImpl("activityId", "name", FlowableElementType.END_EVENT);
        flowableAdaptor.setFlowableElement(null, flowableElement);
        verify(nodeInstanceRepository, never()).save(any());
    }

    @Test
    public void testSetFlowableElementWithNullFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        flowableAdaptor.setFlowableElement(nodeInstance, null);
        verify(nodeInstanceRepository, never()).save(any());
    }
}