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
        params.setErrorStrategy(TaskErrorStrategy.STOP);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "schema");
        assertNotNull(subTasks);
        assertEquals(1, subTasks.size());
    }

}