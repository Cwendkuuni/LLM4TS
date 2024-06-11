package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.FlowableAdaptorImplTest;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowableAdaptorImpl flowableAdaptorImpl;
    private FormService formServiceMock;
    private TaskService taskServiceMock;
    private EventPublisher eventPublisherMock;
    private AuthenticationFacade authenticationFacadeMock;
    private NodeInstanceEntityRepository nodeInstanceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    private GateWayInstanceRepository gateWayInstanceRepositoryMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @Before
    public void setup() {
        flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptor = flowableAdaptorImpl;
        formServiceMock = Mockito.mock(FormService.class);
        taskServiceMock = Mockito.mock(TaskService.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        nodeInstanceRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepositoryMock = Mockito.mock(UserTaskInstanceRepository.class);
        gateWayInstanceRepositoryMock = Mockito.mock(GateWayInstanceRepository.class);
        flowInstanceRepositoryMock = Mockito.mock(FlowInstanceRepository.class);
        flowableAdaptorImpl.setFormService(formServiceMock);
        flowableAdaptorImpl.setTaskService(taskServiceMock);
        flowableAdaptorImpl.setEventPublisher(eventPublisherMock);
        flowableAdaptorImpl.setAuthenticationFacade(authenticationFacadeMock);
        flowableAdaptorImpl.setNodeInstanceRepository(nodeInstanceRepositoryMock);
        flowableAdaptorImpl.setServiceTaskInstanceRepository(serviceTaskInstanceRepositoryMock);
        flowableAdaptorImpl.setUserTaskInstanceRepository(userTaskInstanceRepositoryMock);
        flowableAdaptorImpl.setGateWayInstanceRepository(gateWayInstanceRepositoryMock);
        flowableAdaptorImpl.setFlowInstanceRepository(flowInstanceRepositoryMock);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));
        int affectRows = flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process_instance_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        Mockito.when(flowInstanceRepositoryMock.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(result.get(), 1L);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "process_definition_id";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));
        int affectRows = flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(affectRows, 1);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "process_definition_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        Mockito.when(flowInstanceRepositoryMock.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(result.get(), 1L);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_instance_id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        Mockito.when(flowInstanceRepositoryMock.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(result.get(), processInstanceId);
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setId(1L);
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Mockito.when(serviceTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        FlowTaskInstance testInstance = result.get();
        assertEquals(testInstance.getActivityId(), activityId);
        assertEquals(testInstance.getFlowInstanceId(), flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance testInstance = result.get();
        assertEquals(testInstance.getActivityId(), activityId);
        assertEquals(testInstance.getFlowInstanceId(), flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        FlowApprovalInstance testInstance = result.get();
        assertEquals(testInstance.getName(), name);
        assertEquals(testInstance.getFlowInstanceId(), flowInstanceId);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity_id";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setId(1L);
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        Mockito.when(gateWayInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        FlowGatewayInstance testInstance = result.get();
        assertEquals(testInstance.getActivityId(), activityId);
        assertEquals(testInstance.getFlowInstanceId(), flowInstanceId);
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.OUTPUT;
        FlowableElement flowableElement = new FlowableElement("activity_id", "name", flowableElementType);
        FlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        Mockito.when(nodeInstance.getId()).thenReturn(nodeInstanceId);
        Mockito.when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
        Mockito.when(nodeInstance.getFlowInstanceId()).thenReturn(1L);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        Mockito.verify(nodeInstanceRepositoryMock, Mockito.times(1))
                .save(Mockito.argThat(entity -> Objects.equals(entity.getFlowableElementType(), flowableElementType) &&
                        Objects.equals(entity.getName(), flowableElement.getName()) &&
                        Objects.equals(entity.getActivityId(), flowableElement.getActivityId()) &&
                        Objects.equals(entity.getInstanceType(), FlowNodeType.APPROVAL_TASK) &&
                        Objects.equals(entity.getInstanceId(), nodeInstanceId) &&
                        Objects.equals(entity.getFlowInstanceId(), 1L)));
    }
}