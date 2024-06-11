package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(); 
    }
     
    @Test 
    public void testIsContinueOnError() {

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {

        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        when(mockConnectionConfig.getDialectType()).thenReturn("mySQL");

        onlineSchemaChangeParameters.setSqlContent("SELECT * FROM tbl; UPDATE tbl SET col = 1;");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.SELECT);

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(mockConnectionConfig, "mySchema");
        } catch (Exception e) {
            Assert.fail("Generating sub task parameters should not have thrown any exception");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersExceptionScenario() {

        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        when(mockConnectionConfig.getDialectType()).thenReturn(null);

        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE tbl;");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        onlineSchemaChangeParameters.generateSubTaskParameters(mockConnectionConfig, null);
    }

}
