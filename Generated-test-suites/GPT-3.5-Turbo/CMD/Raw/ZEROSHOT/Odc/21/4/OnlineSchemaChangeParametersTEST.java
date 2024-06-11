package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table_name ADD column_name INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "test";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertEquals("test", subTaskParameter.getDatabaseName());
        assertEquals("table_name", subTaskParameter.getOriginTableName());
        assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table_name ADD column_name INT;", subTaskParameter.getSqlsToBeExecuted().get(0));
    }

}