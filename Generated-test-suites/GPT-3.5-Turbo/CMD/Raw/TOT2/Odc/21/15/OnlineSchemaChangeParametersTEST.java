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
        // Test for typical use case
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE table1;");
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("mysql");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(config, "schema");
        assertFalse(subTaskParams.isEmpty());
        
        // Test for edge case
        OnlineSchemaChangeParameters emptyParams = new OnlineSchemaChangeParameters();
        ConnectionConfig emptyConfig = new ConnectionConfig();
        emptyConfig.setDialectType("mysql");
        List<OnlineSchemaChangeScheduleTaskParameters> emptySubTaskParams = emptyParams.generateSubTaskParameters(emptyConfig, "schema");
        assertTrue(emptySubTaskParams.isEmpty());
        
        // Test for exception handling
        OnlineSchemaChangeParameters errorParams = new OnlineSchemaChangeParameters();
        ConnectionConfig errorConfig = new ConnectionConfig();
        errorConfig.setDialectType("mysql");
        errorParams.setSqlContent("INVALID SQL");
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> errorSubTaskParams = errorParams.generateSubTaskParameters(errorConfig, "schema");
            fail("Exception not thrown");
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + errorParams, e.getMessage());
        }
    }
}