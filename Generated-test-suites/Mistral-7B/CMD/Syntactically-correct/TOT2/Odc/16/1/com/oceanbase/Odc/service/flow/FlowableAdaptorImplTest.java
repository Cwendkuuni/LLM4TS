package com.oceanbase.odc.service.flow;

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

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SuppressWarnings("unchecked")
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";

        // When
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Then
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Given
        String processInstanceId = "process_instance_id";
        List<FlowInstanceEntity> flowInstanceEntities = Collections.singletonList(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(flowInstanceEntities);

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
        verify(flowInstanceRepository).findAll(any(Specification.class));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Given
        String processDefinitionId = "process_definition_id";
        List<FlowInstanceEntity> flowInstanceEntities = Collections.singletonList(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(flowInstanceEntities);

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
        verify(flowInstanceRepository).findAll(any(Specification.class));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        // When
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
        verify(flowInstanceRepository).findById(flowInstanceId);
    }

    @Test
    public void testGetFlowableElementByType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        // When
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        // Then
        assertEquals(1, result.size());

        // Edge case: no elements of the given type found
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(Collections.emptyList());
        List<FlowableElement> result2 = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(0, result2.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Given
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        List<ServiceTaskInstanceEntity> serviceTaskInstanceEntities = Collections.singletonList(new ServiceTaskInstanceEntity());
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntities.get(0)));

        // When
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        // Then
        assertTrue(result.isPresent());

        // Edge case: no service task instance found
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowTaskInstance> result2 = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Given
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Collections.singletonList(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntities.get(0)));

        // When
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        // Then
        assertTrue(result.isPresent());

        // Edge case: no approval task instance found
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result2 = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Given
        String name = "name";
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Collections.singletonList(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntities.get(0)));

        // When
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        // Then
        assertTrue(result.isPresent());

        // Edge case: no approval task instance found
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result2 = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Given
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        List<GateWayInstanceEntity> gateWayInstanceEntities = Collections.singletonList(new GateWayInstanceEntity());
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntities.get(0)));

        // When
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        // Then
        assertTrue(result.isPresent());

        // Edge case: no gateway instance found
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowGatewayInstance> result2 = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertFalse(result2.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElementNullNodeInstance() {
        // Given
        BaseFlowNodeInstance nodeInstance = null;
        FlowableElement flowableElement = new FlowableElement("test", FlowableElementType.START_EVENT);

        // When & Then
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElementNullFlowableElement() {
        // Given
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = null;

        // When & Then
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
    }

    @Test
    public void testSetFlowableElement() {
        // Given
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("test", FlowableElementType.START_EVENT);

        // When
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        // Then
        verify(nodeInstanceRepository).save(any(NodeInstanceEntity.class));
    }
}