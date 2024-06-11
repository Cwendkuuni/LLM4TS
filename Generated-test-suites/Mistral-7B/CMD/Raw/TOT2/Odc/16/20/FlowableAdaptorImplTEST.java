package com.oceanbase.odc.service.flow.test;

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
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptorImplTest;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@ExtendWith(MockitoExtension.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
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

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    private static Long flowInstanceId = 1L;
    private static String processInstanceId = "processInstanceId";
    private static String processDefinitionId = "processDefinitionId";

    @BeforeAll
    public static void setUp() {
        // Initialize mocks and inject dependencies
    }

    @Test
    public void testSetProcessInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        // When
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Then
        // Verify that the process instance Id is bound to the flow instance Id
        Assertions.assertTrue(flowInstanceRepository.findById(flowInstanceId)
                .map(entity -> Objects.equals(entity.getProcessInstanceId(), processInstanceId))
                .isPresent());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Given
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(flowInstanceId);

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Then
        // Verify that the correct flow instance Id is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Given
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        // When
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        // Then
        // Verify that the process definition Id is bound to the flow instance Id
        Assertions.assertTrue(flowInstanceRepository.findById(flowInstanceId)
                .map(entity -> Objects.equals(entity.getProcessDefinitionId(), processDefinitionId))
                .isPresent());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Given
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        flowInstanceEntity.setId(flowInstanceId);

        // When
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        // Then
        // Verify that the correct flow instance Id is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Given
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(flowInstanceId);

        // When
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        // Then
        // Verify that the correct process instance Id is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<FlowableElement> expectedResult = Collections.singletonList(
                new FlowableElement("startEventId", "Start Event", FlowableElementType.START_EVENT));

        // When
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        // Then
        // Verify that the correct list of flowable elements is returned
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Given
        String activityId = "taskActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        // When
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        // Then
        // Verify that the correct task instance is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowTaskInstance.class, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Given
        String activityId = "approvalActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        // When
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        // Then
        // Verify that the correct approval instance is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Given
        String name = "approvalName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        // When
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        // Then
        // Verify that the correct approval instance is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowApprovalInstance.class, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Given
        String activityId = "gatewayActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        // When
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        // Then
        // Verify that the correct gateway instance is returned
        Assertions.assertTrue(result.isPresent());
        Assertions.assertInstanceOf(FlowGatewayInstance.class, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        // Given
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.APPROVAL_TASK);
        FlowableElement flowableElement = new FlowableElement("approvalElementId", "Approval Element",
                FlowableElementType.END_EVENT);

        // When
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        // Then
        // Verify that the flowable element is saved in the database
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceRepository.findById(nodeInstance.getId())
                .orElseThrow(() -> new RuntimeException("Node instance not found"));
        Assertions.assertInstanceOf(FlowableElementType.class, nodeInstanceEntity.getFlowableElementType());
        Assertions.assertEquals(flowableElement.getName(), nodeInstanceEntity.getName());
        Assertions.assertEquals(flowableElement.getActivityId(), nodeInstanceEntity.getActivityId());
    }
}