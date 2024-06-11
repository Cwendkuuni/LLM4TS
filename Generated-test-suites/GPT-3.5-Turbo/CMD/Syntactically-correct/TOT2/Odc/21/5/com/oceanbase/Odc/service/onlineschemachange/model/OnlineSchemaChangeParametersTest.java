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
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column VARCHAR(255);");
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableNameRetryTimes(3);
        
        ConnectionConfig connConfig = new ConnectionConfig();
        connConfig.setDialectType("MYSQL");
        connConfig.setUrl("jdbc:mysql://localhost:3306/test");
        SubTaskParameterFactory subTaskParamFactory = new SubTaskParameterFactory(connConfig, "test_schema");
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connConfig, "test_schema");
        
        assertEquals(1, subTasks.size());
    }

    // Edge cases and exception handling scenarios can be added here
}