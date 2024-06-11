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
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
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
    public void testNewInstanceWithAllValues() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "alter table test table add column new_column int";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 10;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.TRUNCATE;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        SwapTableType swapTableType = SwapTableType.SWAP;

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy,
                lockTableTimeOutSeconds, swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers,
                swapTableType);

        assertEquals(sqlType, parameters.getSqlType());
        assertEquals(sqlContent, parameters.getSqlContent());
        assertEquals(errorStrategy, parameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, parameters.getDelimiter());
        assertNotNull(parameters.getLockUsers());
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER, "alter table test table add column new_column int;alter table test table add column new_column2 int",
                TaskErrorStrategy.CONTINUE, null, null, OriginTableCleanStrategy.TRUNCATE, ";", null, SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = subTaskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = subTaskParameters.get(1);

        String expectedDbName = "test";
        String expectedOriginTableName = "test";
        String expectedNewTableName = "test_new";

        verify(subTaskParameterFactory, times(2)).generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER));

        assertEquals(expectedDbName, taskParameter1.getDatabaseName());
        assertEquals(expectedOriginTableName, taskParameter1.getOriginTableName());
        assertEquals(expectedDbName, taskParameter2.getDatabaseName());
        assertEquals(expectedOriginTableName, taskParameter2.getOriginTableName());
        assertEquals(expectedNewTableName, taskParameter1.getNewTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter1.getSqlType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter2.getSqlType());
        assertTrue(taskParameter1.getSqlsToBeExecuted().size() > 1);
        assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());

        String alterStmt1 = "alter table test table add column new_column int";
        String alterStmt2 = "alter table test table add column new_column2 int";

        assertEquals(alterStmt1, taskParameter1.getSqlsToBeExecuted().get(0));
        assertEquals(alterStmt2, taskParameter2.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER))).thenThrow(new RuntimeException());

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER, "alter table test table add column new_column int",
                TaskErrorStrategy.CONTINUE, null, null, OriginTableCleanStrategy.TRUNCATE, ";", null, SwapTableType.SWAP);

        parameters.generateSubTaskParameters(connectionConfig, "test");
    }
}