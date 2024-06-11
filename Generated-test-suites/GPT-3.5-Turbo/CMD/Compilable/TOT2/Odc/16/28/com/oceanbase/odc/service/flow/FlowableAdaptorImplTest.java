package com.oceanbase.odc.service.flow;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test valid flowInstanceId and processInstanceId
        Long flowInstanceId = 1L;
        String processInstanceId = "123456";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
        
        // Test invalid flowInstanceId
        Long invalidFlowInstanceId = 100L;
        flowableAdaptor.setProcessInstanceId(invalidFlowInstanceId, processInstanceId);
        assertEquals(false, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(invalidFlowInstanceId).isPresent());
        
        // Test invalid processInstanceId
        String invalidProcessInstanceId = "invalidId";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, invalidProcessInstanceId);
        assertEquals(false, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).isPresent());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test valid processInstanceId
        String processInstanceId = "123456";
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(flowInstanceId, flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).get());
        
        // Test invalid processInstanceId
        String invalidProcessInstanceId = "invalidId";
        assertEquals(false, flowableAdaptor.getFlowInstanceIdByProcessInstanceId(invalidProcessInstanceId).isPresent());
    }
    
    // Add more test cases for other methods

}