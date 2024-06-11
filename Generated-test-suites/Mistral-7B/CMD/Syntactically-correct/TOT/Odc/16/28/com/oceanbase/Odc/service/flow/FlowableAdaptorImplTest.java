package com.oceanbase.odc.service.flow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;

@Slf4j
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
    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), eq(FlowNodeType.SERVICE_TASK),
                eq(FlowableElementType.END_EVENT)))
                .thenReturn(Collections.emptyList());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), eq(FlowNodeType.APPROVAL_TASK),
                eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(mock(NodeInstanceEntity.class)));
        when(nodeInstanceRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
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
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(eq(flowInstanceId)))
                .thenReturn(Optional.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(eq(flowInstanceId), eq(processDefinitionId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findById(eq(flowInstanceId)))
                .thenReturn(Optional.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(eq(flowInstanceId)))
                .thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertFalse(result.isPresent());

        when(flowInstanceRepository.findById(eq(flowInstanceId)))
                .thenReturn(Optional.of(new FlowInstanceEntity().setProcessInstanceId(processInstanceId)));

        result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceEntity.getName()).thenReturn("name");
        when(userTaskInstanceEntity.getActivityId()).thenReturn("activityId");

        List<NodeInstanceEntity> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals("name", result.get(0).getName());
        assertEquals("activityId", result.get(0).getActivityId());
        assertEquals(FlowableElementType.USER_TASK, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.SERVICE_TASK), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        verify(serviceTaskInstanceEntity, times(1)).getProcessVariable("odc:runtimeDelegate");
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL_TASK), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(FlowNodeType.APPROVAL_TASK), eq(name),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.GATEWAY), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        String name = "name";
        String activityId = "activityId";
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity existingNodeInstanceEntity = mock(NodeInstanceEntity.class);
        NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(List.of(existingNodeInstanceEntity));

        FlowableElement flowableElement = new FlowableElement(activityId, name, flowableElementType);
        flowableAdaptor.setFlowableElement(mock(BaseFlowNodeInstance.class), flowableElement);

        verify(nodeInstanceRepository, never()).save(newNodeInstanceEntity);
    }

    @Test
    public void testSetFlowableElement_newNodeInstanceCreated() {
        Long instanceId = 1L;
        String name = "name";
        String activityId = "activityId";
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity existingNodeInstanceEntity = mock(NodeInstanceEntity.class);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(Collections.emptyList());

        FlowableElement flowableElement = new FlowableElement(activityId, name, flowableElementType);
        flowableAdaptor.setFlowableElement(mock(BaseFlowNodeInstance.class), flowableElement);

        verify(nodeInstanceRepository).save(argThat(i -> Objects.equals(i.getName(), name)
                && Objects.equals(i.getActivityId(), activityId)
                && Objects.equals(i.getFlowableElementType(), flowableElementType)
                && Objects.equals(i.getFlowInstanceId(), 1L)
                && Objects.equals(i.getNodeType(), nodeType)));
    }
}