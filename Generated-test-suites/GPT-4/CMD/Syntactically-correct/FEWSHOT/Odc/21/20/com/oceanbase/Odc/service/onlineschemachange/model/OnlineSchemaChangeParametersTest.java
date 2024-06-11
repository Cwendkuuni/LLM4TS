package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters objUnderTest = new OnlineSchemaChangeParameters();
        objUnderTest.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(objUnderTest.isContinueOnError());

        objUnderTest.setErrorStrategy(TaskErrorStrategy.FAIL_FAST);
        assertFalse(objUnderTest.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters objUnderTest = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("Mysql");
        objUnderTest.setSqlContent("CREATE TABLE test_table ( id int )");
        objUnderTest.setDelimiter(";");
        objUnderTest.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        objUnderTest.setSwapTableNameRetryTimes(3);

        List<OnlineSchemaChangeScheduleTaskParameters> parameters = objUnderTest.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(parameters);
        assertEquals(1, parameters.size());
        assertEquals("test_schema", parameters.get(0).getDatabaseName());
    }
}