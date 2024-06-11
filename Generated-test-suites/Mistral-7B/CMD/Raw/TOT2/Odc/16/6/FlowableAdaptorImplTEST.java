package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowNodeType.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowableElementTypeEnum;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstanceTestUtil;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTestUtil;
import com.oceanbase.odc.service.flow.instance.FlowableElementTestUtil;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

/**
 * Tests for {@link FlowableAdaptorImpl}
 */
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

    private FlowInstanceEntity flowInstanceEntity;

    private Long flowInstanceId = 1L;

    @Before
    public void setup() {
        flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                FlowNodeTypeEnum.SERVICE_TASK.getValue(), FlowableElementTypeEnum.INPUT.getValue()))
                .thenReturn(Collections.singletonList(Mockito.spy(new NodeInstanceEntity())));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                FlowNodeTypeEnum.APPROVAL_TASK.getValue(), FlowableElementTypeEnum.INPUT.getValue()))
                .thenReturn(Collections.singletonList(Mockito.spy(new NodeInstanceEntity())));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                FlowNodeTypeEnum.GATEWAY.getValue(), FlowableElementTypeEnum.INPUT.getValue()))
                .thenReturn(Collections.singletonList(Mockito.spy(new NodeInstanceEntity())));

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                FlowNodeTypeEnum.APPROVAL_TASK.getValue(), FlowableElementTypeEnum.OUTPUT.getValue()))
                .thenReturn(Collections.singletonList(Mockito.spy(new NodeInstanceEntity())));

        when(nodeInstanceRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, "processInstanceId");
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity expected = new FlowInstanceEntity();
        expected.setId(flowInstanceId);
        expected.setProcessInstanceId("processInstanceId");

        when(flowInstanceRepository.findAll(any()))
                .thenReturn(List.of(expected));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, "processDefinitionId");
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, "processDefinitionId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowInstanceEntity expected = new FlowInstanceEntity();
        expected.setId(flowInstanceId);
        expected.setProcessDefinitionId("processDefinitionId");

        when(flowInstanceRepository.findAll(any()))
                .thenReturn(List.of(expected));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        String processInstanceId = "processInstanceId";
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(eq(flowInstanceId),
                eq(FlowNodeTypeEnum.START_EVENT.getValue()), any()))
                .thenReturn(Collections.singletonList(Mockito.spy(new NodeInstanceEntity())));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        BaseFlowNodeInstance nodeInstance = BaseFlowNodeInstanceTestUtil
                .createNodeInstanceWithId(BaseFlowNodeInstanceTestUtil.createFlowTaskInstance("taskName"));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(flowInstanceId,
                FlowNodeTypeEnum.SERVICE_TASK.getValue(), FlowableElementTypeEnum.INPUT.getValue());

        assertEquals(1, result.size());
        assertEquals("taskName", result.get(0).getName());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        FlowTaskInstance expected = FlowTaskInstanceTestUtil.createFlowTaskInstance();
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = Mockito.mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceRepository.findByActivityIdAndInstanceId(eq("activityId"), eq(flowInstanceId)))
                .thenReturn(serviceTaskInstanceEntity);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId("activityId", flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        FlowApprovalInstance expected = FlowableElementTestUtil.createApprovalInstance("name");
        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByNameAndInstanceId(eq("name"), eq(flowInstanceId)))
                .thenReturn(userTaskInstanceEntity);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName("name", flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        FlowApprovalInstance expected = FlowableElementTestUtil.createApprovalInstance("activityId");
        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepository.findByActivityIdAndInstanceId(eq("activityId"), eq(flowInstanceId)))
                .thenReturn(userTaskInstanceEntity);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId("activityId",
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        FlowGatewayInstance expected = FlowableElementTestUtil.createGatewayInstance("activityId");
        GateWayInstanceEntity gateWayInstanceEntity = Mockito.mock(GateWayInstanceEntity.class);
        when(gateWayInstanceRepository.findByActivityIdAndInstanceId(eq("activityId"), eq(flowInstanceId)))
                .thenReturn(gateWayInstanceEntity);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId("activityId",
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testSetFlowableElement() {
        String activityId = "activityId";
        String name = "name";
        String flowableElementType = "flowableElementType";
        String flowInstanceId = "flowInstanceId";
        String nodeInstanceId = "nodeInstanceId";

        NodeInstanceEntity nodeInstanceEntity = Mockito.mock(NodeInstanceEntity.class);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(eq(nodeInstanceId),
                eq(FlowNodeTypeEnum.SERVICE_TASK.getValue()), eq(FlowableElementTypeEnum.INPUT.getValue())))
                .thenReturn(List.of(nodeInstanceEntity));

        NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();
        newNodeInstanceEntity.setInstanceId(nodeInstanceId);
        newNodeInstanceEntity.setInstanceType(FlowNodeTypeEnum.SERVICE_TASK.getValue());
        newNodeInstanceEntity.setFlowableElementType(FlowableElementTypeEnum.INPUT.getValue());
        newNodeInstanceEntity.setActivityId(activityId);
        newNodeInstanceEntity.setName(name);

        flowableAdaptor.setFlowableElement(new NodeInstanceEntity(),
                FlowableElementTestUtil.createFlowableElement(activityId, name, flowableElementType));
        verify(nodeInstanceRepository, times(1)).save(eq(newNodeInstanceEntity));
    }
}