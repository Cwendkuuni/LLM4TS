package com.oceanbase.odc.service.flow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long flowInstanceId = 2L;
        String processInstanceId = "54321";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 3L;
        String processDefinitionId = "67890";
        
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Long flowInstanceId = 4L;
        String processDefinitionId = "09876";
        
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 5L;
        String processInstanceId = "13579";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    // Additional test cases for other methods can be added here

}