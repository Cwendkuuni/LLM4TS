package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY, name VARCHAR(100)); DROP TABLE foo;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema1");
        assertEquals(2, subTaskParameters.size());

        for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
            if (subTaskParameter.getDatabaseName().equals("schema1") && subTaskParameter.getOriginTableName().equals("foo")) {
                assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
            } else {
                fail("Unexpected subtask");
            }
        }

        // Check with invalid SQL content
        parameters.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY, name VARCHAR(100)");
        parameters.generateSubTaskParameters(connectionConfig, "schema1");
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        // Test setters and getters
        parameters.setSqlType(OnlineSchemaChangeSqlType.DROP);
        assertEquals(OnlineSchemaChangeSqlType.DROP, parameters.getSqlType());

        String sqlContent = "DROP TABLE foo;";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(TaskErrorStrategy.STOP, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(30);
        assertEquals(Integer.valueOf(30), parameters.getLockTableTimeOutSeconds());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.IMMEDIATE);
        assertEquals(OriginTableCleanStrategy.IMMEDIATE, parameters.getOriginTableCleanStrategy());

        String delimiter = "|";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        assertEquals(2, parameters.getLockUsers().size());

        parameters.setSwapTableType(SwapTableType.TEMPORARY);
        assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());
    }
}