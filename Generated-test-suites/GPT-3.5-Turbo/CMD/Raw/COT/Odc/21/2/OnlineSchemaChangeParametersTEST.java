
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnErrorTrue() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorFalse() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "test_schema";
        
        assertNotNull(parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("invalid");
        String schema = "test_schema";
        
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

}

