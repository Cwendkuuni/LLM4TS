package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FlowableAdaptorImplTest {

    @Test
    public void testSetProcessInstanceId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals("12345", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).orElse(null));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String processInstanceId = "67890";
        flowableAdaptor.setProcessInstanceId(2L, processInstanceId);
        assertTrue(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 3L;
        String processDefinitionId = "def123";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals("def123", flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(flowInstanceId).orElse(null));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String processDefinitionId = "def456";
        flowableAdaptor.setProcessDefinitionId(4L, processDefinitionId);
        assertTrue(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        assertNotNull(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(5L).orElse(null));
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        Long instanceId = 6L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT;
        assertEquals(1, flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType).size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "task123";
        Long flowInstanceId = 7L;
        assertTrue(flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "approval123";
        Long flowInstanceId = 8L;
        assertTrue(flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String name = "approvalName";
        Long flowInstanceId = 9L;
        assertTrue(flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        String activityId = "gateway123";
        Long flowInstanceId = 10L;
        assertTrue(flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId).isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        // Test implementation based on database query
        FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("activity123", "Element Name", FlowableElementType.ELEMENT);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        assertFalse(flowableAdaptor.getFlowableElementByType(nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType()).isEmpty());
    }

}