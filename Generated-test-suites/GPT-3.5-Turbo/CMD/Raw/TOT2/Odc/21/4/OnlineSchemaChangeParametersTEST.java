package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test case for generateSubTaskParameters method
        // Edge case: empty SQL content
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:mysql://localhost/test", "user", "password");
        String schema = "test_schema";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, subTaskParameters.size());
    }

    // Add more test cases for other public methods if needed

}