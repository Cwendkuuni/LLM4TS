import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(60);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.AFTER_SWAP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.getLockUsers().add("user1");
        parameters.setSwapTableType(SwapTableType.DATA);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParams.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("test_table", subTaskParam.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParam.getSqlType());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT;", subTaskParam.getSqlsToBeExecuted().get(0));
    }

}