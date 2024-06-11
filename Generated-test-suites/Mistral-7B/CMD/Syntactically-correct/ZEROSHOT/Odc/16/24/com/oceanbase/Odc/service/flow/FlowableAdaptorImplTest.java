package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
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
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeStub;

@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        when(flowInstanceRepository.findById(ArgumentMatchers.longThat(id -> id.equals(flowInstanceId)))).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long processInstanceId = 1L;
        Long flowInstanceId = 2L;

        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setId(1L);
        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();
        flowInstanceEntity2.setId(flowInstanceId);
        flowInstanceEntity2.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(List.of(flowInstanceEntity1, flowInstanceEntity2));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        when(flowInstanceRepository.findById(ArgumentMatchers.longThat(id -> id.equals(flowInstanceId)))).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(processDefinitionId, flowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 2L;

        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setId(1L);
        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();
        flowInstanceEntity2.setId(flowInstanceId);
        flowInstanceEntity2.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(List.of(flowInstanceEntity1, flowInstanceEntity2));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(ArgumentMatchers.longThat(id -> id.equals(flowInstanceId)))).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType).setFlowableElementType(FlowableElementType.START)
                        .setActivityId("activityId1").setName("name1"),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType).setFlowableElementType(FlowableElementType.END)
                        .setActivityId("activityId2").setName("name2"),
                new NodeInstanceEntity().setInstanceId(instanceId).setInstanceType(instanceType).setFlowableElementType(flowableElementType)
                        .setActivityId("activityId3").setName("name3")
        );

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities.stream().filter(e -> e.getFlowableElementType() == flowableElementType).collect(Collectors.toList()));

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals("name3", result.get(0).getName());
        assertEquals("activityId3", result.get(0).getActivityId());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity().setActivityId(activityId).setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setActivityId(activityId).setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setName(name).setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertEquals(name, result.get().getName());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity().setActivityId(activityId).setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(activityId, result.get().getActivityId());
        assertEquals(flowInstanceId, result.get().getFlowInstanceId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.START);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, FlowNodeType.START_EVENT, FlowableElementType.START))
                .thenReturn(Collections.emptyList());

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeInstanceEntity), flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(new NodeInstanceEntity().setInstanceId(nodeInstanceId).setInstanceType(FlowNodeType.START_EVENT)
                .setActivityId(flowableElement.getActivityId()).setName(flowableElement.getName())
                .setFlowableElementType(flowableElement.getType()));

        verify(nodeInstanceRepository).saveAll(nodeInstanceEntities);
    }

    @Test
    public void testInnerConvert() {
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);

        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, "testActivityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        FlowApprovalInstance result = flowableAdaptorImpl.getApprovalInstanceByActivityId("testActivityId", 1L).get();

        verify(flowableAdaptor).convertToFlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor);
        assertEquals(userTaskInstanceEntity, result.getNodeInstance());

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "testActivityId", 1L))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        FlowTaskInstance result2 = flowableAdaptorImpl.getTaskInstanceByActivityId("testActivityId", 1L).get();

        verify(flowableAdaptor).convertToFlowTaskInstance(serviceTaskInstanceEntity, flowableAdaptor);
        assertEquals(serviceTaskInstanceEntity, result2.getNodeInstance());

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "testActivityId", 1L))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        FlowGatewayInstance result3 = flowableAdaptorImpl.getGatewayInstanceByActivityId("testActivityId", 1L).get();

        verify(flowableAdaptor).convertToFlowGatewayInstance(gateWayInstanceEntity, flowableAdaptor);
        assertEquals(gateWayInstanceEntity, result3.getNodeInstance());
    }
}