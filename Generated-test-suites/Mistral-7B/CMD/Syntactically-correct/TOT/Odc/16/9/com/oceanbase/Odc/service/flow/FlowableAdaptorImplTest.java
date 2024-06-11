package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;

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
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private AuthenticationFacade authenticationFacade;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private FormService formService;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptorImpl.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        when(authenticationFacade.getAuthenticationInfo()).thenReturn(mock(Object.class));
        when(eventPublisher.publish(any())).thenReturn(true);

        PreConditions.when(flowableAdaptor).getFlowInstanceIdByProcessInstanceId(anyString());
        PreConditions.when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        PreConditions.when(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(anyLong());

        when(flowInstanceRepository.findById(anyLong())).thenAnswer(i -> new Optional<FlowInstanceEntity>() {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public FlowInstanceEntity get() {
                return new FlowInstanceEntity();
            }
        });
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 1L;
        PreConditions.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        assertNull(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testSetProcessInstanceId_InvalidFlowInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = -1L;

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertFalse(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(processDefinitionId, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get().getId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_EmptyResult() {
        String processDefinitionId = "testProcessDefinitionId";
        PreConditions.when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        assertNull(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        String activityId = "testActivityId";
        String name = "testName";

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeType),
                new FlowableElement(activityId, name, flowableElementType));

        verifyNodeInstancePresent(nodeInstanceId, nodeType, flowableElementType);
    }

    @Test
    public void testSetFlowableElement_DuplicateElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        String activityId = "testActivityId";
        String name = "testName";

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeType,
                flowableElementType)).thenReturn(List.of(new NodeInstanceEntity()));

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeType),
                new FlowableElement(activityId, name, flowableElementType));

        verifyNoNewNodeInstancesCreated(nodeInstanceId, nodeType, flowableElementType);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        Long flowInstanceId = 1L;
        String activityId = "testActivityId";
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                new OdcRuntimeDelegateMapper(), taskService, eventPublisher, nodeRepository, sequenceRepository,
                serviceTaskInstanceRepository);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> actual = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertSame(flowTaskInstance, actual.get());
    }

    @Test
    public void testGetTaskInstanceByActivityId_EmptyResult() {
        Long flowInstanceId = 1L;
        String activityId = "testActivityId";
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());

        assertNull(flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        Long flowInstanceId = 1L;
        String name = "testName";
        FlowApprovalInstance approvalInstance = new FlowApprovalInstance(new UserTaskInstanceEntity(),
                flowService, taskService, formService, eventPublisher, authenticationFacade, nodeRepository,
                sequenceRepository, userTaskInstanceRepository);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> actual = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertSame(approvalInstance, actual.get());
    }

    @Test
    public void testGetApprovalInstanceByName_EmptyResult() {
        Long flowInstanceId = 1L;
        String name = "testName";
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());

        assertNull(flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        Long flowInstanceId = 1L;
        String activityId = "testActivityId";
        FlowGatewayInstance gatewayInstance = new FlowGatewayInstance(new GateWayInstanceEntity(), flowService,
                nodeRepository, sequenceRepository, gateWayInstanceRepository);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> actual = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertSame(gatewayInstance, actual.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId_EmptyResult() {
        Long flowInstanceId = 1L;
        String activityId = "testActivityId";
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());

        assertNull(flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId).get());
    }

    private void verifyNodeInstancePresent(Long nodeInstanceId, FlowNodeType nodeType,
            FlowableElementType flowableElementType) {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeType,
                flowableElementType)).thenReturn(List.of(new NodeInstanceEntity()));
    }

    private void verifyNoNewNodeInstancesCreated(Long nodeInstanceId, FlowNodeType nodeType,
            FlowableElementType flowableElementType) {
        verify(nodeInstanceRepository, never()).save(any(NodeInstanceEntity.class));
    }
}