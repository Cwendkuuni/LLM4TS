
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

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
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1;");
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "schema1");
        
        assertEquals(1, subTaskParams.size());
        
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("schema1", subTaskParam.getDatabaseName());
        assertEquals("table1", subTaskParam.getOriginTableName());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1", subTaskParam.getSqlsToBeExecuted().get(0));
    }
}
