package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, "test_schema"));
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("test_sql;test_sql2");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    }

    @After
    public void tearDown() {
        reset(subTaskParameterFactory);
    }

    @Test
    public void testInstance() {
        assertNotNull(parameters);
    }

    @Test
    public void testGetAndSetSqlType() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());
    }

    @Test
    public void testGetAndSetSqlContent() {
        String sqlContent = "test_sql;test_sql2";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testGetAndSetErrorStrategy() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());
    }

    @Test
    public void testGetAndSetLockTableTimeOutSeconds() {
        int lockTableTimeOutSeconds = 10;
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetAndSetSwapTableNameRetryTimes() {
        int swapTableNameRetryTimes = 5;
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetAndSetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy = OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetAndSetDelimiter() {
        String delimiter = ";";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("test_user1");
        lockUsers.add("test_user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        OnlineSchemaChangeParameters.SwapTableType swapTableType = OnlineSchemaChangeParameters.SwapTableType.SWAP_TO_NEW;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        when(subTaskParameterFactory.generate("test_sql", OnlineSchemaChangeSqlType.ALTER)).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
        when(subTaskParameterFactory.generate("test_sql2", OnlineSchemaChangeSqlType.ALTER)).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(2, taskParameters.size());

        assertEquals("test_schema", taskParameters.get(0).getDatabaseName());
        assertEquals("test_table1", taskParameters.get(0).getOriginTableName());
        assertEquals("new_test_table1", taskParameters.get(0).getNewTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.get(0).getSqlType());
        assertEquals(1, taskParameters.get(0).getSqlsToBeExecuted().size());

        assertEquals("test_schema", taskParameters.get(1).getDatabaseName());
        assertEquals("test_table2", taskParameters.get(1).getOriginTableName());
        assertEquals("new_test_table2", taskParameters.get(1).getNewTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.get(1).getSqlType());
        assertEquals(1, taskParameters.get(1).getSqlsToBeExecuted().size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        when(subTaskParameterFactory.generate("test_sql", OnlineSchemaChangeSqlType.ALTER)).thenThrow(new Exception());
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}