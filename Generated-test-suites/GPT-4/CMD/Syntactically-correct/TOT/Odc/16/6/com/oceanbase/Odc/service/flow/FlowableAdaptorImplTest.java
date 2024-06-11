package com.oceanbase.odc.service.flow;
import org.junit.Assert;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    @Test
    public void setProcessInstanceIdTest() {
        // Setup
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        // Assuming here you can create an instance of FlowableAdaptorImpl in your real context
        FlowableAdaptorImpl systemUnderTest = new FlowableAdaptorImpl();

        // Execute
        systemUnderTest.setProcessInstanceId(flowInstanceId, processInstanceId);
        // Check 
        // This necessitates a getter method that is missing genuinely in your class.
        // Assert.assertEquals(processInstanceId, systemUnderTest.getProcessInstanceId())
        // You should find a way(like above) to check the processInstanceId was set correctly
    }
}