package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
        assertEquals(0, parameters.getLockTableTimeOutSeconds().intValue());
        assertEquals(0, parameters.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.NO_CLEAN, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.IN_PLACE, parameters.getSwapTableType());

        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        parameters1.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters1.setSqlContent("create table t(id int) engine=innodb;");
        parameters1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters1.setLockTableTimeOutSeconds(10);
        parameters1.setSwapTableNameRetryTimes(3);
        parameters1.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters1.setDelimiter(":");
        parameters1.setLockUsers(Arrays.asList("user1", "user2"));
        parameters1.setSwapTableType(SwapTableType.RENAME);

        OnlineSchemaChangeParameters parameters2 = new OnlineSchemaChangeParameters();
        parameters2.setSqlType(parameters1.getSqlType());
        parameters2.setSqlContent(parameters1.getSqlContent());
        parameters2.setErrorStrategy(parameters1.getErrorStrategy());
        parameters2.setLockTableTimeOutSeconds(parameters1.getLockTableTimeOutSeconds());
        parameters2.setSwapTableNameRetryTimes(parameters1.getSwapTableNameRetryTimes());
        parameters2.setOriginTableCleanStrategy(parameters1.getOriginTableCleanStrategy());
        parameters2.setDelimiter(parameters1.getDelimiter());
        parameters2.setLockUsers(parameters1.getLockUsers());
        parameters2.setSwapTableType(parameters1.getSwapTableType());

        assertEquals(parameters1, parameters2);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(null);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("create table t(id int) engine=innodb;");
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.NO_CLEAN);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.RENAME);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("create table t(id int) engine=innodb;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertNull(taskParameter.getNewTableName());
        assertSame("test", taskParameter.getDatabaseName());
        assertSame("t", taskParameter.getOriginTableName());
        assertTrue(taskParameter.isDropSourceTableAfterExecution());
        assertEquals(0, taskParameter.getSqlsToBeExecuted().size());

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");
        assertEquals(1, subTaskParameters.size());
        taskParameter = subTaskParameters.get(0);
        assertEquals("alter table t engine=innodb;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertSame("test", taskParameter.getDatabaseName());
        assertSame("t", taskParameter.getOriginTableName());
        assertTrue(taskParameter.isDropSourceTableAfterExecution());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals("alter table t engine=innodb;", taskParameter.getSqlsToBeExecuted().get(0));

        parameters.setSwapTableType(SwapTableType.DROP_AND_CREATE);
        subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");
        assertEquals(2, subTaskParameters.size());
        taskParameter = subTaskParameters.get(0);
        assertEquals("drop table t;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.DROP, taskParameter.getSqlType());
        assertSame("test", taskParameter.getDatabaseName());
        assertSame("t", taskParameter.getOriginTableName());
        assertTrue(taskParameter.isDropSourceTableAfterExecution());
        assertEquals(0, taskParameter.getSqlsToBeExecuted().size());

        taskParameter = subTaskParameters.get(1);
        assertEquals("create table t(id int) engine=innodb;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertSame("test", taskParameter.getDatabaseName());
        assertSame("t", taskParameter.getOriginTableName());
        assertTrue(taskParameter.isDropSourceTableAfterExecution());
        assertEquals(0, taskParameter.getSqlsToBeExecuted().size());
    }
}