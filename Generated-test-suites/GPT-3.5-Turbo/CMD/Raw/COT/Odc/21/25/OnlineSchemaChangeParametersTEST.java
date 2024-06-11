
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD column test_column");
        ConnectionConfig connectionConfig = new ConnectionConfig("localhost", 3306, "user", "password");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        
        assertEquals("test_schema", subTaskParameters.get(0).getDatabaseName());
        assertEquals("test_table", subTaskParameters.get(0).getOriginTableName());
        assertEquals(1, subTaskParameters.size());
    }

    // Additional test cases for edge cases and exceptions can be added here

}

