package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
    }

    @Test
    public void testSetSqlType() {
        OnlineSchemaChangeSqlType expectedSqlType = OnlineSchemaChangeSqlType.DROP;
        parameters.setSqlType(expectedSqlType);
        assertEquals(expectedSqlType, parameters.getSqlType());
    }

    @Test
    public void testGetSqlContent() {
        String sqlContent = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testSetSqlContent() {
        String expectedSqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        parameters.setSqlContent(expectedSqlContent);
        assertEquals(expectedSqlContent, parameters.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }

    @Test
    public void testSetErrorStrategy() {
        TaskErrorStrategy expectedErrorStrategy = TaskErrorStrategy.ABORT;
        parameters.setErrorStrategy(expectedErrorStrategy);
        assertEquals(expectedErrorStrategy, parameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        parameters.setLockTableTimeOutSeconds(30);
        assertEquals(30, parameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testSetLockTableTimeOutSeconds() {
        int expectedLockTableTimeOutSeconds = 10;
        parameters.setLockTableTimeOutSeconds(expectedLockTableTimeOutSeconds);
        assertEquals(expectedLockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testSetSwapTableNameRetryTimes() {
        int expectedSwapTableNameRetryTimes = 5;
        parameters.setSwapTableNameRetryTimes(expectedSwapTableNameRetryTimes);
        assertEquals(expectedSwapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testSetOriginTableCleanStrategy() {
        OriginTableCleanStrategy expectedOriginTableCleanStrategy = OriginTableCleanStrategy.TRUNCATE;
        parameters.setOriginTableCleanStrategy(expectedOriginTableCleanStrategy);
        assertEquals(expectedOriginTableCleanStrategy, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetDelimiter() {
        parameters.setDelimiter("|");
        assertEquals("|", parameters.getDelimiter());
    }

    @Test
    public void testSetDelimiter() {
        String expectedDelimiter = ";";
        parameters.setDelimiter(expectedDelimiter);
        assertEquals(expectedDelimiter, parameters.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        assertTrue(parameters.getLockUsers().containsAll(lockUsers));
    }

    @Test
    public void testSetLockUsers() {
        List<String> expectedLockUsers = Arrays.asList("user3", "user4");
        parameters.setLockUsers(expectedLockUsers);
        assertSame(expectedLockUsers, parameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        parameters.setSwapTableType(SwapTableType.MATERIALIZED);
        assertEquals(SwapTableType.MATERIALIZED, parameters.getSwapTableType());
    }

    @Test
    public void testSetSwapTableType() {
        SwapTableType expectedSwapTableType = SwapTableType.MERGED;
        parameters.setSwapTableType(expectedSwapTableType);
        assertEquals(expectedSwapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorWithAbortStrategy() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.ABORT;
        parameters.setErrorStrategy(errorStrategy);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "test_schema";
        String sqlContent = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;

        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.MATERIALIZED);

        List<String> sqls = Arrays.asList(sqlContent);
        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("test_schema");
        taskParameter.setOriginTableName("test_table");
        taskParameter.setNewTableName("test_table_new");
        expectedTaskParameters.put(new TableIdentity("test_schema", "test_table"), taskParameter);
        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedTaskParameters.size(), actualTaskParameters.size());
        assertEquals(expectedTaskParameters, actualTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        String schema = "test_schema";
        String sqlContent = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;

        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.MATERIALIZED);

        List<String> sqls = Arrays.asList("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
        throw new RuntimeException("Failed to generate subtasks");

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
    }
}