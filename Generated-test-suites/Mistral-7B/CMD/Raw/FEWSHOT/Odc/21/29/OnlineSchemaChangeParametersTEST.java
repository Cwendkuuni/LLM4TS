package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
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
    }

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testInstanceWithValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setLockUsers(new ArrayList<>());

        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY)", parameters.getsSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());
        assertNull(parameters.getLockUsers());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "testSchema";
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters("testDatabase", "testTable", List.of("CREATE TABLE test (id INT PRIMARY KEY)")));

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = new OnlineSchemaChangeParameters().generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters parameter = taskParameters.get(0);
        assertEquals("testDatabase", parameter.getDatabaseName());
        assertEquals("testTable", parameter.getOriginTableName());
        assertEquals(List.of("CREATE TABLE test (id INT PRIMARY KEY)"), parameter.getSqlsToBeExecuted());

        verify(subTaskParameterFactory, times(1)).generate(anyString(), OnlineSchemaChangeSqlType.CREATE);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenThrow(new RuntimeException("Test exception"));

        new OnlineSchemaChangeParameters().generateSubTaskParameters(connectionConfig, "testSchema");
    }
}