import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        params.setSqlContent("ALTER TABLE test ADD column1 VARCHAR(255);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "test_schema");
        
        assertEquals(1, subTasks.size());
        OnlineSchemaChangeScheduleTaskParameters subTask = subTasks.get(0);
        assertEquals("test_schema", subTask.getDatabaseName());
        assertEquals("test", subTask.getOriginTableName());
        assertEquals(1, subTask.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test ADD column1 VARCHAR(255);", subTask.getSqlsToBeExecuted().get(0));
    }

}