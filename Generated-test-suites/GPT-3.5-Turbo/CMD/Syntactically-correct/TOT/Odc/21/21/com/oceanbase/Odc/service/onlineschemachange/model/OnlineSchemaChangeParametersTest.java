import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        // Typical case: error strategy is set to CONTINUE
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        
        // Edge case: error strategy is set to STOP
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Typical case: generate subtask parameters successfully
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column VARCHAR(255);");
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setSwpaTableType(SwapTableType.HARD);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "test_schema");
        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());

        // Error scenario: fail to generate subtask parameters
        OnlineSchemaChangeParameters invalidParams = new OnlineSchemaChangeParameters();
        invalidParams.setSqlType(null);
        invalidParams.setSqlContent("INVALID SQL");
        invalidParams.setDelimiter(";");
        invalidParams.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        invalidParams.setSwapTableType(SwapTableType.HARD);

        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        invalidConnectionConfig.setDialectType(DialectType.MYSQL);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> invalidSubTaskParams = invalidParams.generateSubTaskParameters(invalidConnectionConfig, "test_schema");
            fail("Expected an Exception to be thrown");
        } catch (UnexpectedException e) {
            // Exception caught, test passed
        }
    }

}