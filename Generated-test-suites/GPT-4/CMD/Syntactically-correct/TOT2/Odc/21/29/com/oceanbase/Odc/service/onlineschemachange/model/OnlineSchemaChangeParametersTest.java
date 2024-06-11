package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters obj = new OnlineSchemaChangeParameters();
        obj.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(obj.isContinueOnError());

        obj.setErrorStrategy(null);
        assertFalse(obj.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        OnlineSchemaChangeParameters obj = new OnlineSchemaChangeParameters();
        obj.setSqlContent("");
        obj.generateSubTaskParameters(connectionConfig, schema);
    }

    // Test edge case by setting a NULL connectionConfig.
    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParameters_withNullConnectionConfig() {
        ConnectionConfig connectionConfig = null;
        String schema = "testSchema";
        OnlineSchemaChangeParameters obj = new OnlineSchemaChangeParameters();
        obj.setSqlContent("");
        obj.generateSubTaskParameters(connectionConfig, schema);
    }

    // Test edge case by setting a NULL schema.
    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParameters_withNullSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = null;
        OnlineSchemaChangeParameters obj = new OnlineSchemaChangeParameters();
        obj.setSqlContent("");
        obj.generateSubTaskParameters(connectionConfig, schema);
    }

    // This is a very minimal test set - a more comprehensive one would need to be generated
    // if the implementation details of the class were known.
}