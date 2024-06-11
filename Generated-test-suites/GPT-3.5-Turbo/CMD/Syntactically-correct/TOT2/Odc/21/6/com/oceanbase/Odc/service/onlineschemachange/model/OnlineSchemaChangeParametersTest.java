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
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        params.setDelimeter(";");
        
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("MySQL");
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(config, "test_schema");
        
        assertEquals(1, subTaskParams.size());
        assertEquals("test_schema", subTaskParams.get(0).getDatabaseName());
        assertEquals("test_table", subTaskParams.get(0).getOriginTableName());
    }
}