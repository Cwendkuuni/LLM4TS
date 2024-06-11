package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;

@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

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
    private EventPublisher eventPublisher;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Autowired
    private AuthenticationFacadeMock authenticationFacadeMock;

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

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(result.get(), flowInstanceId);
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

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(result.get(), flowInstanceId);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(result.get(), processInstanceId);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntities.get(0).getActivityId(), result.get(0).getActivityId());
        assertEquals(nodeInstanceEntities.get(0).getName(), result.get(0).getName());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertNotNull(result.get());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertNotNull(result.get());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertNotNull(result.get());
        assertEquals(name, result.get().getName());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertNotNull(result.get());
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
                FlowNodeType.USER_TASK, FlowableElementType.START_EVENT)).thenReturn(Collections.emptyList());

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(), flowableElement);

        verify(nodeInstanceRepository).save(nodeInstanceEntity);
    }
}