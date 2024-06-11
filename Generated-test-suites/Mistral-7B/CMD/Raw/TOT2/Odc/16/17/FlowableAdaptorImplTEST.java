package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SuppressWarnings("unchecked")
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
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class))).thenAnswer(new Answer<NodeInstanceEntity>() {
            @Override
            public NodeInstanceEntity answer(NodeInstanceEntity entity) {
                return entity;
            }
        });

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                any(FlowableElementType.class))).thenReturn(Collections.emptyList());

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.ofNullable(new UserTaskInstanceEntity()));

        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.ofNullable(new UserTaskInstanceEntity()));

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.ofNullable(new GateWayInstanceEntity()));

        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(new FlowInstanceEntity()));

        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(new FlowInstanceEntity()));

        when(eventPublisher.publish(any())).thenReturn(true);

        when(taskService.createTaskQuery().processInstanceId(anyLong())).thenReturn(mock(TaskService.class));
        when(taskService.createTaskQuery().executionId(anyLong())).thenReturn(mock(TaskService.class));

        when(formService.getTaskListenerFormData(anyLong(), anyLong())).thenReturn("Form Data");
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long processInstanceId = 1L;
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findByProcessInstanceId(processInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long taskId = 1L;

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        when(odcRuntimeDelegateMapper.convertToFlowTaskInstance(any())).thenReturn(new FlowTaskInstance());

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long approvalId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        when(odcRuntimeDelegateMapper.convertToFlowApprovalInstance(any())).thenReturn(new FlowApprovalInstance());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(approvalId, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        Long approvalId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
        when(odcRuntimeDelegateMapper.convertToFlowApprovalInstance(any())).thenReturn(new FlowApprovalInstance());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(approvalId, result.get().getId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long gatewayId = 1L;

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));
        when(odcRuntimeDelegateMapper.convertToFlowGatewayInstance(any())).thenReturn(new FlowGatewayInstance());

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(gatewayId, result.get().getId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement();

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId(flowableElement.getActivityId());
        entity.setName(flowableElement.getName());
        entity.setFlowableElementType(flowableElement.getType());

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(), flowableElement);

        verify(nodeInstanceRepository).save(entity);
    }
}