package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNewInstance() {
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

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN id INT");
        assertEquals("ALTER TABLE test_table ADD COLUMN id INT", parameters.getSqlContent());

        parameters.setErrorStrategy(TaskErrorStrategy.RETHROW);
        assertEquals(TaskErrorStrategy.RETHROW, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter(";");
        assertEquals(";", parameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("test_user1");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW);
        assertEquals(SwapTableType.SWAP_TO_NEW, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        String schema = "test_schema";
        String sqlContent = "ALTER TABLE test_table ADD COLUMN id INT";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW);

        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(taskParameter);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        assertEquals(taskParameter, subTaskParameters.get(0));

        verify(subTaskParameterFactory, times(1)).generate(eq(sqlContent), eq(OnlineSchemaChangeSqlType.ALTER));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException());

        String schema = "test_schema";
        String sqlContent = "ALTER TABLE test_table ADD COLUMN id INT";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        fail("UnexpectedException expected");
    }
}