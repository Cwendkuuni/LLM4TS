package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

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
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertNull(taskParameter.getNewTableName());
        assertEquals(30, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.IN_PLACE, taskParameter.getSwapTableType());

        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(1, sqlsToBeExecuted.size());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", sqlsToBeExecuted.get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
            assertFalse(true);
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithAlterStatement() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("ALTER TABLE test ADD COLUMN new_column INT;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertNull(taskParameter.getNewTableName());
        assertEquals(30, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP, taskParameter.getSwapTableType());

        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(1, sqlsToBeExecuted.size());
        String newAlterStmt = "ALTER TABLE test RENAME TO test_renamed;";
        assertEquals(newAlterStmt, sqlsToBeExecuted.get(0));

        String newRenamedTableName = "test_renamed";
        OnlineSchemaChangeScheduleTaskParameters newTaskParameter = subTaskParameters.get(0);
        newTaskParameter.setNewTableName(newRenamedTableName);
        List<String> newSqlsToBeExecuted = newTaskParameter.getSqlsToBeExecuted();
        assertEquals(2, newSqlsToBeExecuted.size());
        assertEquals("ALTER TABLE test ADD COLUMN new_column INT;", newSqlsToBeExecuted.get(0));
        assertEquals("ALTER TABLE test_renamed RENAME TO test;", newSqlsToBeExecuted.get(1));
    }

    @Test
    public void testGenerateSubTaskParametersWithMultipleStatements() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);\nCREATE TABLE test2 (id INT PRIMARY KEY);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = subTaskParameters.get(0);
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", taskParameter1.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter1.getSqlType());
        assertNull(taskParameter1.getNewTableName());
        assertEquals(30, taskParameter1.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP, taskParameter1.getSwapTableType());

        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = subTaskParameters.get(1);
        assertEquals("CREATE TABLE test2 (id INT PRIMARY KEY);", taskParameter2.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter2.getSqlType());
        assertNull(taskParameter2.getNewTableName());
        assertEquals(30, taskParameter2.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP, taskParameter2.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptyString() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertTrue(subTaskParameters.isEmpty());
    }
}