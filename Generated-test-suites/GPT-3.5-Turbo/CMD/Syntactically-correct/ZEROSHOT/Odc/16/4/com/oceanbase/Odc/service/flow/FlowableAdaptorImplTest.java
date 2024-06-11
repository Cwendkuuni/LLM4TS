package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;

public class FlowableAdaptorImplTest {
    
    private FlowableAdaptorImpl flowableAdaptor;
    
    private FlowInstanceRepository flowInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private GateWayInstanceRepository gateWayInstanceRepository;
    
    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setNodeInstanceEntityRepository(nodeInstanceEntityRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setNodeRepository(nodeRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }
    
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        assertEquals(processInstanceId, flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process1";
        Long flowInstanceId = 1L;
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        
        List<FlowInstanceEntity> entityList = new ArrayList<>();
        entityList.add(flowInstanceEntity);
        
        when(flowInstanceRepository.findAll(any())).thenReturn(entityList);
        
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "definition1";
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);
        
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        
        assertEquals(processDefinitionId, flowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "definition1";
        Long flowInstanceId = 1L;
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        
        List<FlowInstanceEntity> entityList = new ArrayList<>();
        entityList.add(flowInstanceEntity);
        
        when(flowInstanceRepository.findAll(any())).thenReturn(entityList);
        
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_A;
        
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(nodeInstanceEntity);
        
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity.getActivityId(), result.get(0).getActivityId());
        assertEquals(nodeInstanceEntity.getName(), result.get(0).getName());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(serviceTaskInstanceEntity, result.get().getServiceTaskInstanceEntity());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get().getUserTaskInstanceEntity());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approval1";
        Long flowInstanceId = 1L;
        
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get().getUserTaskInstanceEntity());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(gateWayInstanceEntity, result.get().getGateWayInstanceEntity());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("activity1", "element1", FlowableElementType.ELEMENT_TYPE_A);
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType())).thenReturn(new ArrayList<>());
        
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        
        // Verify
    }

}