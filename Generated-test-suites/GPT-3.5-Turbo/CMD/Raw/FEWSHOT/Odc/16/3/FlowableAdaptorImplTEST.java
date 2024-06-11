/*
 * JUnit 4 Test Cases for FlowableAdaptorImpl class
 */

package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test valid case
        Long flowInstanceId = 1L;
        String processInstanceId = "proc1";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<String> retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(retrievedProcessInstanceId.isPresent());
        assertEquals(processInstanceId, retrievedProcessInstanceId.get());

        // Test invalid case
        assertFalse(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(2L).isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test valid case
        Long flowInstanceId = 1L;
        String processDefinitionId = "def1";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());

        // Test invalid case
        assertFalse(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("def2").isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.GATEWAY_NODE;

        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<FlowTaskInstance> taskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(taskInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(approvalInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approval1";
        Long flowInstanceId = 1L;

        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(approvalInstance.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<FlowGatewayInstance> gatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(gatewayInstance.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.SERVICE_TASK);
        nodeInstance.setFlowInstanceId(1L);

        FlowableElement flowableElement = new FlowableElement("activity1", "name1", FlowableElementType.USER_TASK);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
    }

}