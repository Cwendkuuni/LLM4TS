import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    private OnlineSchemaChangeParameters params;
    
    @Before
    public void setUp() {
        params = new OnlineSchemaChangeParameters();
    }
    
    // Test isContinueOnError method
    @Test
    public void testIsContinueOnError() {
        // Test when error strategy is CONTINUE
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        
        // Test when error strategy is STOP
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }
    
    // Test generateSubTaskParameters method
    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        String schema = "test";
        
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);
        
        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getDatabaseName());
        assertEquals("test_table", result.get(0).getOriginTableName());
    }
}