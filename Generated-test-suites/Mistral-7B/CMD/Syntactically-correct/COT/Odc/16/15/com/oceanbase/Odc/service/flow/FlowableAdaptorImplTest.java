package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
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

    private Long flowInstanceId = 1L;
    private String processInstanceId = "testProcessInstanceId";
    private String processDefinitionId = "testProcessDefinitionId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        verifySetProcessInstanceId();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetProcessInstanceIdWithNullOptional() {
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(entity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdWithEmptyList() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verifySetProcessDefinitionId();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetProcessDefinitionIdWithNullOptional() {
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(entity));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionIdWithEmptyList() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceIdWithNullOptional() {
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> entities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(entities);
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetFlowableElementByTypeWithEmptyList() {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(1L, FlowNodeType.START_EVENT,
                FlowableElementType.START_EVENT);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(entity));
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetTaskInstanceByActivityIdWithEmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(entity));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityIdWithEmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setName(name);
        entity.setFlowInstanceId(flowInstanceId);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(entity));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertEquals(name, result.get().getName());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByNameWithEmptyOptional() {
        String name = "testName";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity entity = new GateWayInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(entity));
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityIdWithEmptyOptional() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.START_EVENT);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));
        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
        verifySetFlowableElement();
    }

    private void verifySetProcessInstanceId() {
        // Implement the verification logic here
    }

    private void verifySetProcessDefinitionId() {
        // Implement the verification logic here
    }

    private void verifySetFlowableElement() {
        // Implement the verification logic here
    }
}