package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl adaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceId_InvalidFlowInstanceId() {
        Long flowInstanceId = null;
        String processInstanceId = "processInstanceId";
        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceId_InvalidProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = null;
        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long processInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_NotFound() {
        Long processInstanceId = 1L;
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionId_InvalidFlowInstanceId() {
        Long flowInstanceId = null;
        String processDefinitionId = "processDefinitionId";
        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionId_InvalidProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = null;
        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));
        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_NotFound() {
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId_NotFound() {
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVER;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetFlowableElementByType_EmptyList() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVER;
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(Collections.emptyList());
        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityId_NotFound() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId_NotFound() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByName_NotFound() {
        String name = "name";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId_NotFound() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());
        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.APPROVER);
        adaptor.setFlowableElement(nodeInstance, flowableElement);
    }

}