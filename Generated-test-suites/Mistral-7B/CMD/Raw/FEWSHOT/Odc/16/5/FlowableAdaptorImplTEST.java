package com.oceanbase.odc.service.flow.test;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
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
import com.oceanbase.odc.service.flow.FlowableAdaptorTestDataBuilder;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTestDataBuilder;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTestDataBuilder;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTestDataBuilder;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SpringBootTest
@ActiveProfiles("test")
public class FlowableAdaptorImplTest {

    private static final String FLOW_INSTANCE_ID = "1L";

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Autowired
    private FlowableAdaptor flowableAdaptor;
    @Autowired
    @Qualifier("flowableAdaptorImpl")
    private FlowableAdaptor flowableAdaptorImpl;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private NodeInstanceEntityRepository nodeRepository;
    @Autowired
    private SequenceInstanceRepository sequenceRepository;
    @Autowired
    private FlowInstanceRepository flowInstanceRepository;
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private GateWayInstanceRepository gateWayInstanceRepository;

    private FlowableAdaptorTestDataBuilder flowableAdaptorTestDataBuilder;

    @BeforeEach
    public void setUp() {
        flowableAdaptorTestDataBuilder = new FlowableAdaptorTestDataBuilder();
        Mockito.reset(taskService);
        Mockito.reset(formService);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long expectedFlowInstanceId = 1L;

        // Given
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(expectedFlowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        // When
        Optional<Long> actualFlowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Then
        Assertions.assertTrue(actualFlowInstanceId.isPresent());
        Assertions.assertEquals(expectedFlowInstanceId, actualFlowInstanceId.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<FlowInstanceEntity> optional = flowInstanceRepository.findById(flowInstanceId);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(processDefinitionId, optional.get().getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long expectedFlowInstanceId = 1L;

        // Given
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(expectedFlowInstanceId);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        Mockito.when(flowInstanceRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        // When
        Optional<Long> actualFlowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        // Then
        Assertions.assertTrue(actualFlowInstanceId.isPresent());
        Assertions.assertEquals(expectedFlowInstanceId, actualFlowInstanceId.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        Mockito.when(flowInstanceRepository.findById(flowInstanceId))
                .thenReturn(Optional.ofNullable(new FlowInstanceEntity().setProcessInstanceId(processInstanceId)));

        Assertions.assertEquals(processInstanceId,
                flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Collections.singletonList(
                new NodeInstanceEntity().setActivityId("start-event-id").setName("start event")
                        .setFlowableElementType(flowableElementType));

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType))
                .thenReturn(expectedNodeInstanceEntities);

        List<FlowableElement> actualFlowableElements = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        Assertions.assertEquals(expectedNodeInstanceEntities.size(), actualFlowableElements.size());
        for (int i = 0; i < expectedNodeInstanceEntities.size(); i++) {
            Assertions.assertEquals(expectedNodeInstanceEntities.get(i).getActivityId(),
                    actualFlowableElements.get(i).getActivityId());
            Assertions.assertEquals(expectedNodeInstanceEntities.get(i).getName(), actualFlowableElements.get(i).getName());
            Assertions.assertEquals(expectedNodeInstanceEntities.get(i).getFlowableElementType(),
                    actualFlowableElements.get(i).getType());
        }
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstanceTestDataBuilder().aFlowTaskInstance()
                .withActivityId(activityId).withFlowInstanceId(flowInstanceId).build();

        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity().setId(expectedFlowTaskInstance.getId())
                        .setName(expectedFlowTaskInstance.getName())));

        Optional<FlowTaskInstance> actualFlowTaskInstance = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertTrue(actualFlowTaskInstance.isPresent());
        Assertions.assertEquals(expectedFlowTaskInstance, actualFlowTaskInstance.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstanceTestDataBuilder().aFlowApprovalInstance()
                .withActivityId(activityId).withFlowInstanceId(flowInstanceId).build();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.ofNullable(new UserTaskInstanceEntity().setId(expectedFlowApprovalInstance.getId())
                        .setName(expectedFlowApprovalInstance.getName())));

        Optional<FlowApprovalInstance> actualFlowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertTrue(actualFlowApprovalInstance.isPresent());
        Assertions.assertEquals(expectedFlowApprovalInstance, actualFlowApprovalInstance.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstanceTestDataBuilder().aFlowApprovalInstance()
                .withName(name).withFlowInstanceId(flowInstanceId).build();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.ofNullable(new UserTaskInstanceEntity().setId(expectedFlowApprovalInstance.getId())
                        .setName(expectedFlowApprovalInstance.getName())));

        Optional<FlowApprovalInstance> actualFlowApprovalInstance = flowableAdaptorImpl.getApprovalInstanceByName(name,
                flowInstanceId);

        Assertions.assertTrue(actualFlowApprovalInstance.isPresent());
        Assertions.assertEquals(expectedFlowApprovalInstance, actualFlowApprovalInstance.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        FlowGatewayInstance expectedFlowGatewayInstance = new FlowGatewayInstanceTestDataBuilder().aFlowGatewayInstance()
                .withActivityId(activityId).withFlowInstanceId(flowInstanceId).build();

        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId))
                .thenReturn(Optional.ofNullable(new GateWayInstanceEntity().setId(expectedFlowGatewayInstance.getId())
                        .setName(expectedFlowGatewayInstance.getName())));

        Optional<FlowGatewayInstance> actualFlowGatewayInstance = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertTrue(actualFlowGatewayInstance.isPresent());
        Assertions.assertEquals(expectedFlowGatewayInstance, actualFlowGatewayInstance.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.INTERMEDIATE_EVENT);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId,
                FlowNodeType.INTERMEDIATE_EVENT, FlowableElementType.INTERMEDIATE_EVENT))
                .thenReturn(Collections.emptyList());

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, FlowNodeType.INTERMEDIATE_EVENT),
                flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findAll();
        Assertions.assertEquals(1, nodeInstanceEntities.size());
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.get(0);
        Assertions.assertEquals(flowableElement.getActivityId(), nodeInstanceEntity.getActivityId());
        Assertions.assertEquals(flowableElement.getName(), nodeInstanceEntity.getName());
        Assertions.assertEquals(flowableElement.getType(), nodeInstanceEntity.getFlowableElementType());
    }
}