package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

class FlowableAdaptorImplTest {

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Stream.of(flowInstanceEntity).collect(Collectors.toList()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(1L, result.get()));
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Stream.of(flowInstanceEntity).collect(Collectors.toList()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(1L, result.get()));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(processInstanceId, result.get()));
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType nodeType = FlowNodeType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(
                new NodeInstanceEntity(1L, nodeType, flowableElementType, instanceId, 1L));
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);
        assertEquals(1, result.size());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity(1L, null, null, null, activityId,
                flowInstanceId, null, null, null, null);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(activityId, result.get().getActivityId()));
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(userTaskInstanceEntity -> {
            userTaskInstanceEntity.setName(activityId);
            userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        });
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(activityId, result.get().getActivityId()));
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(userTaskInstanceEntity -> {
            userTaskInstanceEntity.setName(name);
            userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        });
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(name, result.get().getName()));
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity(gateWayInstanceEntity -> {
            gateWayInstanceEntity.setActivityId(activityId);
            gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        });
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertAll(() -> assertTrue(result.isPresent()), () -> assertEquals(activityId, result.get().getActivityId()));
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.SERVICE_TASK);
        BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
        when(baseFlowNodeInstance.getId()).thenReturn(nodeInstanceId);
        when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.SERVICE_TASK);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
                FlowNodeType.SERVICE_TASK, FlowableElementType.SERVICE_TASK))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));
        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);
        verify(nodeInstanceRepository, times(1)).save(any());
    }
}