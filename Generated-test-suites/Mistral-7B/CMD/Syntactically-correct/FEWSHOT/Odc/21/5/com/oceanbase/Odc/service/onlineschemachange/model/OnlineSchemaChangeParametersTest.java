package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
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
        parametersWithValues.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parametersWithValues.setSqlContent("CREATE TABLE foo (id INT PRIMARY KEY);");
        parametersWithValues.setErrorStrategy(TaskErrorStrategy.STOP);
        parametersWithValues.setLockTableTimeOutSeconds(30);
        parametersWithValues.setSwapTableNameRetryTimes(5);
        parametersWithValues.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parametersWithValues.setDelimiter(";");
        parametersWithValues.setLockUsers(Arrays.asList("user1", "user2"));
        parametersWithValues.setSwapTableType(SwapTableType.NO_SWAP);

        assertEquals(OnlineSchemaChangeSqlType.CREATE, parametersWithValues.getSqlType());
        assertEquals("CREATE TABLE foo (id INT PRIMARY KEY);", parametersWithValues.getSqlContent());
        assertEquals(TaskErrorStrategy.STOP, parametersWithValues.getErrorStrategy());
        assertEquals(Integer.valueOf(30), parametersWithValues.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(5), parametersWithValues.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP, parametersWithValues.getOriginTableCleanStrategy());
        assertEquals(";", parametersWithValues.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parametersWithValues.getLockUsers());
        assertEquals(SwapTableType.NO_SWAP, parametersWithValues.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parametersWithContinue = new OnlineSchemaChangeParameters();
        parametersWithContinue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parametersWithContinue.isContinueOnError());

        OnlineSchemaChangeParameters parametersWithStop = new OnlineSchemaChangeParameters();
        parametersWithStop.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parametersWithStop.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_success() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE foo ADD COLUMN bar INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
        assertEquals("ALTER TABLE foo ADD COLUMN bar INT;", subTaskParameter.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, subTaskParameter.getErrorStrategy());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE foo ADD COLUMN bar INT;");

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema)) {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("generateSubTaskParameters should throw UnexpectedException.");
        } catch (UnexpectedException e) {
            // expected
        }
    }
}