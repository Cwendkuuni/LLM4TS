package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test setting process instance Id
        flowableAdaptor.setProcessInstanceId(1L, "process123");
        assertEquals("process123", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L).get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test getting flow instance Id by process instance Id
        flowableAdaptor.setProcessInstanceId(2L, "process456");
        assertEquals(2L, flowableAdaptor.getFlowInstanceIdByProcessInstanceId("process456").get().longValue());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test setting process definition Id
        flowableAdaptor.setProcessDefinitionId(3L, "definition123");
        assertEquals("definition123", flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(3L).get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Test getting flow instance Id by process definition Id
        flowableAdaptor.setProcessDefinitionId(4L, "definition456");
        assertEquals(4L, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("definition456").get().longValue());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Test getting process instance Id by flow instance Id
        flowableAdaptor.setProcessInstanceId(5L, "process789");
        assertEquals("process789", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(5L).get());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test getting flowable elements by type
        assertNotNull(flowableAdaptor.getFlowableElementByType(6L, FlowNodeType.TASK, FlowableElementType.ELEMENT));
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test getting task instance by activity Id
        assertFalse(flowableAdaptor.getTaskInstanceByActivityId("task123", 7L).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test getting approval instance by activity Id
        assertFalse(flowableAdaptor.getApprovalInstanceByActivityId("approval123", 8L).isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Test getting approval instance by name
        assertFalse(flowableAdaptor.getApprovalInstanceByName("ApprovalTask", 9L).isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test getting gateway instance by activity Id
        assertFalse(flowableAdaptor.getGatewayInstanceByActivityId("gateway123", 10L).isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        // Test setting flowable element
        // Prepare the BaseFlowNodeInstance and FlowableElement
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(11L);
        nodeInstance.setNodeType(FlowNodeType.TASK);
        nodeInstance.setFlowInstanceId(12L);
        FlowableElement flowableElement = new FlowableElement("activity123", "Task", FlowableElementType.ELEMENT);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        assertTrue(flowableAdaptor.getFlowableElementByType(11L, FlowNodeType.TASK, FlowableElementType.ELEMENT)
                .stream()
                .anyMatch(element -> element.getActivityId().equals("activity123")));
    }
}