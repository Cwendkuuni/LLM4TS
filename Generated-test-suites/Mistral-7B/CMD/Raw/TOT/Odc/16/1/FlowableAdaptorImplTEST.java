package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link FlowableAdaptorImpl}
 */
@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
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
    private GateWayInstanceRepository gateWayInstanceRepository;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private FormService formService;
    @Mock
    private TaskService taskService;

    @InjectMocks
    private FlowableAdaptorImpl adaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId-1";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = 1;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(affectRows);

        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(affectRows, 1);
        Verify.verify(affectRows == 1, "AffectRows has to be equal to one");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId-1";
        Long flowInstanceId = 1L;

        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity().setId(flowInstanceId)
                .setProcessInstanceId(processInstanceId));

        List<FlowInstanceEntity> entityList = Collections.singletonList(optional.get());

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(entityList);

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId-1";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = 1;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(affectRows);

        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(affectRows, 1);
        Verify.verify(affectRows == 1, "AffectRows has to be equal to one");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId-1";
        Long flowInstanceId = 1L;

        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity().setId(flowInstanceId)
                .setProcessDefinitionId(processDefinitionId));

        List<FlowInstanceEntity> entityList = Collections.singletonList(optional.get());

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(entityList);

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId-1";

        Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity().setId(flowInstanceId)
                .setProcessInstanceId(processInstanceId));

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> nodes = Collections.singletonList(new NodeInstanceEntity().setInstanceId(instanceId)
                .setInstanceType(instanceType)
                .setFlowableElementType(FlowableElementType.START_EVENT));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(nodes);

        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(FlowableElementType.START_EVENT, result.get(0).getType());
        assertEquals("Start Event", result.get(0).getName());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId-1";
        Long flowInstanceId = 1L;
        FlowTaskInstance expected = mock(FlowTaskInstance.class);

        ServiceTaskInstanceEntity serviceTaskInstance = new ServiceTaskInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstance));

        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId-1";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expected = mock(FlowApprovalInstance.class);

        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstance));

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name-1";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expected = mock(FlowApprovalInstance.class);

        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity().setName(name)
                .setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstance));

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertEquals(expected, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId-1";
        Long flowInstanceId = 1L;
        FlowGatewayInstance expected = mock(FlowGatewayInstance.class);

        GateWayInstanceEntity gateWayInstance = new GateWayInstanceEntity().setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstance));

        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(expected, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        String activityId = "activityId-1";
        String name = "name-1";

        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);

        adaptor.setFlowableElement(nodeInstance, new FlowableElement(activityId, name, flowableElementType));

        NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
        nodeEntity.setInstanceId(instanceId);
        nodeEntity.setInstanceType(nodeType);
        nodeEntity.setFlowableElementType(flowableElementType);
        nodeEntity.setActivityId(activityId);
        nodeEntity.setName(name);
        nodeEntity.setFlowInstanceId(instanceId);

        verify(nodeInstanceRepository).save(eq(nodeEntity));
    }
}