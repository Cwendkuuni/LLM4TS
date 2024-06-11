package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());

        OnlineSchemaChangeParameters paramsWithValues = new OnlineSchemaChangeParameters();
        paramsWithValues.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        paramsWithValues.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
        paramsWithValues.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        paramsWithValues.setLockTableTimeOutSeconds(30);
        paramsWithValues.setSwapTableNameRetryTimes(5);
        paramsWithValues.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        paramsWithValues.setDelimiter(";");
        paramsWithValues.setLockUsers(new ArrayList<>());
        paramsWithValues.setSwapTableType(SwapTableType.ALLOW_WRITE);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, paramsWithValues.getSqlType());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT;\r\n", paramsWithValues.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, paramsWithValues.getErrorStrategy());
        assertEquals(30, paramsWithValues.getLockTableTimeOutSeconds().intValue());
        assertEquals(5, paramsWithValues.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.DROP, paramsWithValues.getOriginTableCleanStrategy());
        assertEquals(";", paramsWithValues.getDelimiter());
        assertTrue(paramsWithValues.getLockUsers().isEmpty());
        assertEquals(SwapTableType.ALLOW_WRITE, paramsWithValues.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertFalse(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(255));");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(30);
        params.setSwapTableNameRetryTimes(5);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>());
        params.setSwapTableType(SwapTableType.ALLOW_WRITE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
                schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(255));\r\n", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertSame(params, taskParameter.getParameters());

        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(1, sqlsToBeExecuted.size());
        assertEquals("CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(255));\r\n", sqlsToBeExecuted.get(0));
    }
}