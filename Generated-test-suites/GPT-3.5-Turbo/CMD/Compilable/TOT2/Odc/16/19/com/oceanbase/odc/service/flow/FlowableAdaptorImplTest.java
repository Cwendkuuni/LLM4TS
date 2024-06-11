package com.oceanbase.odc.service.flow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {
    
    private FlowableAdaptorImpl flowableAdaptor;
    
    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        // Mocking dependencies as required
    }
    
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "PID_1";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<Long> resultFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(resultFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, resultFlowInstanceId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_NotFound() {
        String processInstanceId = "PID_100";
        
        Optional<Long> resultFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertFalse(resultFlowInstanceId.isPresent());
    }

    // Add more test methods for other public methods in FlowableAdaptorImpl
    
}