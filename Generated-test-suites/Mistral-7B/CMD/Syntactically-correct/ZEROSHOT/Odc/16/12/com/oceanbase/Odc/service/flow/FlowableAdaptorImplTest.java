package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private FlowableAdaptor flowableAdaptorMock;
    private FormService formServiceMock;
    private TaskService taskServiceMock;
    private AuthenticationFacade authenticationFacadeMock;
    private NodeInstanceEntityRepository nodeInstanceRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    private GateWayInstanceRepository gateWayInstanceRepositoryMock;
    private EventPublisher eventPublisherMock;
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapperMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
        formServiceMock = Mockito.mock(FormService.class);
        taskServiceMock = Mockito.mock(TaskService.class);
        authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        nodeInstanceRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);
        flowInstanceRepositoryMock = Mockito.mock(FlowInstanceRepository.class);
        userTaskInstanceRepositoryMock = Mockito.mock(UserTaskInstanceRepository.class);
        serviceTaskInstanceRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        gateWayInstanceRepositoryMock = Mockito.mock(GateWayInstanceRepository.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        odcRuntimeDelegateMapperMock = Mockito.mock(OdcRuntimeDelegateMapper.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setFormService(formServiceMock);
        flowableAdaptor.setTaskService(taskServiceMock);
        flowableAdaptor.setAuthenticationFacade(authenticationFacadeMock);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepositoryMock);
        flowableAdaptor.setSequenceRepository(sequenceRepositoryMock);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepositoryMock);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepositoryMock);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepositoryMock);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepositoryMock);
        flowableAdaptor.setEventPublisher(eventPublisherMock);
        flowableAdaptor.setOdcRuntimeDelegateMapper(odcRuntimeDelegateMapperMock);
        flowableAdaptor.afterPropertiesSet();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity()))
                .when(flowInstanceRepositoryMock).findById(flowInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepositoryMock, Mockito.times(1))
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setId(flowInstanceId)))
                .when(flowInstanceRepositoryMock).findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity()))
                .when(flowInstanceRepositoryMock).findById(flowInstanceId);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepositoryMock, Mockito.times(1))
                .updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setId(flowInstanceId)))
                .when(flowInstanceRepositoryMock).findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity().setProcessInstanceId(processInstanceId)))
                .when(flowInstanceRepositoryMock).findById(flowInstanceId);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.START_EVENT).setActivityId("startEventId")
                        .setName("startEventName"),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.END_EVENT).setActivityId("endEventId")
                        .setName("endEventName"),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType)
                        .setFlowableElementType(FlowableElementType.USER_TASK).setActivityId("userTaskId")
                        .setName("userTaskName"));
        Mockito.doReturn(nodeInstanceEntities)
                .when(nodeInstanceRepositoryMock).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                        flowableElementType);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals("startEventName", result.get(0).getName());
        assertEquals(FlowableElementType.START_EVENT, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(new ServiceTaskInstanceEntity());
        Mockito.doReturn(serviceTaskInstanceEntityOptional)
                .when(serviceTaskInstanceRepositoryMock)
                .findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());

        FlowTaskInstance expected = new FlowTaskInstanceTest().build();
        FlowTaskInstance actual = result.get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getActivityId(), actual.getActivityId());
        assertEquals(expected.getTaskDefinitionKey(), actual.getTaskDefinitionKey());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        Mockito.doReturn(userTaskInstanceEntityOptional)
                .when(userTaskInstanceRepositoryMock)
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());

        FlowApprovalInstance expected = new FlowApprovalInstanceTest().build();
        FlowApprovalInstance actual = result.get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getActivityId(), actual.getActivityId());
        assertEquals(expected.getTaskDefinitionKey(), actual.getTaskDefinitionKey());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approvalName";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        Mockito.doReturn(userTaskInstanceEntityOptional)
                .when(userTaskInstanceRepositoryMock)
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());

        FlowApprovalInstance expected = new FlowApprovalInstanceTest().build();
        FlowApprovalInstance actual = result.get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getActivityId(), actual.getActivityId());
        assertEquals(expected.getTaskDefinitionKey(), actual.getTaskDefinitionKey());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(new GateWayInstanceEntity());
        Mockito.doReturn(gateWayInstanceEntityOptional)
                .when(gateWayInstanceRepositoryMock)
                .findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());

        FlowGatewayInstance expected = new FlowGatewayInstanceTest().build();
        FlowGatewayInstance actual = result.get();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getActivityId(), actual.getActivityId());
        assertEquals(expected.getGateWayDefinitionKey(), actual.getGateWayDefinitionKey());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement().setType(flowableElementType).setName("flowableElementName")
                .setActivityId("flowableElementActivityId");
        FlowableElementType nodeInstanceFlowableElementType = FlowableElementType.USER_TASK;
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance().setId(nodeInstanceId).setNodeType(FlowNodeType.USER_TASK)
                .setFlowInstanceId(1L);

        Mockito.doNothing()
                .when(nodeInstanceRepositoryMock)
                .save(Mockito.any(NodeInstanceEntity.class));

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepositoryMock, Mockito.times(1))
                .save(Mockito.any(NodeInstanceEntity.class));

        NodeInstanceEntity nodeInstanceEntity = Mockito.mock(NodeInstanceEntity.class);
        Mockito.when(nodeInstanceEntity.getActivityId()).thenReturn("flowableElementActivityId");
        Mockito.when(nodeInstanceEntity.getName()).thenReturn("flowableElementName");
        Mockito.when(nodeInstanceEntity.getFlowableElementType()).thenReturn(flowableElementType);

        Mockito.verify(nodeInstanceEntityMock, Mockito.times(1))
                .setActivityId("flowableElementActivityId");
        Mockito.verify(nodeInstanceEntityMock, Mockito.times(1))
                .setName("flowableElementName");
        Mockito.verify(nodeInstanceEntityMock, Mockito.times(1))
                .setFlowableElementType(flowableElementType);
    }
}