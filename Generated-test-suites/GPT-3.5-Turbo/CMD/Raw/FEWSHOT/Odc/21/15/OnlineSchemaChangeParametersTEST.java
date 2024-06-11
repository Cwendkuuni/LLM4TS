package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParams = subTaskParameters.get(0);
        assertEquals("test_schema", subTaskParams.getDatabaseName());
        assertEquals("test_table", subTaskParams.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParams.getSqlType());
        assertEquals(1, subTaskParams.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT;", subTaskParams.getSqlsToBeExecuted().get(0));
    }
}