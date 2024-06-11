package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.THROW_EXCEPTION);
        assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");
    
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "MySchema");
        assertNotNull(subTaskParameters);
        assertNotEquals(0, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstParam = subTaskParameters.get(0);
        assertEquals("MySchema", firstParam.getDatabaseName());
    }
}