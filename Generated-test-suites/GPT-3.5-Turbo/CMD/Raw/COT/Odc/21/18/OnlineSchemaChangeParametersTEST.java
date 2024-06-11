
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
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParams.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("table1", subTaskParam.getOriginTableName());
    }

}
