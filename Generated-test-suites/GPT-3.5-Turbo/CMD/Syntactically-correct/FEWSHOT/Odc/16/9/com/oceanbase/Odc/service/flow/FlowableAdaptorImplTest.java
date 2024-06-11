package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test case for setting process instance ID
        Long flowInstanceId = 1L;
        String processInstanceId = "process-1";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for getting flow instance ID by process instance ID
        String processInstanceId = "process-2";
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test case for setting process definition ID
        Long flowInstanceId = 2L;
        String processDefinitionId = "definition-1";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Test case for getting flow instance ID by process definition ID
        String processDefinitionId = "definition-2";
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Test case for getting process instance ID by flow instance ID
        Long flowInstanceId = 3L;
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test case for getting flowable elements by type
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT;
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertNotNull(flowableElements);
        assertEquals(0, flowableElements.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test case for getting task instance by activity ID
        String activityId = "activity-1";
        Long flowInstanceId = 1L;
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test case for getting approval instance by activity ID
        String activityId = "activity-2";
        Long flowInstanceId = 2L;
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Test case for getting approval instance by name
        String name = "approval-1";
        Long flowInstanceId = 3L;
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test case for getting gateway instance by activity ID
        String activityId = "gateway-1";
        Long flowInstanceId = 1L;
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        // Test case for setting flowable element
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("1", "element-1", FlowableElementType.ELEMENT);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        assertNotNull(flowableAdaptor);
    }

    @Test
    public void testInnerConvert() {
        // Test case for inner conversion function
        Optional<Object> optional = Optional.of("test");
        Optional<Object> result = flowableAdaptor.innerConvert(optional, flowableAdaptor, (entity, adaptor) -> entity);
        assertTrue(result.isPresent());
        assertEquals("test", result.get());
    }
}