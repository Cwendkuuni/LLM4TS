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

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setConnectionConfig(connectionConfig);
        onlineSchemaChangeParameters.setSubTaskParameterFactory(subTaskParameterFactory);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "testSchema";
        String dbName = "testDatabase";
        String originTableName = "testOriginTable";
        String newTableName = "testNewTable";
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY) ENGINE=OBSERVER;";

        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.CREATE))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        verify(subTaskParameterFactory, times(1)).generate(eq(sqlContent), eq(OnlineSchemaChangeSqlType.CREATE));

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        TableIdentity tableIdentity = new TableIdentity(dbName, originTableName);
        verify(taskParameter).getTableIdentity();
        assertEquals(dbName, taskParameter.getDatabaseName());
        assertEquals(originTableName, taskParameter.getOriginTableName());

        String newAlterStmt = "ALTER TABLE test_table MODIFY COLUMN id INT PRIMARY KEY;";
        verify(subTaskParameterFactory, times(1)).generate(eq(newAlterStmt), eq(OnlineSchemaChangeSqlType.ALTER));
        assertTrue(taskParameter.getSqlsToBeExecuted().contains(newAlterStmt));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersFailure() throws Exception {
        when(subTaskParameterFactory.generate(anyString(), anyInt())).thenThrow(new Exception());

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY) ENGINE=OBSERVER;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, null);
    }
}