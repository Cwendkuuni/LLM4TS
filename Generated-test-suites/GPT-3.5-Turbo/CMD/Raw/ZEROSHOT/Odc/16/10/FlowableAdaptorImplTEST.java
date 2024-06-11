package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    
    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Create mock FlowInstanceEntity
        FlowInstanceEntity mockFlowInstanceEntity = mock(FlowInstanceEntity.class);
        when(mockFlowInstanceEntity.getId()).thenReturn(1L);
        
        // Test setProcessInstanceId
        flowableAdaptor.setProcessInstanceId(1L, "processInstanceId");
        
        Optional<String> processInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);
        assertTrue(processInstanceId.isPresent());
        assertEquals("processInstanceId", processInstanceId.get());
    }
    
    @Test
    public void testSetProcessDefinitionId() {
        // Create mock FlowInstanceEntity
        FlowInstanceEntity mockFlowInstanceEntity = mock(FlowInstanceEntity.class);
        when(mockFlowInstanceEntity.getId()).thenReturn(1L);
        
        // Test setProcessDefinitionId
        flowableAdaptor.setProcessDefinitionId(1L, "processDefinitionId");
        
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(flowInstanceId.isPresent());
        assertEquals(1L, flowInstanceId.get().longValue());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test getFlowableElementByType
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.ELEMENT);
        assertNotNull(flowableElements);
        assertFalse(flowableElements.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test getTaskInstanceByActivityId
        Optional<FlowTaskInstance> taskInstance = flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L);
        assertTrue(taskInstance.isPresent());
        assertNotNull(taskInstance.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test getApprovalInstanceByActivityId
        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByActivityId("activityId", 1L);
        assertTrue(approvalInstance.isPresent());
        assertNotNull(approvalInstance.get());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Test getApprovalInstanceByName
        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByName("name", 1L);
        assertTrue(approvalInstance.isPresent());
        assertNotNull(approvalInstance.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test getGatewayInstanceByActivityId
        Optional<FlowGatewayInstance> gatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId("activityId", 1L);
        assertTrue(gatewayInstance.isPresent());
        assertNotNull(gatewayInstance.get());
    }

}