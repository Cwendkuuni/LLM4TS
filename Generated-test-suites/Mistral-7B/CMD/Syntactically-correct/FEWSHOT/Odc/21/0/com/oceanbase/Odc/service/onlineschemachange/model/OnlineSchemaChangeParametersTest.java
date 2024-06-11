package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testParametersWithDefaultValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.RETRY, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_RECREATE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.TEMP, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testParametersWithNonDefaultValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("\t");
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(SwapTableType.SHARED);
        parameters.setContinueOnError(true);

        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("CREATE TABLE foo (id INT PRIMARY KEY);", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
        assertEquals("\t", parameters.getDelimiter());
        assertSame(lockUsers, parameters.getLockUsers());
        assertEquals(SwapTableType.SHARED, parameters.getSwapTableType());
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("\t");
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(SwapTableType.SHARED);
        parameters.setContinueOnError(true);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        assertEquals("CREATE TABLE foo (id INT PRIMARY KEY);", taskParameters.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameters.getSqlType());
        assertSame(connectionConfig, taskParameters.getConnectionConfig());
        assertSame("test_schema", taskParameters.getSchemaName());
        assertEquals(Arrays.asList("test_schema.foo"), taskParameters.getTablesToBeAltered());
        assertEquals(1, taskParameters.getSqlsToBeExecuted().size());
        assertEquals("CREATE TABLE foo (id INT PRIMARY KEY);", taskParameters.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY);");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}