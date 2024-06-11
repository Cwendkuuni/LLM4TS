package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;

/**
 * Testing the {@link FlowableAdaptorImpl} class
 */
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private AuthenticationFacade authenticationFacade;
    private FormService formService;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        formService = Mockito.mock(FormService.class);
        taskService = Mockito.mock(TaskService.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
        gateWayInstanceRepository = Mockito.mock(GateWayInstanceRepository.class);
        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
    }

    /**
     * Testing method: setProcessInstanceId
     */
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);

        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    /**
     * Testing method: getFlowInstanceIdByProcessInstanceId
     */
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(Mockito.any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());

        verify(flowInstanceRepository, times(1)).findAll(Mockito.any(Specification.class));
    }

    /**
     * Testing method: setProcessDefinitionId
     */
    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(affectRows, 1);

        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    /**
     * Testing method: getFlowInstanceIdByProcessDefinitionId
     */
    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(Mockito.any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());

        verify(flowInstanceRepository, times(1)).findAll(Mockito.any(Specification.class));
    }

    /**
     * Testing method: getProcessInstanceIdByFlowInstanceId
     */
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());

        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
    }

    /**
     * Testing method: getFlowableElementByType
     */
    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.OUTPUT;

        List<NodeInstanceEntity> nodes = Arrays.asList(
                new NodeInstanceEntity("node1", "node1", FlowableElementType.INPUT, FlowNodeType.USER_TASK, 1L),
                new NodeInstanceEntity("node2", "node1", FlowableElementType.OUTPUT, FlowNodeType.USER_TASK, 1L),
                new NodeInstanceEntity("node3", "node2", FlowableElementType.OUTPUT, FlowNodeType.USER_TASK, 1L)
        );

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodes);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof FlowableElement);

        verify(nodeInstanceRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);
    }

    /**
     * Testing method: getTaskInstanceByActivityId
     */
    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setId(1L);
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowNodeType.SERVICE_TASK, result.get().getNodeType());

        verify(serviceTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId);
    }

    /**
     * Testing method: getApprovalInstanceByActivityId
     */
    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowNodeType.APPROVAL_TASK, result.get().getNodeType());

        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId);
    }

    /**
     * Testing method: getApprovalInstanceByName
     */
    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approvalName";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowNodeType.APPROVAL_TASK, result.get().getNodeType());

        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId);
    }

    /**
     * Testing method: getGatewayInstanceByActivityId
     */
    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setId(1L);
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowNodeType.GATEWAY, result.get().getNodeType());

        verify(gateWayInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId);
    }

    /**
     * Testing method: setFlowableElement
     */
    @Test
    public void testSetFlowableElement() {
        AuthenticationFacade authenticationFacade = new AuthenticationFacadeImpl();
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.OUTPUT);
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);

        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.USER_TASK);
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());

        when(nodeInstanceRepository.save(Mockito.any(NodeInstanceEntity.class))).thenReturn(nodeInstanceEntity);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        verify(nodeInstanceRepository, times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }
}