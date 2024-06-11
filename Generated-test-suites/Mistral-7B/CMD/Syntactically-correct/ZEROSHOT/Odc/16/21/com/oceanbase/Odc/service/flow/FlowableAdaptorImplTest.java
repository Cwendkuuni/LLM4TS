package com.oceanbase.odc.service.flow.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SpringJUnitConfig(classes = { FlowableAdaptorImpl.class })
@ActiveProfiles("test")
@SpringBootTest
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

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

    @Autowired
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    private FlowableAdaptorImpl flowableAdaptorImpl;

    @BeforeEach
    public void setup() {
        flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptorImpl.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptorImpl.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptorImpl.setEventPublisher(eventPublisher);
        flowableAdaptorImpl.setNodeRepository(nodeRepository);
        flowableAdaptorImpl.setSequenceRepository(sequenceRepository);
        flowableAdaptorImpl.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptorImpl.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptorImpl.setTaskService(taskService);
        flowableAdaptorImpl.setFormService(formService);
        flowableAdaptorImpl.setAuthenticationFacade(authenticationFacade);
        flowableAdaptorImpl.setGateWayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptorImpl.setOdcRuntimeDelegateMapper(odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, times(1))
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        flowInstanceEntity.setId(flowInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("activityId1");
        nodeInstanceEntity.setName("name1");
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowInstanceId(instanceId);
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        FlowableElement flowableElement = result.get(0);
        assertEquals("activityId1", flowableElement.getActivityId());
        assertEquals("name1", flowableElement.getName());
        assertEquals(FlowableElementType.START_EVENT, flowableElement.getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        FlowTaskInstance flowTaskInstance = result.get();
        assertNotNull(flowTaskInstance.getFlowableAdaptor());
        assertNotNull(flowTaskInstance.getTaskService());
        assertNotNull(flowTaskInstance.getNodeRepository());
        assertNotNull(flowTaskInstance.getSequenceRepository());
        assertNotNull(flowTaskInstance.getServiceTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertNotNull(flowApprovalInstance.getFlowableAdaptor());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getEventPublisher());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        FlowApprovalInstance flowApprovalInstance = result.get();
        assertNotNull(flowApprovalInstance.getFlowableAdaptor());
        assertNotNull(flowApprovalInstance.getFormService());
        assertNotNull(flowApprovalInstance.getTaskService());
        assertNotNull(flowApprovalInstance.getEventPublisher());
        assertNotNull(flowApprovalInstance.getAuthenticationFacade());
        assertNotNull(flowApprovalInstance.getNodeRepository());
        assertNotNull(flowApprovalInstance.getSequenceRepository());
        assertNotNull(flowApprovalInstance.getUserTaskInstanceRepository());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        FlowGatewayInstance flowGatewayInstance = result.get();
        assertNotNull(flowGatewayInstance.getFlowableAdaptor());
        assertNotNull(flowGatewayInstance.getFlowService());
        assertNotNull(flowGatewayInstance.getNodeRepository());
        assertNotNull(flowGatewayInstance.getSequenceRepository());
        assertNotNull(flowGatewayInstance.getGateWayInstanceRepository());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.APPROVAL_TASK);
        nodeInstance.setFlowInstanceId(1L);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.START_EVENT);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());
        nodeInstanceEntity.setInstanceId(nodeInstance.getId());
        nodeInstanceEntity.setInstanceType(nodeInstance.getNodeType());
        nodeInstanceEntity.setFlowInstanceId(nodeInstance.getFlowInstanceId());
        nodeInstanceEntities.add(nodeInstanceEntity);

        verify(nodeInstanceRepository, times(1))
                .saveAll(nodeInstanceEntities);
    }
}