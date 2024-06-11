package com.oceanbase.odc.service.onlineschemachange.model;
import java.util.Map;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

//Public constructors and methods from the class
public class OnlineSchemaChangeParametersTest {
    OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        String mockSchema = "mockSchema";
        parameters.setSqlContent("INVALID");
        parameters.generateSubTaskParameters(mockConnectionConfig ,mockSchema);
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.LOG_AND_STOP);
        assertFalse(parameters.isContinueOnError());
    }

    // Edge case and Error scenario tests
    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParameters_nullSchema() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        parameters.generateSubTaskParameters(mockConnectionConfig ,null);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_emptySqlContent() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        String mockSchema = "mockSchema";
        parameters.setSqlContent("");
        parameters.generateSubTaskParameters(mockConnectionConfig ,mockSchema);
    }
}
//