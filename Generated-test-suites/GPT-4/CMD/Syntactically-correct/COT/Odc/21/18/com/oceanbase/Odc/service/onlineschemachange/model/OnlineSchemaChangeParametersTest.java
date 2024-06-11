package com.oceanbase.odc.service.onlineschemachange.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
         OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
         params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
         assertTrue(params.isContinueOnError());

         params.setErrorStrategy(TaskErrorStrategy.FAIL);
         assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersNormal() {
         OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
         ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
         assertNotNull(params.generateSubTaskParameters(connectionConfig, "testSchema"));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersNullConnection() {
         OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
         params.generateSubTaskParameters(null, "testSchema");
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersEmptySchema() {
         OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
         ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
         params.generateSubTaskParameters(connectionConfig, "");
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenThrow(UnexpectedException.class);
        params.generateSubTaskParameters(connectionConfig, "testSchema");
    }
}