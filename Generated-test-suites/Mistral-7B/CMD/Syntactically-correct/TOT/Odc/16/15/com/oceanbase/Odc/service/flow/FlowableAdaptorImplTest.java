package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.test.mocks.MockFlowableAdaptor;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    @Mock
    private FlowableAdaptor flowableAdaptorMock;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepositoryMock;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;

    @Mock
    private EventPublisher eventPublisherMock;

    @Mock
    private NodeInstanceEntityRepository nodeRepositoryMock;

    @Mock
    private SequenceInstanceRepository sequenceRepositoryMock;

    @Mock
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Mock
    private TaskService taskServiceMock;

    @Mock
    private FormService formServiceMock;

    @Mock
    private AuthenticationFacade authenticationFacadeMock;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepositoryMock;

    @InjectMocks
    private FlowableAdaptorImpl systemUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(nodeInstanceRepositoryMock.save(any(NodeInstanceEntity.class))).thenReturn(mock(NodeInstanceEntity.class));
        when(serviceTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong())).thenAnswer(invocationOnMock -> Optional.ofNullable(mock(ServiceTaskInstanceEntity.class)));
        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong())).thenAnswer(invocationOnMock -> Optional.ofNullable(mock(UserTaskInstanceEntity.class)));
        when(gateWayInstanceRepositoryMock.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong())).thenAnswer(invocationOnMock -> Optional.ofNullable(mock(GateWayInstanceEntity.class)));
        when(flowInstanceRepositoryMock.findById(anyLong())).thenAnswer(invocationOnMock -> {
            FlowInstanceEntity entity = mock(FlowInstanceEntity.class);
            entity.setProcessInstanceId("processInstanceId");
            entity.setProcessDefinitionId("processDefinitionId");
            return Optional.ofNullable(entity);
        });
        when(flowInstanceRepositoryMock.findAll(any(Specification.class))).thenAnswer(invocationOnMock -> {
            List<FlowInstanceEntity> entities = new ArrayList<>();
            entities.add(mock(FlowInstanceEntity.class));
            return Optional.ofNullable(entities);
        });
        when(flowInstanceRepositoryMock.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);
        when(flowInstanceRepositoryMock.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);
        when(nodeRepositoryMock.findById(anyLong())).thenAnswer(invocationOnMock -> Optional.ofNullable(mock(NodeInstanceEntity.class)));
        when(nodeRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenAnswer(invocationOnMock -> {
                    List<NodeInstanceEntity> entities = new ArrayList<>();
                    entities.add(mock(NodeInstanceEntity.class));
                    return Optional.ofNullable(entities);
                });
        when(sequenceRepositoryMock.findByInstanceIdAndInstanceTypeAndName(anyLong(), any(FlowNodeType.class),
                anyString())).thenAnswer(invocationOnMock -> Optional.ofNullable(mock(NodeInstanceEntity.class)));
        when(sequenceRepositoryMock.save(any(NodeInstanceEntity.class))).thenAnswer(invocationOnMock -> {
            NodeInstanceEntity entity = mock(NodeInstanceEntity.class);
            entity.setActivityId("activityId");
            entity.setName("name");
            entity.setInstanceId(invocationOnMock.getArgument(0).getId());
            entity.setInstanceType(invocationOnMock.getArgument(0).getNodeType());
            return entity;
        });
        when(eventPublisherMock.publishEvent(any())).thenAnswer(invocationOnMock -> {});

        when(taskServiceMock.createExecutionQuery()).thenReturn(mock(org.flowable.engine.runtime.ExecutionQuery.class));
        when(taskServiceMock.createExecutionQuery()).thenAnswer(invocationOnMock -> {
            org.flowable.engine.runtime.ExecutionQuery query = mock(org.flowable.engine.runtime.ExecutionQuery.class);
            when(query.list()).thenAnswer(invocationOnMock -> Collections.emptyList());
            return query;
        });
        when(taskServiceMock.createExecutionQuery()).thenAnswer(invocationOnMock -> {
            org.flowable.engine.runtime.ExecutionQuery query = mock(org.flowable.engine.runtime.ExecutionQuery.class);
            when(query.singleResult()).thenAnswer(invocationOnMock -> mock(org.flowable.engine.runtime.Execution.class));
            return query;
        });
        when(formServiceMock.getTaskFormData(anyLong(), anyString())).thenAnswer(invocationOnMock -> {
            Object formData = mock(Object.class);
            return formData;
        });
        when(authenticationFacadeMock.getCurrentUserId()).thenAnswer(invocationOnMock -> "userId");

        systemUnderTest = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        systemUnderTest.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepositoryMock, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(mock(FlowInstanceEntity.class)));
        Optional<Long> result = systemUnderTest.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
        verify(flowInstanceRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        when(flowInstanceRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(mock(FlowInstanceEntity.class)));
        Optional<Long> result = systemUnderTest.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
        verify(flowInstanceRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(mock(FlowInstanceEntity.class)));
        Optional<String> result = systemUnderTest.getProcessInstanceIdByFlowInstanceId(1L);
        assertTrue(result.isPresent());
        assertEquals("processInstanceId", result.get());
        verify(flowInstanceRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<NodeInstanceEntity> nodes = new ArrayList<>();
        nodes.add(mock(NodeInstanceEntity.class));
        when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(Optional.ofNullable(nodes));

        List<FlowableElement> result = systemUnderTest.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertEquals(1, result.size());
        verify(nodeInstanceRepositoryMock, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);

        when(serviceTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.ofNullable(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = systemUnderTest.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(serviceTaskInstanceEntity.getActivityId(), result.get().getActivityId());
        assertEquals(serviceTaskInstanceEntity.getActivityName(), result.get().getName());
        verify(serviceTaskInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId, flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);

        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = systemUnderTest.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertEquals(userTaskInstanceEntity.getActivityId(), result.get().getActivityId());
        assertEquals(userTaskInstanceEntity.getName(), result.get().getName());
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId, flowInstanceId);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);

        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = systemUnderTest.getApprovalInstanceByName(name, flowInstanceId);

        assertEquals(userTaskInstanceEntity.getActivityId(), result.get().getActivityId());
        assertEquals(userTaskInstanceEntity.getName(), result.get().getName());
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);

        when(gateWayInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.ofNullable(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = systemUnderTest.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertEquals(gateWayInstanceEntity.getActivityId(), result.get().getActivityId());
        assertEquals(gateWayInstanceEntity.getName(), result.get().getName());
        verify(gateWayInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId, flowInstanceId);
    }

    @Test
    public void testSetFlowableElement() {
        Long id = 1L;
        String activityId = "activityId";
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);

        systemUnderTest.setFlowableElement(nodeInstance, flowableElement);

        NodeInstanceEntity nodeInstanceEntity = mock(NodeInstanceEntity.class);
        when(nodeInstanceRepositoryMock.save(any(NodeInstanceEntity.class))).thenAnswer(invocationOnMock -> nodeInstanceEntity);
        when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, nodeInstance.getNodeType(),
                flowableElementType)).thenAnswer(invocationOnMock -> Optional.ofNullable(Collections.singletonList(nodeInstanceEntity)));

        verify(nodeInstanceRepositoryMock, times(1)).save(nodeInstanceEntity);
    }
}