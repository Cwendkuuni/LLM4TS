package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.flow.instance.InstanceType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(1, affectRows);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);

        List<FlowInstanceEntity> entities = Collections.singletonList(entity);

        when(flowInstanceRepository.findAll(eq(Specification.class))).thenReturn(entities);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, Optional.of(new FlowInstanceEntity()));

        int affectRows = flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(1, affectRows);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessDefinitionId(processDefinitionId);

        List<FlowInstanceEntity> entities = Collections.singletonList(entity);

        when(flowInstanceRepository.findAll(eq(Specification.class))).thenReturn(entities);

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);

        Optional<FlowInstanceEntity> optional = Optional.of(entity);

        when(flowInstanceRepository.findById(eq(Long.class))).thenReturn(optional);

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType elementType = FlowableElementType.APPROVAL;

        List<NodeInstanceEntity> entities = Collections.singletonList(new NodeInstanceEntity());

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                elementType)).thenReturn(entities);

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType, elementType);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(new ServiceTaskInstanceEntity());

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.SERVICE_TASK), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(serviceTaskInstanceEntityOptional);

        BiFunction<ServiceTaskInstanceEntity, FlowableAdaptor, FlowTaskInstance> function = (entity, flowableAdaptor) -> {
            FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
            flowTaskInstance.setId(entity.getId());
            return flowTaskInstance;
        };

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL_TASK), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(userTaskInstanceEntityOptional);

        BiFunction<UserTaskInstanceEntity, FlowableAdaptor, FlowApprovalInstance> function = (entity, flowableAdaptor) -> {
            FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance();
            flowApprovalInstance.setId(entity.getId());
            return flowApprovalInstance;
        };

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(FlowNodeType.APPROVAL_TASK), eq(name),
                eq(flowInstanceId)))
                .thenReturn(userTaskInstanceEntityOptional);

        BiFunction<UserTaskInstanceEntity, FlowableAdaptor, FlowApprovalInstance> function = (entity, flowableAdaptor) -> {
            FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance();
            flowApprovalInstance.setId(entity.getId());
            return flowApprovalInstance;
        };

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(new GateWayInstanceEntity());

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.GATEWAY), eq(activityId),
                eq(flowInstanceId)))
                .thenReturn(gateWayInstanceEntityOptional);

        BiFunction<GateWayInstanceEntity, FlowableAdaptor, FlowGatewayInstance> function = (gateway, flowableAdaptor) -> {
            FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance();
            flowGatewayInstance.setId(gateway.getId());
            return flowGatewayInstance;
        };

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.APPROVAL);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceId, InstanceType.FLOW_NODE,
                FlowableElementType.APPROVAL)).thenReturn(Collections.emptyList());

        flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, InstanceType.FLOW_NODE), flowableElement);

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId(flowableElement.getActivityId());
        entity.setName(flowableElement.getName());
        entity.setFlowableElementType(flowableElement.getType());
        entity.setInstanceId(nodeInstanceId);
        entity.setInstanceType(InstanceType.FLOW_NODE);
        entity.setFlowInstanceId(nodeInstanceId);

        verify(nodeInstanceRepository, times(1)).save(entity);
    }

}