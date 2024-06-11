import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError_WithErrorStrategyContinue_ReturnsTrue() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testIsContinueOnError_WithErrorStrategyStop_ReturnsFalse() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_WithValidSqlContent_ReturnsSubTaskParametersList() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE test_table (id INT);");
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "schema");
        
        assertEquals(1, subTaskParameters.size());
        assertEquals("test_table", subTaskParameters.get(0).getOriginTableName());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_WithInvalidSqlContent_ThrowsUnexpectedException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("INVALID SQL");
        params.setDelimiter(";");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        params.generateSubTaskParameters(connectionConfig, "schema");
    }

}