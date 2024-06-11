package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, "test_schema"));
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("test_sql;test_sql2");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        when(subTaskParameterFactory.generate("test_sql", OnlineSchemaChangeSqlType.CREATE)).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters("test_db", "test_table", "test_sql", "test_sql2"));

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, result.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(0);
        assertEquals("test_db", taskParameters.getDatabaseName());
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertEquals(2, taskParameters.getSqlsToBeExecuted().size());
        verify(subTaskParameterFactory, times(1)).generate("test_sql", OnlineSchemaChangeSqlType.CREATE);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws UnexpectedException {
        when(subTaskParameterFactory.generate("test_sql", OnlineSchemaChangeSqlType.CREATE)).thenThrow(new RuntimeException());
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}