import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {

    @Test
    public void testSetProcessInstanceId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;
        String processInstanceId = "testInstanceId";

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1))
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String processInstanceId = "testInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(List.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assertions.assertEquals(result.get(), 1L);
    }

    @Test
    public void testSetProcessDefinitionId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1))
                .updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String processDefinitionId = "testProcessDefinitionId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(List.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        Assertions.assertEquals(result.get(), 1L);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId("testProcessInstanceId");

        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(result.get(), "testProcessInstanceId");
    }

    @Test
    public void testGetFlowableElementByType() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("testActivityId");
        nodeInstanceEntity.setName("testName");
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType.name());
        nodeInstanceEntity.setFlowableElementType(flowableElementType.name());
        nodeInstanceEntity.setFlowInstanceId(instanceId);

        List<NodeInstanceEntity> nodeInstanceEntityList = List.of(nodeInstanceEntity);

        NodeInstanceEntityRepository nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntityList);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getType(), flowableElementType);
    }

    @Test
    public void testGetTaskInstanceByActivityId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        serviceTaskInstanceEntity.setInstanceId(1L);

        OdcRuntimeDelegateMapper odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, odcRuntimeDelegateMapper, formService, taskService, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(result.get(), flowTaskInstance);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceEntity.setInstanceId(1L);
        userTaskInstanceEntity.setName("testName");

        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowService, taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(result.get(), flowApprovalInstance);
    }

    @Test
    public void testGetApprovalInstanceByName() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String name = "testName";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId("testActivityId");
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceEntity.setInstanceId(1L);
        userTaskInstanceEntity.setName(name);

        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        FormService formService = mock(FormService.class);
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

        FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowService, taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        Assertions.assertEquals(result.get(), flowApprovalInstance);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        gateWayInstanceEntity.setInstanceId(1L);

        GateWayInstanceRepository gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        FlowService flowService = mock(FlowService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);

        FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, flowService, nodeRepository, sequenceRepository, gateWayInstanceRepository);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        Assertions.assertEquals(result.get(), flowGatewayInstance);
    }

    @Test
    public void testSetFlowableElement() throws Exception {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Long id = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", flowableElementType);

        BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);

        when(baseFlowNodeInstance.getId()).thenReturn(id);
        when(baseFlowNodeInstance.getNodeType()).thenReturn(nodeType);
        when(baseFlowNodeInstance.getFlowInstanceId()).thenReturn(id);

        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("testActivityId");
        nodeInstanceEntity.setName("testName");
        nodeInstanceEntity.setInstanceId(id);
        nodeInstanceEntity.setInstanceType(nodeType.name());
        nodeInstanceEntity.setFlowableElementType(flowableElementType.name());
        nodeInstanceEntity.setFlowInstanceId(id);

        Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(nodeInstanceEntity);
    }
}