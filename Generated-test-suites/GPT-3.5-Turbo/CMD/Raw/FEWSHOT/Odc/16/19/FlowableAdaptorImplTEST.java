package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

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
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123abc";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        Optional<FlowInstanceEntity> optional = Optional.of(flowInstanceEntity);

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "abc123";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);

        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class))).thenReturn(entityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "def456";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        Optional<FlowInstanceEntity> optional = Optional.of(flowInstanceEntity);

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);
        Mockito.when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);

        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class))).thenReturn(entityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId("abc123");

        Optional<FlowInstanceEntity> optional = Optional.of(entity);

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals("abc123", result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT;

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId("activity1");
        entity.setName("Element1");
        entity.setFlowableElementType(flowableElementType);

        List<NodeInstanceEntity> entityList = Collections.singletonList(entity);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(entityList);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "task1";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setInstanceType(FlowNodeType.SERVICE_TASK);
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<ServiceTaskInstanceEntity> optional = Optional.of(entity);

        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId)).thenReturn(optional);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "approval1";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId)).thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approvalTask";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        entity.setName(name);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId)).thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "gateway1";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity entity = new GateWayInstanceEntity();
        entity.setInstanceType(FlowNodeType.GATEWAY);
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<GateWayInstanceEntity> optional = Optional.of(entity);

        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId)).thenReturn(optional);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeId = 1L;
        FlowableElement flowableElement = new FlowableElement("activity1", "Element1", FlowableElementType.ELEMENT);

        NodeInstanceEntity existingNode = new NodeInstanceEntity();
        existingNode.setActivityId(flowableElement.getActivityId());
        existingNode.setName(flowableElement.getName());
        existingNode.setFlowableElementType(flowableElement.getType());

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeId, FlowNodeType.TASK, FlowableElementType.ELEMENT)).thenReturn(Arrays.asList(existingNode));

        flowableAdaptor.setFlowableElement(existingNode, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(0)).save(Mockito.any(NodeInstanceEntity.class));
    }

}