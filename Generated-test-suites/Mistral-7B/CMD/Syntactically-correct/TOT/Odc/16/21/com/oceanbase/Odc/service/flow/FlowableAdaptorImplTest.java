package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;
import com.oceanbase.odc.service.iam.auth.mocks.AuthenticationFacadeMock;

/**
 * Test for {@link FlowableAdaptorImpl}
 */
public class FlowableAdaptorImplTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.getAuthentication()).thenReturn(new AuthenticationFacadeMock());
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testInstanceId";

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(asList(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(1L, flowInstanceIdOptional.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(asList(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(1L, flowInstanceIdOptional.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> processInstanceIdOptional = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(processInstanceIdOptional.isPresent());
        assertEquals(processInstanceId, processInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = asList(new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> flowableElementList = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(nodeInstanceEntities.size(), flowableElementList.size());
        for (int i = 0; i < nodeInstanceEntities.size(); i++) {
            FlowableElement flowableElement = flowableElementList.get(i);
            NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.get(i);
            assertEquals(nodeInstanceEntity.getActivityId(), flowableElement.getActivityId());
            assertEquals(nodeInstanceEntity.getName(), flowableElement.getName());
        }
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> flowTaskInstanceOptional = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(flowTaskInstanceOptional.isPresent());

        FlowTaskInstance flowTaskInstance = flowTaskInstanceOptional.get();
        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertNotNull(flowTaskInstance.getFlowService());
        assertNotNull(flowTaskInstance.getTaskService());
        assertNotNull(flowTaskInstance.getNodeRepository());
        assertNotNull(flowTaskInstance.getSequenceRepository());
        assertNotNull(flowTaskInstance.getServiceTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(flowApprovalInstanceOptional.isPresent());

        FlowApprovalInstance flowApprovalInstance = flowApprovalInstanceOptional.get();
        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptorImpl.getApprovalInstanceByName(name,
                flowInstanceId);

        assertTrue(flowApprovalInstanceOptional.isPresent());

        FlowApprovalInstance flowApprovalInstance = flowApprovalInstanceOptional.get();
        assertEquals(name, flowApprovalInstance.getName());
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> flowGatewayInstanceOptional = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(flowGatewayInstanceOptional.isPresent());

        FlowGatewayInstance flowGatewayInstance = flowGatewayInstanceOptional.get();
        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertNotNull(flowGatewayInstance.getFlowService());
        assertNotNull(flowGatewayInstance.getNodeRepository());
        assertNotNull(flowGatewayInstance.getSequenceRepository());
        assertNotNull(flowGatewayInstance.getGateWayInstanceRepository());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.END_EVENT);

        nodeInstance.setFlowInstanceId(1L);

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType());

        assertEquals(1, nodeInstanceEntities.size());
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.get(0);
        assertEquals(flowableElement.getActivityId(), nodeInstanceEntity.getActivityId());
        assertEquals(flowableElement.getName(), nodeInstanceEntity.getName());
        assertEquals(flowableElement.getType(), nodeInstanceEntity.getFlowableElementType());
    }
}