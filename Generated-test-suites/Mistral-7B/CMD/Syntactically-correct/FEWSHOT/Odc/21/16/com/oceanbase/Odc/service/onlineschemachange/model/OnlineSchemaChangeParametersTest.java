package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertNull(params.getSqlType());
        assertNull(params.getSqlContent());
        assertNull(params.getErrorStrategy());
        assertEquals(0, params.getLockTableTimeOutSeconds());
        assertEquals(0, params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertEquals(";", params.getDelimiter());
        assertNull(params.getLockUsers());
        assertEquals(SwapTableType.SWAP, params.getSwapTableType());
        assertFalse(params.isContinueOnError());

        OnlineSchemaChangeParameters paramsWithAllValues = new OnlineSchemaChangeParameters();
        paramsWithAllValues.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        paramsWithAllValues.setSqlContent("sqlContent");
        paramsWithAllValues.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        paramsWithAllValues.setLockTableTimeOutSeconds(10);
        paramsWithAllValues.setSwapTableNameRetryTimes(3);
        paramsWithAllValues.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_OLD);
        paramsWithAllValues.setDelimiter(";");
        paramsWithAllValues.setLockUsers(Arrays.asList("user1", "user2"));
        paramsWithAllValues.setSwapTableType(SwapTableType.SWAP);
        assertTrue(paramsWithAllValues.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("create table if not exists test_table (id int);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableNameRetryTimes(3);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_OLD);
        params.setDelimiter(";");
        params.setLockUsers(Arrays.asList("user1", "user2"));
        params.setSwapTableType(SwapTableType.SWAP);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        connectionConfig.setDatabase("test_db");
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "test_schema");

        OnlineSchemaChangeScheduleTaskParameters taskParam1 = subTaskParameters.get(0);
        TableIdentity key1 = new TableIdentity("test_db", "test_table");
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParam1 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParam1.setTableIdentity(key1);
        expectedTaskParam1.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        expectedTaskParam1.setSqlContent("create table if not exists test_table (id int);");
        assertEquals(expectedTaskParam1, taskParam1);

        OnlineSchemaChangeScheduleTaskParameters taskParam2 = subTaskParameters.get(1);
        TableIdentity key2 = new TableIdentity("test_db", "test_table");
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParam2 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParam2.setTableIdentity(key2);
        expectedTaskParam2.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        expectedTaskParam2.setSqlsToBeExecuted(new ArrayList<>());
        assertEquals(expectedTaskParam2, taskParam2);

        String newAlterStmt = "ALTER TABLE test_table ADD COLUMN new_column int;";
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParam3 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParam3.setTableIdentity(key1);
        expectedTaskParam3.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<String> sqlsToBeExecuted = new ArrayList<>();
        sqlsToBeExecuted.add(newAlterStmt);
        expectedTaskParam3.setSqlsToBeExecuted(sqlsToBeExecuted);
        assertEquals(expectedTaskParam3, subTaskParameters.get(2));
    }
}