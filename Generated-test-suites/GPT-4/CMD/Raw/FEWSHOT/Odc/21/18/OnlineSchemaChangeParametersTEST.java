package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(ConnectionConfig.DialectType.MYSQL);
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE testTable (id INT, name VARCHAR(30), PRIMARY KEY (id))");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        
        String schema = "testSchema";
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> generatedParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            Assert.assertTrue("The generateSubTaskParameters method should return a non-empty list.", !generatedParameters.isEmpty());
        } catch(UnexpectedException e) {
            Assert.fail("UnexpectedException was thrown by generateSubTaskParameters method.");
        }
    }
    
    @Test
    public void testGenerateSubTaskParametersWithException() {
        
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(ConnectionConfig.DialectType.MYSQL);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID SQL CONTENT");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        String schema = "testSchema";

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
            Assert.fail("The generateSubTaskParameters method should have thrown an UnexpectedException.");
        } catch(UnexpectedException ignored) {
            //Expected Exception
        }
    }
}