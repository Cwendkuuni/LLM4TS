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
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElement;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.test.MockFlowableService;

/**
 * Test class for {@link FlowableAdaptorImpl}
 */
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

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class.getName()), anyString(),
                anyLong())).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class.getName()), anyString(),
                anyLong())).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class.getName()), anyString(),
                anyLong())).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class.getName()), anyString(),
                anyLong())).thenReturn(Optional.of(new GateWayInstanceEntity()));
        when(nodeRepository.findById(anyLong())).thenReturn(Optional.of(new NodeInstanceEntity()));
        when(sequenceRepository.findById(anyLong())).thenReturn(Optional.of(new NodeInstanceEntity()));
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(mock(TaskService.class));
        when(taskService.createTaskQuery().processDefinitionId(anyString())).thenReturn(mock(TaskService.class));
        when(taskService.createTaskQuery().taskDefinitionKey(anyString())).thenReturn(mock(TaskService.class));
        when(formService.getTaskFormData(anyString(), anyLong())).thenReturn("FORM DATA");
        when(authenticationFacade.getCurrentUserLoginName()).thenReturn("CURRENT_USER");
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "PROCESS_INSTANCE_ID";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "PROCESS_INSTANCE_ID";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "PROCESS_DEFINITION_ID";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "PROCESS_INSTANCE_ID";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement("START_EVENT_ID", "START_EVENT_NAME",
                FlowableElementType.START_EVENT);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setFlowInstanceId(instanceId);

        List<NodeInstanceEntity> entities = Collections.singletonList(nodeInstanceEntity);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(entities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);
        assertEquals(1, result.size());
        assertEquals(flowableElement, result.get(0));
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "SERVICE_TASK_ID";
        Long flowInstanceId = 1L;
        FlowTaskInstance expected = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new OdcRuntimeDelegateMapper(), flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskInstanceRepository);
        Optional<ServiceTaskInstanceEntity> optional = Optional.of(new ServiceTaskInstanceEntity());
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "APPROVAL_TASK_ID";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expected = new FlowApprovalInstance(new UserTaskInstanceEntity(),
                flowableAdaptor, taskService, formService, eventPublisher, authenticationFacade, nodeRepository,
                sequenceRepository, userTaskInstanceRepository);
        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "APPROVAL_TASK_NAME";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expected = new FlowApprovalInstance(new UserTaskInstanceEntity(),
                flowableAdaptor, taskService, formService, eventPublisher, authenticationFacade, nodeRepository,
                sequenceRepository, userTaskInstanceRepository);
        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "GATEWAY_ID";
        Long flowInstanceId = 1L;
        FlowGatewayInstance expected = new FlowGatewayInstance(new GateWayInstanceEntity(), flowableAdaptor, nodeRepository,
                sequenceRepository, gateWayInstanceRepository);
        Optional<GateWayInstanceEntity> optional = Optional.of(new GateWayInstanceEntity());
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("ACTIVITY_ID", "NAME", FlowableElementType.END_EVENT);
        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance() {
            @Override
            public Long getId() {
                return nodeInstanceId;
            }

            @Override
            public FlowNodeType getNodeType() {
                return FlowNodeType.END_EVENT;
            }
        }, flowableElement);

        verify(nodeInstanceRepository, times(1)).save(argThat(nodeInstance -> {
            return nodeInstance.getActivityId() != null && nodeInstance.getName() != null
                    && nodeInstance.getFlowableElementType() != null
                    && nodeInstance.getInstanceId() == nodeInstanceId
                    && nodeInstance.getInstanceType() == FlowNodeType.END_EVENT
                    && Objects.equals(nodeInstance.getFlowableElementType(), flowableElement.getType())
                    && Objects.equals(nodeInstance.getName(), flowableElement.getName())
                    && Objects.equals(nodeInstance.getActivityId(), flowableElement.getActivityId());
        }));
    }
}