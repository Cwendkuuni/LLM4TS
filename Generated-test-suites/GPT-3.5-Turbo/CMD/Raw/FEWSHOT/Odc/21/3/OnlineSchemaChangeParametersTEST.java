package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column VARCHAR(100);");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
        assertEquals("schema", subTaskParameters.get(0).getSchemaName());
        assertEquals("test_table", subTaskParameters.get(0).getOriginTableName());
        assertEquals("new_column", subTaskParameters.get(0).getNewColumnName());
        assertEquals("VARCHAR(100)", subTaskParameters.get(0).getNewColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters.get(0).getSqlType());
        assertEquals(Arrays.asList("ALTER TABLE test_table ADD COLUMN new_column VARCHAR(100);"), subTaskParameters.get(0).getSqlsToBeExecuted());
    }

}