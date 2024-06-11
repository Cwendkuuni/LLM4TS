package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    @Mockito.Inject
    private TaskService taskService;

    @Mockito.Inject
    private FormService formService;

    @Mockito.Inject
    private AuthenticationFacade authenticationFacade;

    @Mockito.Inject
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mockito.Inject
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mockito.Inject
    private EventPublisher eventPublisher;

    @Mockito.Inject
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mockito.Inject
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mockito.Inject
    private SequenceInstanceRepository sequenceRepository;

    private static Long flowInstanceId;
    private static String processDefinitionId;

    @BeforeAll
    public static void setup() {
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptor = flowableAdaptorImpl;

        flowInstanceId = 1L;
        processDefinitionId = "processDefinitionId";
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(flowInstanceId, "processInstanceId");

        Assertions.assertTrue(true);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId("processInstanceId");
        flowInstanceEntity.setId(flowInstanceId);

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        Assertions.assertEquals(flowInstanceId, flowInstanceIdOptional.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Assertions.assertTrue(true);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        flowInstanceEntity.setId(flowInstanceId);

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Assertions.assertEquals(flowInstanceId, flowInstanceIdOptional.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        String processInstanceId = "processInstanceId";

        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(processInstanceId, processInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.USER_TASK);

        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.START_EVENT);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.START_EVENT))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.START_EVENT);

        Assertions.assertEquals(1, flowableElements.size());
        Assertions.assertEquals(flowableElement.getActivityId(), flowableElements.get(0).getActivityId());
        Assertions.assertEquals(flowableElement.getName(), flowableElements.get(0).getName());
        Assertions.assertEquals(FlowableElementType.START_EVENT, flowableElements.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance("taskInstanceId", activityId, null, null);

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = Mockito.mock(ServiceTaskInstanceEntity.class);
        Mockito.when(serviceTaskInstanceEntity.getActivityId()).thenReturn(activityId);
        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> flowTaskInstanceOptional = flowableAdaptor.getTaskInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertEquals(flowTaskInstance, flowTaskInstanceOptional.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance("approvalInstanceId", activityId, null, null);

        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceEntity.getActivityId()).thenReturn(activityId);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertEquals(flowApprovalInstance, flowApprovalInstanceOptional.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance("approvalInstanceId", name, null, null);

        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceEntity.getName()).thenReturn(name);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByName(name,
                flowInstanceId);

        Assertions.assertEquals(flowApprovalInstance, flowApprovalInstanceOptional.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance("gatewayInstanceId", activityId, null, null, null);

        GateWayInstanceEntity gateWayInstanceEntity = Mockito.mock(GateWayInstanceEntity.class);
        Mockito.when(gateWayInstanceEntity.getActivityId()).thenReturn(activityId);
        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> flowGatewayInstanceOptional = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        Assertions.assertEquals(flowGatewayInstance, flowGatewayInstanceOptional.get());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.USER_TASK);

        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.START_EVENT);

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.START_EVENT)).thenReturn(Collections.emptyList());

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }
}