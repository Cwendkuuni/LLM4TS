/*
 * Test class for OnlineSchemaChangeParameters
 */
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertNotNull(subTaskParameter);
        assertEquals("test_schema", subTaskParameter.getDatabaseName());
        assertEquals("test", subTaskParameter.getOriginTableName());
        assertEquals("new_column", subTaskParameter.getNewTableName());
        assertEquals("ALTER TABLE test ADD COLUMN new_column INT;", subTaskParameter.getSqlsToBeExecuted().get(0));
    }
}