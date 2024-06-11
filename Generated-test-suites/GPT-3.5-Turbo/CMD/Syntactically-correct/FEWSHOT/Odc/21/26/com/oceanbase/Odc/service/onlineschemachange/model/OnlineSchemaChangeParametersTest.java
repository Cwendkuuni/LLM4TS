package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params1 = new OnlineSchemaChangeParameters();
        params1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params1.isContinueOnError());

        OnlineSchemaChangeParameters params2 = new OnlineSchemaChangeParameters();
        params2.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params2.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, subTaskParams.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("schema", subTaskParam.getDatabaseName());
        assertEquals("table1", subTaskParam.getOriginTableName());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT", subTaskParam.getSqlsToBeExecuted().get(0));
    }
}