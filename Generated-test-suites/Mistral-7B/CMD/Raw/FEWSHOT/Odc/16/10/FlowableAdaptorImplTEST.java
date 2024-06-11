package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setup() {
        flowableAdaptor = Mockito.spy(new FlowableAdaptorImpl());
        flowableAdaptorImpl = (FlowableAdaptorImpl) flowableAdaptor;
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity())).when(flowableAdaptorImpl.flowInstanceRepository)
                .findById(flowInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowableAdaptorImpl.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId,
                processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceId() {
        String processInstanceId = "processInstanceId";
        flowableAdaptor.setProcessInstanceId(null, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullProcessInstanceId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Optional<FlowInstanceEntity> flowInstanceEntityOptional = Optional.of(new FlowInstanceEntity());
        Mockito.when(flowableAdaptorImpl.flowInstanceRepository.findAll(Mockito.any()))
                .thenReturn(List.of(flowInstanceEntityOptional.get()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceEntityOptional.get().getId(), result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdWithEmptyResult() {
        String processInstanceId = "processInstanceId";
        Mockito.when(flowableAdaptorImpl.flowInstanceRepository.findAll(Mockito.any()))
                .thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertFalse(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFlowInstanceIdByProcessInstanceIdWithNullProcessInstanceId() {
        flowableAdaptor.getFlowInstanceIdByProcessInstanceId(null);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        Mockito.doReturn(Optional.of(new FlowInstanceEntity())).when(flowableAdaptorImpl.flowInstanceRepository)
                .findById(flowInstanceId);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(flowableAdaptorImpl.flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId,
                processDefinitionId);
    }

    // ... Add test cases for other methods in FlowableAdaptorImpl

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, nodeType,
                flowableElementType);

        // Add assertions based on the expected behavior
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceOptional = Optional.of(new ServiceTaskInstanceEntity());

        Mockito.when(flowableAdaptorImpl.serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId))
                .thenReturn(serviceTaskInstanceOptional);

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof FlowTaskInstance);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = Optional.of(new UserTaskInstanceEntity());

        Mockito.when(flowableAdaptorImpl.userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId))
                .thenReturn(userTaskInstanceOptional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof FlowApprovalInstance);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = Optional.of(new UserTaskInstanceEntity());

        Mockito.when(flowableAdaptorImpl.userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId))
                .thenReturn(userTaskInstanceOptional);

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof FlowApprovalInstance);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<GateWayInstanceEntity> gateWayInstanceOptional = Optional.of(new GateWayInstanceEntity());

        Mockito.when(flowableAdaptorImpl.gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY,
                activityId, flowInstanceId))
                .thenReturn(gateWayInstanceOptional);

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof FlowGatewayInstance);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowNodeType nodeType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.INPUT;
        FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);
        BaseFlowNodeInstance baseFlowNodeInstance = new BaseFlowNodeInstance(nodeInstanceId, nodeType, null, null, null);

        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

        // Add assertions based on the expected behavior
    }
}