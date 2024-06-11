package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
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
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeInstanceRepository;
import com.oceanbase.odc.service.flow.instance.FlowableElementTypeConverter;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test class for {@link FlowableAdaptorImpl}.
 */
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

    @Mock
    private FlowInstanceEntity flowInstanceEntity;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private GateWayInstanceEntity gateWayInstanceEntity;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Mock
    private FlowableElementTypeConverter flowableElementTypeConverter;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "nodeInstanceRepository", nodeInstanceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "serviceTaskInstanceRepository",
                serviceTaskInstanceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "eventPublisher", eventPublisher);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "nodeRepository", nodeRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "sequenceRepository", sequenceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "flowInstanceRepository", flowInstanceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "userTaskInstanceRepository",
                userTaskInstanceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "taskService", taskService);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "formService", formService);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "authenticationFacade", authenticationFacade);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "gateWayInstanceRepository",
                gateWayInstanceRepository);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "flowableElementTypeConverter",
                flowableElementTypeConverter);
        ReflectionTestUtils.setFieldValue(flowableAdaptor, "odcRuntimeDelegateMapper", odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(processDefinitionId, flowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceEntity.getId(), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowInstanceEntity.getProcessInstanceId()).thenReturn(processInstanceId);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.APPROVAL;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType, flowableElementType)).thenReturn(nodeInstanceEntities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(flowableElementType, result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        FlowTaskInstance expected = new FlowTaskInstance(serviceTaskInstanceEntity, odcRuntimeDelegateMapper, taskService,
                nodeRepository, sequenceRepository, serviceTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        FlowApprovalInstance expected = new FlowApprovalInstance(userTaskInstanceEntity, flowService, taskService, formService,
                eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        FlowApprovalInstance expected = new FlowApprovalInstance(userTaskInstanceEntity, flowService, taskService, formService,
                eventPublisher, authenticationFacade, nodeRepository, sequenceRepository, userTaskInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        FlowGatewayInstance expected = new FlowGatewayInstance(gateWayInstanceEntity, flowService, nodeRepository,
                sequenceRepository, gateWayInstanceRepository);
        assertEquals(expected, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.APPROVAL);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, nodeType, flowableElementType))
                .thenReturn(Collections.emptyList());

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, nodeType), flowableElement);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        nodeInstanceEntity.setFlowableElementType(flowableElement.getType());
        nodeInstanceEntity.setInstanceType(nodeType);
        nodeInstanceEntity.setInstanceId(nodeInstanceId);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceRepository.save(nodeInstanceEntity);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findByInstanceIdAndInstanceType(nodeInstanceId, nodeType);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(flowableElement, FlowableElementTypeConverter.convert(nodeInstanceEntities.get(0)));
    }
}