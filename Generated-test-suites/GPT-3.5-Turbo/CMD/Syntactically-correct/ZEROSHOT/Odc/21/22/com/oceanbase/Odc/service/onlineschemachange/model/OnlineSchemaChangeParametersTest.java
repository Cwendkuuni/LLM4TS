package com.oceanbase.odc.service.onlineschemachange.model;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("MYSQL");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(1, subTaskParameters.size());
        assertEquals("test_schema", subTaskParameters.get(0).getDatabaseName());
        assertEquals("test", subTaskParameters.get(0).getOriginTableName());
        assertEquals(1, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test ADD COLUMN id INT", subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }
}