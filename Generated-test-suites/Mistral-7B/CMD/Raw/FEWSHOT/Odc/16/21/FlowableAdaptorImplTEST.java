package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    private FlowInstanceRepository flowInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setup() {
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        authenticationFacade = mock(AuthenticationFacade.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setEventPublisher(Mockito.mock(EventPublisher.class));
        flowableAdaptor.setNodeRepository(nodeInstanceRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }

    @After
    public void teardown() {
        reset(flowInstanceRepository, gateWayInstanceRepository, nodeInstanceRepository, sequenceRepository,
                serviceTaskInstanceRepository, userTaskInstanceRepository, taskService, formService,
                authenticationFacade);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(null);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(null);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(processDefinitionId, flowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        String activityId = "activityId";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setActivityId(activityId);

        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(nodeInstanceEntity);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(activityId, result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        String taskDefinitionKey = "taskDefinitionKey";

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        serviceTaskInstanceEntity.setProcessInstanceId(processInstanceId);
        serviceTaskInstanceEntity.setTaskDefinitionKey(taskDefinitionKey);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, null, null, null, null, null, null, null, null);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertSame(flowTaskInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        String taskDefinitionKey = "taskDefinitionKey";
        String assignee = "assignee";

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceEntity.setProcessInstanceId(processInstanceId);
        userTaskInstanceEntity.setTaskDefinitionKey(taskDefinitionKey);
        userTaskInstanceEntity.setAssignee(assignee);

        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, null, null, null, null, null, null, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertSame(flowApprovalInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        String taskDefinitionKey = "taskDefinitionKey";
        String assignee = "assignee";

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceEntity.setProcessInstanceId(processInstanceId);
        userTaskInstanceEntity.setTaskDefinitionKey(taskDefinitionKey);
        userTaskInstanceEntity.setAssignee(assignee);

        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, null, null, null, null, null, null, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertSame(flowApprovalInstance, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        String gatewayName = "gatewayName";

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        gateWayInstanceEntity.setName(gatewayName);

        FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, null, null, null, null);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertSame(flowGatewayInstance, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
        String activityId = "activityId";
        String name = "name";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(nodeInstanceId);
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setFlowInstanceId(UUID.randomUUID().toString());

        FlowableElement flowableElement = new FlowableElement(activityId, name, flowableElementType);

        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance() {
            @Override
            public Long getId() {
                return nodeInstanceId;
            }

            @Override
            public FlowNodeType getNodeType() {
                return nodeType;
            }

            @Override
            public Long getFlowInstanceId() {
                return Long.parseLong("1");
            }
        }, flowableElement);

        NodeInstanceEntity result = nodeInstanceRepository.findById(nodeInstanceId).get();

        assertEquals(activityId, result.getActivityId());
        assertEquals(name, result.getName());
        assertEquals(flowableElementType, result.getFlowableElementType());
    }
}