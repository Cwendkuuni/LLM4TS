import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GatewayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
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
    private FlowableAdaptorImpl flowableAdaptorImpl;

    private BaseFlowNodeInstance nodeInstance;
    private UserTaskInstanceEntity userTaskInstanceEntity;
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    private GateWayInstanceEntity gateWayInstanceEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstance = new BaseFlowNodeInstance(1L, FlowNodeType.APPROVAL_TASK);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-instance-id";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process-instance-id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity(1L, processInstanceId, null);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "process-definition-id";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity(1L, null, processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-instance-id";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(flowInstanceId, processInstanceId, null)));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals("process-instance-id", result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL;
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, nodeType, flowableElementType, null, null, null);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, nodeType, flowableElementType, null, null, null);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, flowableElementType))
                .thenReturn(List.of(userTaskInstanceEntity1, userTaskInstanceEntity2));

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType, flowableElementType);

        assertEquals(2, result.size());
        assertEquals(userTaskInstanceEntity1.getActivityId(), result.get(0).getActivityId());
        assertEquals(userTaskInstanceEntity1.getName(), result.get(0).getName());
        assertEquals(FlowableElementType.APPROVAL, result.get(0).getType());
        assertEquals(userTaskInstanceEntity2.getActivityId(), result.get(1).getActivityId());
        assertEquals(userTaskInstanceEntity2.getName(), result.get(1).getName());
        assertEquals(FlowableElementType.APPROVAL, result.get(1).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId, null, null, null);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertEquals(activityId, flowTaskInstance.getActivityId());
        assertNotNull(flowTaskInstance.getFlowService());
        assertNotNull(flowTaskInstance.getTaskService());
        assertNotNull(flowTaskInstance.getEventPublisher());
        assertNotNull(flowTaskInstance.getNodeRepository());
        assertNotNull(flowTaskInstance.getSequenceRepository());
        assertNotNull(flowTaskInstance.getServiceTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId, null, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(activityId, flowApprovalInstance.getActivityId());
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(FlowNodeType.APPROVAL_TASK, null, flowInstanceId, name, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertEquals(name, flowApprovalInstance.getName());
        assertNotNull(flowApprovalInstance.getFlowService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity(FlowNodeType.GATEWAY, activityId, flowInstanceId, null, null, null);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertEquals(activityId, flowGatewayInstance.getActivityId());
        assertNotNull(flowGatewayInstance.getFlowService());
        assertNotNull(flowGatewayInstance.getNodeRepository());
        assertNotNull(flowGatewayInstance.getSequenceRepository());
        assertNotNull(flowGatewayInstance.getGateWayInstanceRepository());
    }

    @Test
    public void testSetFlowableElement() {
        FlowNodeType nodeType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL;
        String activityId = "activity-id";
        String name = "name";

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(activityId);
        nodeInstanceEntity.setName(name);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setInstanceId(nodeInstance.getId());
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setFlowInstanceId(nodeInstance.getFlowInstanceId());

        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        flowableAdaptorImpl.setFlowableElement(nodeInstance, new FlowableElement(activityId, name, flowableElementType));

        verify(nodeInstanceRepository).save(nodeInstanceEntity);
    }
}