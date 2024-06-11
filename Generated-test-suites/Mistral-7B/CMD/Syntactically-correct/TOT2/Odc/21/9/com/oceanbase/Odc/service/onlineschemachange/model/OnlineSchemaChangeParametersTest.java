package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testDefaultConstructor() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType expectedDefaultSqlType = OnlineSchemaChangeSqlType.ALTER;
        String expectedDefaultDelimiter = ";";
        // Test other default values as needed
        assertEquals(expectedDefaultSqlType, parameters.getSqlType());
        assertEquals(expectedDefaultDelimiter, parameters.getDelimiter());
    }

    @Test
    public void testGenerateSubTaskParameters_Valid() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        expectedSubTaskParameters.add(new OnlineSchemaChangeScheduleTaskParameters("testDatabaseName", "testTableName", sqlType));
        params.setSqlType(sqlType);

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedSubTaskParameters, actualSubTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_InvalidSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        OnlineSchemaChangeSqlType invalidSqlType = null;
        params.setSqlType(invalidSqlType);

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testIsContinueOnError_False() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnError_True() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

}