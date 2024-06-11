package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeSqlType sqlType;
    private String schema;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        sqlType = OnlineSchemaChangeSqlType.ALTER;
        schema = "testSchema";
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());
    }

    @Test
    public void testSetSqlType() {
        OnlineSchemaChangeSqlType newSqlType = OnlineSchemaChangeSqlType.CREATE;
        parameters.setSqlType(newSqlType);
        assertEquals(newSqlType, parameters.getSqlType());
    }

    @Test
    public void testGetSqlContent() {
        String sqlContent = "testSqlContent";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testSetSqlContent() {
        String newSqlContent = "testNewSqlContent";
        parameters.setSqlContent(newSqlContent);
        assertEquals(newSqlContent, parameters.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());
    }

    @Test
    public void testSetErrorStrategy() {
        TaskErrorStrategy newErrorStrategy = TaskErrorStrategy.ABORT;
        parameters.setErrorStrategy(newErrorStrategy);
        assertEquals(newErrorStrategy, parameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        int lockTableTimeOutSeconds = 10;
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSetLockTableTimeOutSeconds() {
        int newLockTableTimeOutSeconds = 20;
        parameters.setLockTableTimeOutSeconds(newLockTableTimeOutSeconds);
        assertEquals(newLockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        int swapTableNameRetryTimes = 3;
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testSetSwapTableNameRetryTimes() {
        int newSwapTableNameRetryTimes = 5;
        parameters.setSwapTableNameRetryTimes(newSwapTableNameRetryTimes);
        assertEquals(newSwapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testSetOriginTableCleanStrategy() {
        OriginTableCleanStrategy newOriginTableCleanStrategy = OriginTableCleanStrategy.TRUNCATE;
        parameters.setOriginTableCleanStrategy(newOriginTableCleanStrategy);
        assertEquals(newOriginTableCleanStrategy, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetDelimiter() {
        String delimiter = ";";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());
    }

    @Test
    public void testSetDelimiter() {
        String newDelimiter = ",";
        parameters.setDelimiter(newDelimiter);
        assertEquals(newDelimiter, parameters.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        List<String> lockUsers = new ArrayList<>();
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testSetLockUsers() {
        List<String> newLockUsers = new ArrayList<>();
        newLockUsers.add("testUser");
        parameters.setLockUsers(newLockUsers);
        assertEquals(newLockUsers, parameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        SwapTableType swapTableType = SwapTableType.SWAP;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testSetSwapTableType() {
        SwapTableType newSwapTableType = SwapTableType.REPLACE;
        parameters.setSwapTableType(newSwapSwapTableType);
        assertEquals(newSwapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertTrue(parameters.isContinueOnError());

        errorStrategy = TaskErrorStrategy.ABORT;
        parameters.setErrorStrategy(errorStrategy);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        when(connectionConfig.getDialectType()).thenReturn("testDialectType");
        String sqlContent = "testSqlContent;testSqlContent2";
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        parameters.setSwapTableType(SwapTableType.SWAP);

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedParameters = new LinkedHashMap<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter1.setDatabaseName("testDatabaseName1");
        taskParameter1.setOriginTableName("testOriginTableName1");
        taskParameter1.setNewTableName("testNewTableName1");
        expectedParameters.put(new TableIdentity("testDatabaseName1", "testOriginTableName1"), taskParameter1);

        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter2.setDatabaseName("testDatabaseName2");
        taskParameter2.setOriginTableName("testOriginTableName2");
        taskParameter2.setNewTableName("testNewTableName2");
        expectedParameters.put(new TableIdentity("testDatabaseName2", "testOriginTableName2"), taskParameter2);

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, ";");

        when(connectionConfig.getDialectType()).thenReturn("testDialectType");
        when(connectionConfig.getSchema()).thenReturn(schema);
        when(connectionConfig.getOceanbaseUrl()).thenReturn("testOceanbaseUrl");

        List<OnlineSchemaChangeScheduleTaskParameters> actualParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedParameters.size(), actualParameters.size());
        for (OnlineSchemaChangeScheduleTaskParameters actualParameter : actualParameters) {
            TableIdentity key = new TableIdentity(actualParameter.getDatabaseName(), actualParameter.getOriginTableName());
            OnlineSchemaChangeScheduleTaskParameters expectedParameter = expectedParameters.get(key);
            assertEquals(expectedParameter, actualParameter);
        }
    }
}