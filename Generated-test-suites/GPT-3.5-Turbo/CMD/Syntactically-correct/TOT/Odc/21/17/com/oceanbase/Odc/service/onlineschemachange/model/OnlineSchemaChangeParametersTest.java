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
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table1 ADD column1 INT;");
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "schema1";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(subTaskParams);
        assertFalse(subTaskParams.isEmpty());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table1 ADD column1 INT;");
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("invalid_dialect");
        String schema = "schema1";

        params.generateSubTaskParameters(connectionConfig, schema);
    }
}