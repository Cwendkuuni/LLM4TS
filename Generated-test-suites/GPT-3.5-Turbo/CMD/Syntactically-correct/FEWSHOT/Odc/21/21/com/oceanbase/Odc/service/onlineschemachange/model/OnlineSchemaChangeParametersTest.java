package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertTrue(!params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("test_table", subTaskParam.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParam.getSqlType());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT;", subTaskParam.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        Exception exception = null;
        try {
            params.generateSubTaskParameters(null, "test_schema");
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
    }
}