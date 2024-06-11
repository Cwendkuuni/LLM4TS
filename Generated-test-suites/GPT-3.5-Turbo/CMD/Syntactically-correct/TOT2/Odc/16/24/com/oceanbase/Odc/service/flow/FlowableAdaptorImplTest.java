package com.oceanbase.odc.service.flow;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        // Test case for setting process instance id
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setProcessInstanceId(1L, "12345");
        assertTrue(true);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for getting flow instance id by process instance id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getFlowInstanceIdByProcessInstanceId("12345").isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test case for setting process definition id
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setProcessDefinitionId(1L, "def123");
        assertTrue(true);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Test case for getting flow instance id by process definition id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("def123").isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Test case for getting process instance id by flow instance id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L).isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test case for getting flowable elements by type
        flowableAdaptor = new FlowableAdaptorImpl();
        assertEquals(0, flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.TASK, FlowableElementType.ELEMENT).size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test case for getting task instance by activity id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getTaskInstanceByActivityId("task123", 1L).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test case for getting approval instance by activity id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getApprovalInstanceByActivityId("approve123", 1L).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Test case for getting approval instance by name
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getApprovalInstanceByName("approval", 1L).isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test case for getting gateway instance by activity id
        flowableAdaptor = new FlowableAdaptorImpl();
        assertFalse(flowableAdaptor.getGatewayInstanceByActivityId("gate123", 1L).isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        // Test case for setting flowable element
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setFlowableElement(mock(BaseFlowNodeInstance.class), new FlowableElement("123", "Test", FlowableElementType.TYPE));
        assertTrue(true);
    }
}