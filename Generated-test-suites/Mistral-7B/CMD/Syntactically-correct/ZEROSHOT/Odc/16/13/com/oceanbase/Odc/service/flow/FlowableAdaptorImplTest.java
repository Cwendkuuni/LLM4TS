package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private FormService formService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setProcessInstanceIdTest() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional::of);
        int affectRows = 1;
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(affectRows);

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceIdTest() {
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        List<FlowInstanceEntity> flowInstanceEntities = Collections.singletonList(flowInstanceEntity);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(flowInstanceEntities);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(result.get(), 1L);
    }

    @Test
    public void setProcessDefinitionIdTest() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional::of);
        int affectRows = 1;
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(affectRows);

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(affectRows, 1);
    }

    @Test
    public void getFlowInstanceIdByProcessDefinitionIdTest() {
        String processDefinitionId = "testProcessDefinitionId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
        List<FlowInstanceEntity> flowInstanceEntities = Collections.singletonList(flowInstanceEntity);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(flowInstanceEntities);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(result.get(), 1L);
    }

    @Test
    public void getProcessInstanceIdByFlowInstanceIdTest() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);
        Optional<FlowInstanceEntity> optional = Optional.of(flowInstanceEntity);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(result.get(), processInstanceId);
    }

    @Test
    public void getFlowableElementByTypeTest() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);
        assertEquals(result.size(), 1);
    }

    @Test
    public void getTaskInstanceByActivityIdTest() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Optional<ServiceTaskInstanceEntity> optional = Optional.of(serviceTaskInstanceEntity);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(optional);

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void getApprovalInstanceByActivityIdTest() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Optional<UserTaskInstanceEntity> optional = Optional.of(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void getApprovalInstanceByNameTest() {
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
        Optional<UserTaskInstanceEntity> optional = Optional.of(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void getGatewayInstanceByActivityIdTest() {
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
        Optional<GateWayInstanceEntity> optional = Optional.of(gateWayInstanceEntity);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(optional);

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);
        assertNotNull(result.get());
    }

    @Test
    public void setFlowableElementTest() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", flowableElementType);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setName("testName");
        nodeInstanceEntity.setActivityId("testActivityId");

        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class)))
                .thenReturn(nodeInstanceEntity);

        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(instanceId, nodeType), flowableElement);

        List<NodeInstanceEntity> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);
        assertEquals(result.get(0).getName(), "testName");
    }
}