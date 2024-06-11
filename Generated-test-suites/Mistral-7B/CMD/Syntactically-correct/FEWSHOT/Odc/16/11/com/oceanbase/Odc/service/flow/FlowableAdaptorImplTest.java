package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Service
@SkipAuthorize("odc internal usage")
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
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        PreConditions mockPreConditions = mock(PreConditions.class);
        Verify mockVerify = mock(Verify.class);
        FlowInstanceEntity mockFlowInstanceEntity = mock(FlowInstanceEntity.class);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(mockFlowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(1);

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Given
        String processInstanceId = "processInstanceId";
        List<FlowInstanceEntity> mockFlowInstanceEntities = Collections.singletonList(mock(FlowInstanceEntity.class));

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(mockFlowInstanceEntities);

        // When
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        PreConditions mockPreConditions = mock(PreConditions.class);
        Verify mockVerify = mock(Verify.class);
        FlowInstanceEntity mockFlowInstanceEntity = mock(FlowInstanceEntity.class);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(mockFlowInstanceEntity));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(1);

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        List<FlowInstanceEntity> mockFlowInstanceEntities = Collections.singletonList(mock(FlowInstanceEntity.class));

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(mockFlowInstanceEntities);

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity mockFlowInstanceEntity = mock(FlowInstanceEntity.class);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(mockFlowInstanceEntity));
        when(mockFlowInstanceEntity.getProcessInstanceId()).thenReturn(processInstanceId);

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> mockNodeInstanceEntities = List.of(mock(NodeInstanceEntity.class), mock(NodeInstanceEntity.class));
        List<FlowableElement> expectedFlowableElements = List.of(
                new FlowableElement("activityId1", "name1", FlowableElementType.TYPE1),
                new FlowableElement("activityId2", "name2", FlowableElementType.TYPE1));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.ACTOR,
                FlowableElementType.TYPE1)).thenReturn(mockNodeInstanceEntities);

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, FlowNodeType.ACTOR,
                FlowableElementType.TYPE1);

        assertEquals(expectedFlowableElements, result);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity mockServiceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        NodeRepository nodeRepository = mock(NodeRepository.class);
        SequenceRepository sequenceRepository = mock(SequenceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(mockServiceTaskInstanceEntity));

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstance(mockServiceTaskInstanceEntity,
                new OdcRuntimeDelegateMapper(), formService, taskService, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskInstanceRepository);
        assertEquals(expectedFlowTaskInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity mockUserTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        NodeRepository nodeRepository = mock(NodeRepository.class);
        SequenceRepository sequenceRepository = mock(SequenceRepository.class);
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(mockUserTaskInstanceEntity));

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(mockUserTaskInstanceEntity,
                formService, taskService, formService, eventPublisher, authenticationFacade, nodeRepository,
                sequenceRepository, userTaskInstanceRepository);
        assertEquals(expectedFlowApprovalInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity mockUserTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        NodeRepository nodeRepository = mock(NodeRepository.class);
        SequenceRepository sequenceRepository = mock(SequenceRepository.class);
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId)).thenReturn(Optional.of(mockUserTaskInstanceEntity));

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(mockUserTaskInstanceEntity,
                formService, taskService, formService, eventPublisher, authenticationFacade, nodeRepository,
                sequenceRepository, userTaskInstanceRepository);
        assertEquals(expectedFlowApprovalInstance, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity mockGateWayInstanceEntity = mock(GateWayInstanceEntity.class);
        FormService formService = mock(FormService.class);
        NodeRepository nodeRepository = mock(NodeRepository.class);
        SequenceRepository sequenceRepository = mock(SequenceRepository.class);
        GateWayInstanceRepository gateWayInstanceRepository = mock(GateWayInstanceRepository.class);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(mockGateWayInstanceEntity));

        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowGatewayInstance expectedFlowGatewayInstance = new FlowGatewayInstance(mockGateWayInstanceEntity,
                formService, nodeRepository, sequenceRepository, gateWayInstanceRepository);
        assertEquals(expectedFlowGatewayInstance, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;
        String name = "name";
        String activityId = "activityId";
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();

        NodeInstanceEntity nodeInstanceEntity = mock(NodeInstanceEntity.class);
        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class))).thenReturn(nodeInstanceEntity);

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, FlowNodeType.ACTOR),
                new FlowableElement(activityId, name, flowableElementType));

        verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
    }

}