package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import org.junit.Test;

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
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        String schema = "test_schema";

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD column1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("test_table", subTaskParam.getOriginTableName());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table ADD column1 INT;", subTaskParam.getSqlsToBeExecuted().get(0));
        // Add more assertions as needed
    }

}