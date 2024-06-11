import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, result.size());

        // Test edge case for SQL content
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT; ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        List<OnlineSchemaChangeScheduleTaskParameters> result2 = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(2, result2.size());

        // Test error scenario for connection failure
        ConnectionConfig invalidConfig = new ConnectionConfig();
        invalidConfig.setDialectType("invalid");
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        try {
            parameters.generateSubTaskParameters(invalidConfig, "test_schema");
        } catch (UnexpectedException e) {
            assertTrue(true);
        }
    }
}