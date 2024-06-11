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
        params.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        params.setDelimiter(";");
        
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("mysql");
        
        String schema = "schema";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(config, schema);
        
        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());
    }
}