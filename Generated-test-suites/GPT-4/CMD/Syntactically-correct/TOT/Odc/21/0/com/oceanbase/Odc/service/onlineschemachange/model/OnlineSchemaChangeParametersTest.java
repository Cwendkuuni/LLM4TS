package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        params.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Setup code goes here. The exact code would depend heavily 
        // on the contents and behavior of your individual classes and objects, 
        // but you might typically set up various Objects here,
        // and perhaps a ConnectionConfig and a schema String..

        ConnectionConfig connectionConfig = new ConnectionConfig(/* mock params */);
        String schema = "testSchema";
        
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent(/* mock params */);
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        //...

        try {
            assertEquals(1, params.generateSubTaskParameters(connectionConfig, schema).size());
            
            // Add more assertions as needed here to validate your results...
            
        } catch (UnexpectedException e) {
            fail("UnexpectedException was thrown");
        }
    }
}