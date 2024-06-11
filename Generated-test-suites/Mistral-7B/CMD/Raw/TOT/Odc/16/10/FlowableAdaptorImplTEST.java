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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.task.mapper.OdcRuntimeDelegateMapper;

@Service
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "instanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "instanceId";
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(
                Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(
                Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(Collections.singletonList(new NodeInstanceEntity()));
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        FlowTaskInstance expected = new FlowTaskInstance(serviceTaskInstanceEntity, new OdcRuntimeDelegateMapper(),
                mock(TaskService.class), mock(EventPublisher.class), mock(NodeInstanceEntityRepository.class),
                mock(SequenceInstanceRepository.class), serviceTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        FlowApprovalInstance expected = new FlowApprovalInstance(userTaskInstanceEntity, mock(FlowService.class),
                mock(TaskService.class), mock(FormService.class), mock(EventPublisher.class), mock(AuthenticationFacade.class),
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                userTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        FlowApprovalInstance expected = new FlowApprovalInstance(userTaskInstanceEntity, mock(FlowService.class),
                mock(TaskService.class), mock(FormService.class), mock(EventPublisher.class), mock(AuthenticationFacade.class),
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                userTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        FlowGatewayInstance expected = new FlowGatewayInstance(gateWayInstanceEntity, mock(FlowService.class),
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                gateWayInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("name", FlowableElementType.START);
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        nodeInstance.setId(nodeInstanceId);
        nodeInstance.setFlowInstanceId(1L);
        nodeInstance.setNodeType(FlowNodeType.USER_TASK);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
                nodeInstance.getNodeType(), flowableElement.getType())).thenReturn(Collections.emptyList());
        when(nodeInstanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        verify(nodeInstanceRepository).save(eq(new NodeInstanceEntity()));
    }
}