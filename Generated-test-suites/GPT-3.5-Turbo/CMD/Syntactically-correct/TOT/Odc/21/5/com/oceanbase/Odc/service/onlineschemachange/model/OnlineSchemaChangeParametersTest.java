import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters params;

    @Before
    public void setUp() {
        params = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "test_schema";
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParamsList = params.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(subTaskParamsList);
        assertEquals(1, subTaskParamsList.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParams = subTaskParamsList.get(0);
        assertEquals("test_schema", subTaskParams.getDatabaseName());
        assertEquals("test_table", subTaskParams.getOriginTableName());
        assertEquals(1, subTaskParams.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", subTaskParams.getSqlToBeExecuted().get(0));
    }

}