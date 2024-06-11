import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = parameters.generateSubTaskParameters(connectionConfig, "test_db");
        
        assertEquals(1, subTaskParams.size());
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUrl("invalid_url"); // Intentionally setting invalid URL to trigger exception
        
        parameters.generateSubTaskParameters(connectionConfig, "test_db");
    }
}