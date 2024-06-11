package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
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
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertEquals(0, parameters.getLockTableTimeOutSeconds());
        assertEquals(0, parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertSame(Character.toString(System.property("line.separator")), parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());

        OnlineSchemaChangeParameters copy = new OnlineSchemaChangeParameters(parameters);
        assertEquals(parameters, copy);

        OnlineSchemaChangeParameters modified = new OnlineSchemaChangeParameters();
        modified.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        modified.setSqlContent("create table test (id int primary key);");
        modified.setErrorStrategy(TaskErrorStrategy.ABORT);
        modified.setLockTableTimeOutSeconds(30);
        modified.setSwapTableNameRetryTimes(5);
        modified.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        modified.setDelimiter("\r\n");
        modified.setLockUsers(new ArrayList<>());
        modified.setSwapTableType(SwapTableType.READ_WRITE);

        assertEquals(OnlineSchemaChangeSqlType.CREATE, modified.getSqlType());
        assertEquals("create table test (id int primary key);", modified.getSqlContent());
        assertEquals(TaskErrorStrategy.ABORT, modified.getErrorStrategy());
        assertEquals(30, modified.getLockTableTimeOutSeconds());
        assertEquals(5, modified.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, modified.getOriginTableCleanStrategy());
        assertEquals("\r\n", modified.getDelimiter());
        assertTrue(modified.getLockUsers().isEmpty());
        assertEquals(SwapTableType.READ_WRITE, modified.getSwapTableType());

        assertFalse(modified.isContinueOnError());

        OnlineSchemaChangeParameters empty = new OnlineSchemaChangeParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = empty.generateSubTaskParameters(null, null);
        assertTrue(subTaskParameters.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("alter table test add column id2 int;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.READ_ONLY);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("test", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals("alter table test add column id2 int;\r\n", taskParameter.getSqlContentToBeExecuted().get(0));
        assertEquals("test_schema.test.id2", taskParameter.getNewTableName());
        assertEquals(List.of("alter table test add column id2 int;"), taskParameter.getSqlsToBeExecuted());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(null);
        parameters.setSqlContent("alter table test add column id2 int;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.READ_ONLY);

        ConnectionConfig connectionConfig = null;
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
    }
}