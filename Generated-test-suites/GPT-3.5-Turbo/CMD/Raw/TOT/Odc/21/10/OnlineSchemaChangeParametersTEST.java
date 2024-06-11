import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        // Typical use case
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        // Edge case
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Typical use case
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        params.setDelimiter(";");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, subTaskParams.size());
        assertEquals("test", subTaskParams.get(0).getOriginTableName());

        // Error scenario
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE test (id INT);");

        try {
            subTaskParams = params.generateSubTaskParameters(connectionConfig, "schema");
            fail("Exception should be thrown for unsupported sql type");
        } catch (Exception e) { }

    }

}