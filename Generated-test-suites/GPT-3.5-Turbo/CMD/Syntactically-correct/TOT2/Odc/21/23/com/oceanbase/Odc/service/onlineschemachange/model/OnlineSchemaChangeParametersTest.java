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
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "schema");
        
        assertEquals(1, subTaskParameters.size());
        
        OnlineSchemaChangeScheduleTaskParameters subTask = subTaskParameters.get(0);
        assertEquals("schema", subTask.getDatabaseName());
        assertEquals("test", subTask.getOriginTableName());
        assertEquals("ALTER TABLE test ADD COLUMN id INT;", subTask.getSqlsToBeExecuted().get(0));
    }

}