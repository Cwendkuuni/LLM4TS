import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test typical use case
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT;");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "test_db");
        assertEquals(1, subTasks.size());
        
        // Test edge case
        params.setSqlType(OnlineSchemaChangeSqlType.DELETE);
        params.setSqlContent("DELETE FROM test WHERE id = 1;");
        subTasks = params.generateSubTaskParameters(connectionConfig, "test_db");
        assertEquals(0, subTasks.size());
        
        // Test error scenario
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test RENAME TO new_test;");
        try {
            subTasks = params.generateSubTaskParameters(connectionConfig, "test_db");
            fail("Expected Exception");
        } catch (UnexpectedException e) {
            // Exception expected
        }
    }
}