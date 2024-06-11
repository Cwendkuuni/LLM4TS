package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {
    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "testSchema";
        String dialectType = "MySQL";

        when(connectionConfig.getDialectType()).thenReturn(dialectType);
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenAnswer(i -> {
            OnlineSchemaChangeScheduleTaskParameters parameter = new OnlineSchemaChangeScheduleTaskParameters();
            parameter.setDatabaseName("testDB");
            parameter.setOriginTableName("testTable");
            parameter.setNewTableName("newTable");
            return parameter;
        });

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE testTable ADD COLUMN newCol INT");
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SAME_NAME);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        assertEquals("testDB", taskParameter.getDatabaseName());
        assertEquals("testTable", taskParameter.getOriginTableName());
        assertEquals("newTable", taskParameter.getNewTableName());

        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(2, sqlsToBeExecuted.size());

        assertEquals("ALTER TABLE testTable ADD COLUMN newCol INT", sqlsToBeExecuted.get(0));
        String newAlterStmt = DdlUtils.replaceTableName("ALTER TABLE testTable ADD COLUMN newCol INT", "newTable", dialectType, OnlineSchemaChangeSqlType.ALTER);
        assertEquals(newAlterStmt, sqlsToBeExecuted.get(1));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenThrow(new RuntimeException());

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE testTable ADD COLUMN newCol INT");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }
}