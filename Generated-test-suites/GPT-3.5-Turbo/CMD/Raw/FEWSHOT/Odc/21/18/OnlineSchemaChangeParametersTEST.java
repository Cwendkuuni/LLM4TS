package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
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
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table_name ADD column_name VARCHAR(255);");
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        assertEquals("test_schema", subTaskParameters.get(0).getDatabaseName());
        assertEquals("table_name", subTaskParameters.get(0).getOriginTableName());
        assertEquals("ADD column_name VARCHAR(255);", subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }

}