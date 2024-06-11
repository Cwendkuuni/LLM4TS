package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig mockConnectionConfig;
    private String schema;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        mockConnectionConfig = new ConnectionConfig();
        schema = "test_schema";
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(mockConnectionConfig, schema);
        } catch (UnexpectedException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowingException() {
        // To simulate error scenario, setup the mock to throw exception
        onlineSchemaChangeParameters.generateSubTaskParameters(mockConnectionConfig, schema);
    }
}