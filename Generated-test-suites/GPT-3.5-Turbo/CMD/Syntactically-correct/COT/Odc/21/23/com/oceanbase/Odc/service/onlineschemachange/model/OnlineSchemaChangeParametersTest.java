
import org.junit.Test;
import static org.junit.Assert.*;

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
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        
        assertEquals("test_schema", subTaskParameter.getDatabaseName());
        assertEquals("test_table", subTaskParameter.getOriginTableName());
        assertEquals("new_column", subTaskParameter.getNewTableName());

        List<String> sqlsToBeExecuted = subTaskParameter.getSqlsToBeExecuted();
        assertNotNull(sqlsToBeExecuted);
        assertEquals(1, sqlsToBeExecuted.size());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", sqlsToBeExecuted.get(0));
    }

}
