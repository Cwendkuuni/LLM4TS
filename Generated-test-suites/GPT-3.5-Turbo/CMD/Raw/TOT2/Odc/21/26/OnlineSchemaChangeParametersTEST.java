import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, subTasks.size());
        OnlineSchemaChangeScheduleTaskParameters subTask = subTasks.get(0);
        assertEquals("schema", subTask.getDatabaseName());
        assertEquals("test_table", subTask.getOriginTableName());
        assertEquals(1, subTask.getSqlsToBeExecuted().size());
    }

}