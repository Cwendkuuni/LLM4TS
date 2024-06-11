import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceId;
import org.flowable.engine.runtime.ProcessRuntime;
import org.flowable.engine.runtime.Task;
import org.junit.jupyter.annotations.DisplayName;
import org.junit.jupyter.annotations.Notes;
import org.junit.jupyter.annotations.WithClasses;
import org.junit.jupyter.api.BeforeEach;
import org.junit.jupyter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupyter.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.task.mapper.OdcRuntimeDelegateMapper;
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
import com.oceanbase.odc.service.flow.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowInstance;
import com.oceanbase.odc.service.flow.task.FlowService;
import com.oceanbase.odc.service.flow.task.FlowableServiceTask;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@WithClasses(FlowableAdaptorImpl.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowService flowService;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private FormService formService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test setProcessInstanceId() - happy path")
    @Notes("Test setting processInstanceId for a given flowInstanceId")
    public void testSetProcessInstanceIdHappyPath() {
        ProcessInstance processInstance = mock(ProcessInstance.class);
        processInstance.getId().setValue("testProcessInstanceId");

        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        when(flowService.getProcessInstance(anyLong(), anyString())).thenReturn(processInstance);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    @DisplayName("Test getFlowInstanceIdByProcessInstanceId() - happy path")
    @Notes("Test getting flowInstanceId for a given processInstanceId")
    public void testGetFlowInstanceIdByProcessInstanceIdHappyPath() {
        ProcessInstance processInstance = mock(ProcessInstance.class);
        processInstance.getId().setValue("testProcessInstanceId");
        Long flowInstanceId = 1L;

        when(flowService.getProcessInstance(anyString(), anyString())).thenReturn(processInstance);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        assertEquals(flowInstanceId, flowableAdaptor.getFlowInstanceIdByProcessInstanceId("testProcessInstanceId").get());
    }

    @Test
    @DisplayName("Test setProcessDefinitionId() - happy path")
    @Notes("Test setting processDefinitionId for a given flowInstanceId")
    public void testSetProcessDefinitionIdHappyPath() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    @DisplayName("Test getFlowInstanceIdByProcessDefinitionId() - happy path")
    @Notes("Test getting flowInstanceId for a given processDefinitionId")
    public void testGetFlowInstanceIdByProcessDefinitionIdHappyPath() {
        String processDefinitionId = "testProcessDefinitionId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        assertEquals(flowInstanceId, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get());
    }

    @Test
    @DisplayName("Test getProcessInstanceIdByFlowInstanceId() - happy path")
    @Notes("Test getting processInstanceId for a given flowInstanceId")
    public void testGetProcessInstanceIdByFlowInstanceIdHappyPath() {
        String processInstanceId = "testProcessInstanceId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    @DisplayName("Test getFlowableElementByType() - happy path")
    @Notes("Test getting flowableElements of a given instanceId, nodeType, and flowableElementType")
    public void testGetFlowableElementByTypeHappyPath() {
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL_GATEWAY;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        Long instanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = List.of(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(2, flowableElements.size());
    }

    @Test
    @DisplayName("Test getTaskInstanceByActivityId() - happy path")
    @Notes("Test getting taskInstance for a given activityId and flowInstanceId")
    public void testGetTaskInstanceByActivityIdHappyPath() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        FlowTaskInstance flowTaskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).get();

        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    @DisplayName("Test getApprovalInstanceByActivityId() - happy path")
    @Notes("Test getting approvalInstance for a given activityId and flowInstanceId")
    public void testGetApprovalInstanceByActivityIdHappyPath() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        FlowApprovalInstance flowApprovalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId).get();

        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    @DisplayName("Test getApprovalInstanceByName() - happy path")
    @Notes("Test getting approvalInstance for a given name and flowInstanceId")
    public void testGetApprovalInstanceByNameHappyPath() {
        String name = "testName";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        FlowApprovalInstance flowApprovalInstance = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).get();

        assertEquals(name, flowApprovalInstance.getName());
        assertEquals(flowInstanceId, flowApprovalInstance.getFlowInstanceId());
    }

    @Test
    @DisplayName("Test getGatewayInstanceByActivityId() - happy path")
    @Notes("Test getting gatewayInstance for a given activityId and flowInstanceId")
    public void testGetGatewayInstanceByActivityIdHappyPath() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        FlowGatewayInstance flowGatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId).get();

        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertEquals(flowInstanceId, flowGatewayInstance.getFlowInstanceId());
    }

    @Test
    @DisplayName("Test setFlowableElement() - happy path")
    @Notes("Test setting flowableElement for a given nodeInstance and flowableElement")
    public void testSetFlowableElementHappyPath() {
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL_GATEWAY;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        Long instanceId = 1L;
        String activityId = "testActivityId";
        String name = "testName";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(nodeType.name());
        nodeInstanceEntity.setFlowInstanceId(instanceId);

        FlowableElement flowableElement = new FlowableElement(activityId, name, flowableElementType);

        nodeInstanceRepository.save(nodeInstanceEntity);

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(instanceId, nodeType), flowableElement);

        NodeInstanceEntity updatedNodeInstanceEntity = nodeInstanceRepository.findById(instanceId).get();

        assertEquals(activityId, updatedNodeInstanceEntity.getActivityId());
        assertEquals(name, updatedNodeInstanceEntity.getName());
        assertEquals(flowableElementType, updatedNodeInstanceEntity.getFlowableElementType());
    }
}