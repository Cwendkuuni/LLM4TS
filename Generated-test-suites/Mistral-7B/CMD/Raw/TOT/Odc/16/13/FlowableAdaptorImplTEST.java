package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowNodeTypeConstants;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElement;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.iam.model.OdcUser;

/**
 * Test class for {@link FlowableAdaptorImpl}
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
    private OdcRuntimeDelegateMapper mapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        UUID flowInstanceId = UUID.randomUUID();
        String processInstanceId = "testProcessInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(eq(flowInstanceId), eq(processInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        UUID processInstanceId = UUID.randomUUID();
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(UUID.randomUUID());
        flowInstanceEntity.setProcessInstanceId(processInstanceId.toString());

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId.toString());

        assertTrue(result.isPresent());
        assertEquals(flowInstanceEntity.getId(), result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        UUID processDefinitionId = UUID.randomUUID();
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(UUID.randomUUID());
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId.toString());

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId.toString());

        assertTrue(result.isPresent());
        assertEquals(flowInstanceEntity.getId(), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        UUID flowInstanceId = UUID.randomUUID();
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> entities = Arrays.asList(
                new NodeInstanceEntity(),
                new NodeInstanceEntity().setActivityId("activityId1").setFlowableElementType(flowableElementType),
                new NodeInstanceEntity().setActivityId("activityId2").setFlowableElementType(flowableElementType));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, nodeType,
                flowableElementType)).thenReturn(entities);

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        assertEquals(1, result.size());
        assertEquals("activityId1", result.get(0).getActivityId());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "testActivityId";
        long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity().setActivityId(activityId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertSame(serviceTaskInstanceEntity, result.get().getEntity());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "testActivityId";
        long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setActivityId(activityId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertSame(userTaskInstanceEntity, result.get().getEntity());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "testName";
        long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity().setName(name);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertSame(userTaskInstanceEntity, result.get().getEntity());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "testActivityId";
        long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity().setActivityId(activityId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertSame(gateWayInstanceEntity, result.get().getEntity());
    }

    @Test
    public void testSetFlowableElement() {
        OdcUser user = new OdcUser();
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.USER_TASK);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.USER_TASK)).thenReturn(Collections.singletonList(
                        new NodeInstanceEntity().setInstanceId(nodeInstance.getId()).setFlowableElementType(
                                FlowableElementType.USER_TASK)));

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

        NodeInstanceEntity result = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), FlowableElementType.USER_TASK).get(0);

        assertEquals("testActivityId", result.getActivityId());
        assertEquals("testName", result.getName());
        assertEquals(FlowableElementType.USER_TASK, result.getFlowableElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElement_NullFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = null;

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElement_DifferentFlowableElementType() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.START_EVENT);

        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
    }
}