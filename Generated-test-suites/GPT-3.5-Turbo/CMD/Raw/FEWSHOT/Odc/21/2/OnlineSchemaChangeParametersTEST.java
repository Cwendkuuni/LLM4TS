package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        // Test when error strategy is CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        // Test when error strategy is NOT CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        // Add test cases for generateSubTaskParameters method here
        // Ensure to cover various scenarios and edge cases
    }

}