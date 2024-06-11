package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowableElementTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceEntity.getId()).thenReturn(1L);
        List<FlowInstanceEntity> flowInstanceEntityList = Arrays.asList(flowInstanceEntity);

        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processInstanceIdEquals("processInstanceId"))))
                .thenReturn(flowInstanceEntityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceEntity.getId()).thenReturn(1L);
        List<FlowInstanceEntity> flowInstanceEntityList = Arrays.asList(flowInstanceEntity);

        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processDefinitionIdEquals("processDefinitionId"))))
                .thenReturn(flowInstanceEntityList);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceEntity.getProcessInstanceId()).thenReturn("processInstanceId");

        Optional<FlowInstanceEntity> optional = Optional.of(flowInstanceEntity);
        when(flowInstanceRepository.findById(anyLong())).thenReturn(optional);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);

        assertTrue(result.isPresent());
        assertEquals("processInstanceId", result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.END_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntityList = Arrays.asList(
                new NodeInstanceEntity(),
                new NodeInstanceEntity().setActivityId("activityId1").setName("name1").setFlowableElementType(
                        FlowableElementType.START_EVENT),
                new NodeInstanceEntity().setActivityId("activityId2").setName("name2").setFlowableElementType(
                        FlowableElementType.USER_TASK),
                new NodeInstanceEntity().setActivityId("activityId3").setName("name3").setFlowableElementType(
                        FlowableElementType.END_EVENT),
                new NodeInstanceEntity().setActivityId("activityId4").setName("name4").setInstanceType(instanceType)
                        .setFlowableElementType(flowableElementType));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntityList);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals("activityId4", result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceEntity.getId()).thenReturn(1L);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.SERVICE_TASK),
                eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceEntity.getId()).thenReturn(1L);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL_TASK),
                eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceEntity.getId()).thenReturn(1L);
        when(userTaskInstanceEntity.getName()).thenReturn(name);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(FlowNodeType.APPROVAL_TASK), eq(name),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
        when(gateWayInstanceEntity.getId()).thenReturn(1L);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.GATEWAY), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
    }
}