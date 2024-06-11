
import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD column1 VARCHAR(255); ALTER TABLE test2 ADD column2 INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, subTaskParameters.size());
    }
}
