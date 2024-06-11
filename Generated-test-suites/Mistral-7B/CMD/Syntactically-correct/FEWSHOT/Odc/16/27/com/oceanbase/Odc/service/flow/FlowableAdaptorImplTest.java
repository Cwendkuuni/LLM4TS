package com.oceanbase.odc.service.flow.test;

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

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
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
import com.oceanbase.odc.service.flow.FlowableElementTestUtil;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowApprovalInstanceTestUtil;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstanceTestUtil;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceTestUtil;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeTestUtil;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long processInstanceId = 1L;
        Long flowInstanceId = 2L;

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(
                new FlowInstanceEntity().setId(flowInstanceId).setProcessInstanceId(processInstanceId)));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 2L;

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(
                new FlowInstanceEntity().setId(flowInstanceId).setProcessDefinitionId(processDefinitionId)));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()
                .setProcessInstanceId(processInstanceId)));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(new NodeInstanceEntity().setInstanceId(instanceId)
                .setActivityId("activityId1").setFlowableElementType(flowableElementType)
                .setInstanceType(nodeType));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntities.get(0).getActivityId(), result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId1";
        Long flowInstanceId = 1L;
        Long taskInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity().setActivityId(activityId)
                .setTaskDefinitionId("taskDefinitionId")
                .setId(taskInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertEquals("taskDefinitionId", flowTaskInstance.getTaskDefinitionId());
        assertNotNull(flowTaskInstance.getRuntimeService());
        assertNotNull(flowTaskInstance.getEventNotifier());
        assertNotNull(flowTaskInstance.getProcessInstance());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId1";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setActivityId(activityId)
                .setId(approvalInstanceId)
                .setName("name");

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertEquals("name", flowApprovalInstance.getName());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getEventNotifier());
        assertNotNull(flowApprovalInstance.getRuntimeService());
        assertNotNull(flowApprovalInstance.getProcessInstance());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setActivityId("activityId1")
                .setName(name).setId(approvalInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(name, flowApprovalInstance.getName());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId1";
        Long flowInstanceId = 1L;
        Long gatewayInstanceId = 1L;
        GateWayInstanceEntity gatewayInstanceEntity = new GateWayInstanceEntity().setActivityId(activityId)
                .setId(gatewayInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gatewayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertEquals(gatewayInstanceId, flowGatewayInstance.getId());
        assertNotNull(flowGatewayInstance.getFlowService());
        assertNotNull(flowGatewayInstance.getNodeRepository());
        assertNotNull(flowGatewayInstance.getSequenceRepository());
        assertNotNull(flowGatewayInstance.getGatewayInstanceRepository());
    }

    @Test
    public void testSetFlowableElement() {
        Long id = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        String name = "name";
        String activityId = "activityId1";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity().setInstanceId(id).setInstanceType(nodeType)
                .setFlowableElementType(flowableElementType);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, nodeType,
                flowableElementType)).thenReturn(Collections.emptyList());

        FlowableElement flowableElement = new FlowableElement().setActivityId(activityId).setName(name)
                .setType(flowableElementType);

        flowableAdaptorImpl.setFlowableElement(FlowableElementTestUtil.createMockBaseFlowNodeInstance(id),
                flowableElement);

        verify(nodeInstanceRepository, times(1)).save(nodeInstanceEntity);
    }
}