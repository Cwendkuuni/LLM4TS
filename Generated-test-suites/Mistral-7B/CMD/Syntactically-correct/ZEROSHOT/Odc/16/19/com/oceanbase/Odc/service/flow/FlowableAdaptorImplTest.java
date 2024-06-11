package com.oceanbase.odc.service.flow.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTest;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@ContextConfiguration(classes = { FlowableAdaptorImpl.class, TestConfig.class },
        loader = AnnotationConfigContextLoader.class)
@SpringJUnitConfig
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptor flowableAdaptor;
    @Autowired
    private FlowableAdaptorImpl flowableAdaptorImpl;
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

    private TaskService taskServiceMock;
    private FormService formServiceMock;
    private AuthenticationFacade authenticationFacadeMock;

    @org.junit.Before
    public void setup() {
        taskServiceMock = mock(TaskService.class);
        formServiceMock = mock(FormService.class);
        authenticationFacadeMock = mock(AuthenticationFacade.class);
        PreConditions.init();
        Verify.init();
        when(taskService.createExecutionQuery()).thenReturn(Mockito.mock(org.flowable.engine.runtime.ExecutionQuery.class));
    }

    @org.junit.Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        Verify.verify(flowInstanceRepository, Mockito.times(1)).updateProcessInstanceIdById(flowInstanceId,
                processInstanceId);
    }

    @org.junit.Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findAll(
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(
                Collections.singletonList(flowInstanceEntity));
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        Verify.verify(flowInstanceRepository, Mockito.times(1)).findAll(
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)));
        Verify.verify(flowInstanceIdOptional).isPresent();
    }

    @org.junit.Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Verify.verify(flowInstanceRepository, Mockito.times(1)).updateProcessDefinitionIdById(flowInstanceId,
                processDefinitionId);
    }

    @org.junit.Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        when(flowInstanceRepository.findAll(
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(
                Collections.singletonList(flowInstanceEntity));
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        Verify.verify(flowInstanceRepository, Mockito.times(1)).findAll(
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)));
        Verify.verify(flowInstanceIdOptional).isPresent();
    }

    @org.junit.Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        Verify.verify(flowInstanceRepository, Mockito.times(1)).findById(flowInstanceId);
        Verify.verify(processInstanceIdOptional).isPresent();
    }

    @org.junit.Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(
                new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity());
        nodeInstanceEntities.get(0).setInstanceId(instanceId);
        nodeInstanceEntities.get(0).setInstanceType(nodeType);
        nodeInstanceEntities.get(0).setFlowableElementType(flowableElementType);
        nodeInstanceEntities.get(0).setActivityId("activityId1");
        nodeInstanceEntities.get(0).setName("name1");

        List<FlowableElement> expectedFlowableElements = List.of(
                new FlowableElement("activityId1", "name1", FlowableElementType.START_EVENT));
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(nodeInstanceEntities);
        List<FlowableElement> actualFlowableElements = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        Verify.verify(nodeInstanceRepository, Mockito.times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, nodeType, flowableElementType);
        assertThat(actualFlowableElements).isEqualTo(expectedFlowableElements);
    }

    @org.junit.Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
        Optional<FlowTaskInstance> flowTaskInstanceOptional = flowableAdaptor.getTaskInstanceByActivityId(activityId,
                flowInstanceId);
        Verify.verify(serviceTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(
                FlowNodeType.SERVICE_TASK, activityId, flowInstanceId);
        assertThat(flowTaskInstanceOptional).isPresent();
    }

    @org.junit.Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByActivityId(
                activityId, flowInstanceId);
        Verify.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId);
        assertThat(flowApprovalInstanceOptional).isPresent();
    }

    @org.junit.Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<FlowApprovalInstance> flowApprovalInstanceOptional = flowableAdaptor.getApprovalInstanceByName(name,
                flowInstanceId);
        Verify.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndName(
                FlowNodeType.APPROVAL_TASK, name, flowInstanceId);
        assertThat(flowApprovalInstanceOptional).isPresent();
    }

    @org.junit.Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
        Optional<FlowGatewayInstance> flowGatewayInstanceOptional = flowableAdaptor.getGatewayInstanceByActivityId(
                activityId, flowInstanceId);
        Verify.verify(gateWayInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(
                FlowNodeType.GATEWAY, activityId, flowInstanceId);
        assertThat(flowGatewayInstanceOptional).isPresent();
    }

    @org.junit.Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.START_EVENT);
        when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.SERVICE_TASK);
        when(nodeInstance.getFlowInstanceId()).thenReturn(1L);
        when(nodeInstanceRepository.save(Mockito.any(NodeInstanceEntity.class))).thenReturn(Mockito.mock(
                NodeInstanceEntity.class));
        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
        Verify.verify(nodeInstanceRepository, Mockito.times(1)).save(Mockito.any(NodeInstanceEntity.class));
    }
}