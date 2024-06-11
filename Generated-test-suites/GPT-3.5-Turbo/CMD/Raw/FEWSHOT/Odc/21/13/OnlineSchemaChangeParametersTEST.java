package com.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test ADD column1 INT;");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setHostname("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "schema");

        assertNotNull(subTasks);
        assertEquals(1, subTasks.size());
        assertEquals("ALTER TABLE test ADD column1 INT;", subTasks.get(0).getSqlsToBeExecuted().get(0));
    }

}