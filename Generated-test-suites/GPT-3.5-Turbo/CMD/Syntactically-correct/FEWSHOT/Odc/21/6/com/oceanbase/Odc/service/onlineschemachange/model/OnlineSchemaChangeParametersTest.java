package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        
        String schema = "schema1";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);
        
        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());
        
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("schema1", subTaskParam.getDatabaseName());
        assertEquals("table1", subTaskParam.getOriginTableName());
    }

}