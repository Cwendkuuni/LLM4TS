package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        // Assuming we have a valid connectionConfig and schema
        ConnectionConfig config = new ConnectionConfig();
        String schema = "testSchema";
        assertNotNull(parameters.generateSubTaskParameters(config, schema));
        // Test rest of functionality here.
    }

    @Test(expected = NullPointerException.class)
    public void testIsContinueOnErrorNull() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(null);
        parameters.isContinueOnError();
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenThrow(new RuntimeException());
        parameters.generateSubTaskParameters(config, "testSchema");
    }
}