package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "test_schema";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        
        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParameters.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("test", subTaskParam.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParam.getSqlType());
    }
}