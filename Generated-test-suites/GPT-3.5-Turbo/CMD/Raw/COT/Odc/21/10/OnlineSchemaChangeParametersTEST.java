import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    OnlineSchemaChangeParameters parameters;

    @Test
    public void testIsContinueOnError() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Create mock objects as needed for testing
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";

        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table1 RENAME COLUMN col1 TO new_col1;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        assertNotNull(parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testGenerateSubTaskParameters_Exception() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";

        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table1 RENAME COLUMN col1 TO new_col1;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        Exception exception = assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(null, schema);
        });

        String expectedMessage = "Failed to generate subtasks with parameter: " + parameters;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Additional test cases can be added for edge cases and exception handling scenarios

}


