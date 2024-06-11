package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
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
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());

        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY)", parameters.getSqlContent());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(30);
        assertEquals(30, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter(":");
        assertEquals(":", parameters.getDelimiter());

        List<String> users = Arrays.asList("user1", "user2");
        parameters.setLockUsers(users);
        assertEquals(users, parameters.getLockUsers());

        parameters.setSwapTableType(SwapTableType.NO_SWAP);
        assertEquals(SwapTableType.NO_SWAP, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetErrorStrategyNull() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(null);
    }

    @Test
    public void testGenerateSubTaskParametersNullConnectionConfig() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(null, ""));
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParametersNullSchema() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(connectionConfig, null));
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, result.size());
    }

}