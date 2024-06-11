import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table1 ADD column1 INT;");
        params.setDelimiter(";");

        ConnectionConfig connConfig = new ConnectionConfig();
        connConfig.setDialectType(DialectType.MYSQL);

        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connConfig, schema);

        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("table1", subTaskParam.getOriginTableName());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD column1 INT;", subTaskParam.getSqlsToBeExecuted().get(0));
    }
}