import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

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
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column VARCHAR(255);");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");
        String schema = "test_schema";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        
        assertEquals(1, subTaskParameters.size());
        assertNotNull(subTaskParameters.get(0).getDatabaseName());
        assertNotNull(subTaskParameters.get(0).getOriginTableName());
        assertNotNull(subTaskParameters.get(0).getSqlsToBeExecuted());
    }

}