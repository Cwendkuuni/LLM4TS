package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

        OnlineSchemaChangeParameters parametersWithValues = new OnlineSchemaChangeParameters();
        parametersWithValues.setSqlType(OnlineSchemaChangeSqlType.ADD);
        parametersWithValues.setSqlContent("sqlContent");
        parametersWithValues.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parametersWithValues.setLockTableTimeOutSeconds(5);
        parametersWithValues.setSwapTableNameRetryTimes(2);
        parametersWithValues.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_DATA);
        parametersWithValues.setDelimiter(";");
        parametersWithValues.setLockUsers(Arrays.asList("user1", "user2"));
        parametersWithValues.setSwapTableType(SwapTableType.IN_PLACE);

        assertEquals(OnlineSchemaChangeSqlType.ADD, parametersWithValues.getSqlType());
        assertEquals("sqlContent", parametersWithValues.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parametersWithValues.getErrorStrategy());
        assertEquals(Integer.valueOf(5), parametersWithValues.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(2), parametersWithValues.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DELETE_DATA, parametersWithValues.getOriginTableCleanStrategy());
        assertEquals(";", parametersWithValues.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parametersWithValues.getLockUsers());
        assertEquals(SwapTableType.IN_PLACE, parametersWithValues.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        OnlineSchemaChangeParameters parametersWithContinueErrorStrategy = new OnlineSchemaChangeParameters();
        parametersWithContinueErrorStrategy.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parametersWithContinueErrorStrategy.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters1.setDatabaseName("databaseName1");
        taskParameters1.setOriginTableName("tableName1");
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters2.setDatabaseName("databaseName2");
        taskParameters2.setOriginTableName("tableName2");
        expectedTaskParameters.add(taskParameters1);
        expectedTaskParameters.add(taskParameters2);

        String sqlContent = "sqlContent;sqlContent2";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableNameRetryTimes(2);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_DATA);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        List<String> sqls = Arrays.asList("sql1;sql1_1", "sql2;sql2_1");
        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedTaskParameters.size(), actualTaskParameters.size());
        for (int i = 0; i < expectedTaskParameters.size(); i++) {
            OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter = expectedTaskParameters.get(i);
            OnlineSchemaChangeScheduleTaskParameters actualTaskParameter = actualTaskParameters.get(i);
            assertSame(expectedTaskParameter, actualTaskParameter);
        }
    }
}