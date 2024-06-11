package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
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
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;

@MockitoJUnitRunner
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

    @Mock
    private AuthenticationFacadeMock authenticationFacadeMock;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.ofNullable(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity().setId(flowInstanceId)
                .setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(
                Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.ofNullable(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity().setId(flowInstanceId)
                .setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(
                Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.ofNullable(new FlowInstanceEntity())
                .setProcessInstanceId(processInstanceId));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity().setInstanceId(instanceId)
                        .setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.INPUT)
                        .setActivityId("activityId1"),
                new NodeInstanceEntity().setInstanceId(instanceId)
                        .setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.OUTPUT)
                        .setActivityId("activityId2"));
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals(FlowableElementType.INPUT, result.get(0).getType());
        assertEquals("activityId1", result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        verify(serviceTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.ofNullable(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        verify(gateWayInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId);
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeType,
                flowableElementType)).thenReturn(Collections.emptyList());

        FlowableElement flowableElement = mock(FlowableElement.class);
        when(flowableAdaptor.setFlowableElement(nodeInstance, flowableElement)).thenCallRealMethod();

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity().setInstanceId(nodeInstanceId)
                .setInstanceType(nodeType)
                .setFlowableElementType(flowableElementType);
        nodeInstanceRepository.save(nodeInstanceEntity);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository, times(1)).save(argThat(entity -> {
            assertEquals(nodeInstanceId, entity.getInstanceId());
            assertEquals(nodeType, entity.getInstanceType());
            assertEquals(flowableElementType, entity.getFlowableElementType());
            assertEquals(flowableElement.getActivityId(), entity.getActivityId());
            assertEquals(flowableElement.getName(), entity.getName());
            return true;
        }));
    }

    @Test
    public void testInnerConvert() {
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceEntity.getFlowInstanceId()).thenReturn(1L);
        when(serviceTaskInstanceEntity.getId()).thenReturn(1L);

        OdcRuntimeDelegateMapper odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        NodeRepository nodeRepository = mock(NodeRepository.class);
        SequenceRepository sequenceRepository = mock(SequenceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        Optional<ServiceTaskInstanceEntity> optional = Optional.ofNullable(serviceTaskInstanceEntity);

        FlowTaskInstance result = flowableAdaptor.innerConvert(optional, flowableAdaptor,
                (entity, flowService) -> new FlowTaskInstance(entity, odcRuntimeDelegateMapper, flowService,
                        eventPublisher, taskService, nodeRepository, sequenceRepository,
                        serviceTaskInstanceRepository));

        assertEquals(1L, result.getId());
        assertEquals(1L, result.getFlowInstanceId());
        verify(odcRuntimeDelegateMapper, times(1)).getRuntimeDelegate(any(), eq(ServiceTaskInstanceEntity.class));
        verify(formService, times(1)).getFormData(anyLong(), anyLong());
        verify(taskService, times(1)).createTask(any(), any(), anyMap(), any(), anyLong(), anyLong(), anyString(),
                anyString());
        verify(nodeRepository, times(1)).findById(anyLong());
        verify(sequenceRepository, times(1)).getSequence(anyLong(), anyLong(), anyLong());
        verify(serviceTaskInstanceRepository, times(1)).findById(anyLong());
    }
}