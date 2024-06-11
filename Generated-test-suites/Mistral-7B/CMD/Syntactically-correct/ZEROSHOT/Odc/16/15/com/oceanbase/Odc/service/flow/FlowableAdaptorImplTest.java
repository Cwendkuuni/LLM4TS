package com.oceanbase.odc.service.flow.test;

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
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private FlowableAdaptorImpl flowableAdaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity()))
                .when(flowInstanceRepository).findById(flowInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);

        Mockito.doReturn(Optional.of(flowInstanceEntity))
                .when(flowInstanceRepository).findAll(Mockito.any());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity()))
                .when(flowInstanceRepository).findById(flowInstanceId);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);

        Mockito.doReturn(Optional.of(flowInstanceEntity))
                .when(flowInstanceRepository).findAll(Mockito.any());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(1L);

        Mockito.doReturn(Optional.of(flowInstanceEntity))
                .when(flowInstanceRepository).findById(flowInstanceId);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity(),
                new NodeInstanceEntity().setActivityId("activityId1").setFlowableElementType(flowableElementType),
                new NodeInstanceEntity().setActivityId("activityId2").setFlowableElementType(flowableElementType),
                new NodeInstanceEntity().setActivityId("activityId3").setFlowableElementType(FlowableElementType.OUTPUT));

        Mockito.doReturn(nodeInstanceEntities).when(nodeInstanceRepository)
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, flowableElementType);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();

        Mockito.doReturn(Optional.of(serviceTaskInstanceEntity))
                .when(serviceTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Mockito.doReturn(Optional.of(userTaskInstanceEntity))
                .when(userTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Mockito.doReturn(Optional.of(userTaskInstanceEntity))
                .when(userTaskInstanceRepository).findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();

        Mockito.doReturn(Optional.of(gateWayInstanceEntity))
                .when(gateWayInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.OUTPUT);

        nodeInstance.setFlowInstanceId(1L);
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.USER_TASK);

        Mockito.doReturn(Optional.empty())
                .when(nodeInstanceRepository)
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType());

        nodeInstanceRepository.save(nodeInstance);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(nodeInstance);
    }
}