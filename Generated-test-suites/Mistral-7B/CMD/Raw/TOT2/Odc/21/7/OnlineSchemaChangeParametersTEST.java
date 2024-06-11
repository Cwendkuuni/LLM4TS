package com.oceanbase.odc.service.onlineschemachange.model.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;
    private String schema;

    @Before public void setUp() { connectionConfig = new ConnectionConfig(); schema = "test_schema"; }

    @Test public void testGetSqlType() { parameters = new OnlineSchemaChangeParameters(); assertSame(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType()); }

    @Test public void testSetSqlType() { parameters = new OnlineSchemaChangeParameters(); parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER); assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType()); }

    @Test(expected = IllegalArgumentException.class) public void testSetSqlTypeNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setSqlType(null); }

    @Test public void testGetSqlContent() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getSqlContent()); }

    @Test public void testSetSqlContent() { parameters = new OnlineSchemaChangeParameters(); String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY)"; parameters.setSqlContent(sqlContent); assertEquals(sqlContent, parameters.getSqlContent()); }

    @Test(expected = NullPointerException.class) public void testSetSqlContentNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setSqlContent(null); }

    @Test public void testIsContinueOnError() { parameters = new OnlineSchemaChangeParameters(); assertFalse(parameters.isContinueOnError()); }

    @Test public void testSetErrorStrategy() { parameters = new OnlineSchemaChangeParameters(); parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE); assertTrue(parameters.isContinueOnError()); }

    @Test(expected = IllegalArgumentException.class) public void testSetErrorStrategyNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setErrorStrategy(null); }

    @Test public void testGetLockTableTimeOutSeconds() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getLockTableTimeOutSeconds()); }

    @Test public void testSetLockTableTimeOutSeconds() { parameters = new OnlineSchemaChangeParameters(); parameters.setLockTableTimeOutSeconds(10); assertEquals(10, parameters.getLockTableTimeOutSeconds()); }

    @Test(expected = IllegalArgumentException.class) public void testSetLockTableTimeOutSecondsNegative() { parameters = new OnlineSchemaChangeParameters(); parameters.setLockTableTimeOutSeconds(-1); }

    @Test public void testGetSwapTableNameRetryTimes() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getSwapTableNameRetryTimes()); }

    @Test public void testSetSwapTableNameRetryTimes() { parameters = new OnlineSchemaChangeParameters(); parameters.setSwapTableNameRetryTimes(5); assertEquals(5, parameters.getSwapTableNameRetryTimes()); }

    @Test(expected = IllegalArgumentException.class) public void testSetSwapTableNameRetryTimesNegative() { parameters = new OnlineSchemaChangeParameters(); parameters.setSwapTableNameRetryTimes(-1); }

    @Test public void testGetOriginTableCleanStrategy() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getOriginTableCleanStrategy()); }

    @Test public void testSetOriginTableCleanStrategy() { parameters = new OnlineSchemaChangeParameters(); parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_OLD); assertEquals(OriginTableCleanStrategy.DELETE_OLD, parameters.getOriginTableCleanStrategy()); }

    @Test(expected = IllegalArgumentException.class) public void testSetOriginTableCleanStrategyNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setOriginTableCleanStrategy(null); }

    @Test public void testGetDelimiter() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getDelimiter()); }

    @Test public void testSetDelimiter() { parameters = new OnlineSchemaChangeParameters(); parameters.setDelimiter(";"); assertEquals(";", parameters.getDelimiter()); }

    @Test(expected = IllegalArgumentException.class) public void testSetDelimiterNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setDelimiter(null); }

    @Test public void testGetLockUsers() { parameters = new OnlineSchemaChangeParameters(); assertNull(parameters.getLockUsers()); }

    @Test public void testSetLockUsers() { parameters.setLockUsers(new ArrayList<>()); assertEquals(0, parameters.getLockUsers().size()); }

    @Test(expected = IllegalArgumentException.class) public void testSetLockUsersNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setLockUsers(null); }

    @Test public void testGetSwapTableType() { parameters = new OnlineSchemaChangeParameters(); assertSame(SwapTableType.SWAP_TO_TEMP, parameters.getSwapTableType()); }

    @Test public void testSetSwapTableType() { parameters = new OnlineSchemaChangeParameters(); parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW); assertEquals(SwapTableType.SWAP_TO_NEW, parameters.getSwapTableType()); }

    @Test(expected = IllegalArgumentException.class) public void testSetSwapTableTypeNull() { parameters = new OnlineSchemaChangeParameters(); parameters.setSwapTableType(null); }

    @Test public void testGenerateSubTaskParameters() { parameters = new OnlineSchemaChangeParameters(); List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema); assertNotNull(result); }

    @Test(expected = UnexpectedException.class) public void testGenerateSubTaskParametersException() { parameters = new OnlineSchemaChangeParameters(); ConnectionConfig connectionConfig = new ConnectionConfig(); ConnectionException exception = new ConnectionException("Test Exception"); parameters.setConnectionConfig(connectionConfig); parameters.setErrorStrategy(TaskErrorStrategy.STOP); throw exception; List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema); fail("Should have thrown UnexpectedException"); }
}