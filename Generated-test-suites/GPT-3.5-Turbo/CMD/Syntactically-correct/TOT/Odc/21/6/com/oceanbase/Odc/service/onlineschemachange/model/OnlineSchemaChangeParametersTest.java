import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test typical use case
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        // Set up parameters
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name INT;");
        params.setDelimiter(";");
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTasks.size());

        // Test edge case when sqlType is not ALTER
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        subTasks = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, subTasks.size());

        // Test error scenario
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("INVALID SQL STATEMENT");
        try {
            subTasks = params.generateSubTaskParameters(connectionConfig, schema); // This should throw an exception
            fail("Expected exception to be thrown");
        } catch (UnexpectedException e) {
            // Exception expected
        }
    }
}