
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");
        // Initialize connectionConfig and schema
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        
        // Test generating sub task parameters
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParams.size());
    }
}
