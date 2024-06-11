package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
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
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, params.getSqlType());

        params.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
        assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", params.getSqlContent());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertEquals(TaskErrorStrategy.ABORT, params.getErrorStrategy());

        params.setLockTableTimeOutSeconds(10);
        assertEquals(10, params.getLockTableTimeOutSeconds().intValue());

        params.setSwapTableNameRetryTimes(5);
        assertEquals(5, params.getSwapTableNameRetryTimes().intValue());

        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());

        params.setDelimiter("--");
        assertEquals("--", params.getDelimiter());

        params.setLockUsers(Arrays.asList("user1", "user2"));
        assertEquals(Arrays.asList("user1", "user2"), params.getLockUsers());

        params.setSwapTableType(SwapTableType.OLD_TO_NEW);
        assertEquals(SwapTableType.OLD_TO_NEW, params.getSwapTableType());

        params.setSqlType(null);
        assertNull(params.getSqlType());

        params.setSqlContent(null);
        assertNull(params.getSqlContent());

        params.setErrorStrategy(null);
        assertNull(params.getErrorStrategy());

        params.setLockTableTimeOutSeconds(null);
        assertNull(params.getLockTableTimeOutSeconds());

        params.setSwapTableNameRetryTimes(null);
        assertNull(params.getSwapTableNameRetryTimes());

        params.setOriginTableCleanStrategy(null);
        assertNull(params.getOriginTableCleanStrategy());

        params.setDelimiter(null);
        assertNull(params.getDelimiter());

        params.setLockUsers(null);
        assertNull(params.getLockUsers());

        params.setSwapTableType(null);
        assertNull(params.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertFalse(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(null);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersNullConnectionConfig() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
        assertNull(params.generateSubTaskParameters(null, "test_schema"));
    }

    @Test
    public void testGenerateSubTaskParametersEmptyList() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertTrue(taskParameters.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParametersValid() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableNameRetryTimes(5);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter("--");
        params.setLockUsers(Arrays.asList("user1", "user2"));
        params.setSwapTableType(SwapTableType.OLD_TO_NEW);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameter = taskParameters.get(0);
        assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", firstTaskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, firstTaskParameter.getSqlType());
        assertEquals(TaskErrorStrategy.CONTINUE, firstTaskParameter.getErrorStrategy());
        assertEquals(10, firstTaskParameter.getLockTableTimeOutSeconds().intValue());
        assertEquals(5, firstTaskParameter.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.DROP, firstTaskParameter.getOriginTableCleanStrategy());
        assertEquals("--", firstTaskParameter.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), firstTaskParameter.getLockUsers());
        assertEquals(SwapTableType.OLD_TO_NEW, firstTaskParameter.getSwapTableType());
        assertEquals(Collections.singletonList("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);"), firstTaskParameter.getSqlsToBeExecuted());
    }
}