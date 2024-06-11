package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jUpasMockitoStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository.UserTaskInstanceSpecs;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;

@SpringJUnitConfig(classes = FlowableAdaptorImpl.class)
@MockitoStyle(deterministic = true)
public class FlowableAdaptorImplTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

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
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    @NonNull
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId)));

        assertEquals(Optional.of(flowInstanceId), flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId)));

        assertEquals(Optional.of(flowInstanceId), flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(processInstanceId)));

        assertEquals(Optional.of(processInstanceId), flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<FlowableElement> expected = Arrays.asList(new FlowableElement("startEventId", "Start Event", FlowableElementType.START_EVENT));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        assertEquals(expected, flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType, flowableElementType));
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        FlowTaskInstance expected = mock(FlowTaskInstance.class);
        when(new OdcRuntimeDelegateMapper().map(any(ServiceTaskInstanceEntity.class))).thenReturn(expected);

        assertEquals(Optional.of(expected), flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId));
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        FlowApprovalInstance expected = mock(FlowApprovalInstance.class);
        when(new OdcRuntimeDelegateMapper().map(any(UserTaskInstanceEntity.class))).thenReturn(expected);

        assertEquals(Optional.of(expected), flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId));
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        FlowApprovalInstance expected = mock(FlowApprovalInstance.class);
        when(new OdcRuntimeDelegateMapper().map(any(UserTaskInstanceEntity.class))).thenReturn(expected);

        assertEquals(Optional.of(expected), flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId));
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));

        FlowGatewayInstance expected = mock(FlowGatewayInstance.class);
        when(new OdcRuntimeDelegateMapper().map(any(GateWayInstanceEntity.class))).thenReturn(expected);

        assertEquals(Optional.of(expected), flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId));
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.emptyList());

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository).save(any(NodeInstanceEntity.class));
    }
}