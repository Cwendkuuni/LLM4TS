
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        // mock necessary dependencies
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        params.setDelimiter(";");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "test_schema");
        
        assertEquals(1, subTaskParams.size());
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        // mock necessary dependencies
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        params.setDelimiter(";");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        
        // force an exception by passing an invalid schema name
        params.generateSubTaskParameters(connectionConfig, null);
    }

}
