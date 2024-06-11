package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
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
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
    }

    @Test
    public void testOnlineSchemaChangeParametersWithValues() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "sample sql content";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 60;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP_AND_RECREATE;
        String delimiter = ";";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.BACKUP;

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(errorStrategy);
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        parameters.setDelimiter(delimiter);
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(swapTableType);

        assertEquals(sqlType, parameters.getSqlType());
        assertEquals(sqlContent, parameters.getSqlContent());
        assertEquals(errorStrategy, parameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, parameters.getDelimiter());
        assertEquals(lockUsers, parameters.getLockUsers());
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("sample sql content;alter table sample_table add column new_column int;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(60);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.BACKUP);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameters = subTaskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters secondTaskParameters = subTaskParameters.get(1);

        assertEquals("sample_table", firstTaskParameters.getOriginTableName());
        assertEquals("sample sql content", firstTaskParameters.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, firstTaskParameters.getSqlType());

        assertEquals("sample_table", secondTaskParameters.getOriginTableName());
        assertEquals("alter table sample_table add column new_column int;", secondTaskParameters.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, secondTaskParameters.getSqlType());
        assertTrue(firstTaskParameters.getSqlsToBeExecuted().isEmpty());
        assertTrue(secondTaskParameters.getSqlsToBeExecuted().size() > 0);
    }

    @Test
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("sample sql content;alter table sample_table add column new_column int;");
        parameters.setErrorStrategy(null);
        parameters.setLockTableTimeOutSeconds(60);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.BACKUP);

        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);

        assertFalse(parameters.isContinueOnError());
    }
}