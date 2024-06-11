package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import lombok.Data;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {
    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void testNewOnlineSchemaChangeParameters() {
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

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());

        String sqlContent = "ALTER TABLE t1 ADD COLUMN c1 INT";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(5);
        assertEquals(5, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());

        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());

        String delimiter = ";";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.SWAP;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy("INVALID_STRATEGY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableNameRetryTimes(-1);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP);

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, result.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(0);
        assertEquals("mysql", taskParameters.getConnectionConfig().getDialectType());
        assertEquals("test_schema", taskParameters.getSchema());
        assertNull(taskParameters.getDatabaseName());
        assertEquals("t1", taskParameters.getOriginTableName());
        assertEquals("ALTER TABLE t1 ADD COLUMN c1 INT", taskParameters.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.getSqlType());
        assertNull(taskParameters.getNewTableName());
        assertNull(taskParameters.getErrorStrategy());
        assertNull(taskParameters.getLockTableTimeOutSeconds());
        assertNull(taskParameters.getSwapTableNameRetryTimes());
        assertNull(taskParameters.getOriginTableCleanStrategy());
        assertNull(taskParameters.getSqlsToBeExecuted());
    }

    @Test(expected = Exception.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER))).thenThrow(new RuntimeException("test exception"));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        fail("Should throw exception");
    }
}