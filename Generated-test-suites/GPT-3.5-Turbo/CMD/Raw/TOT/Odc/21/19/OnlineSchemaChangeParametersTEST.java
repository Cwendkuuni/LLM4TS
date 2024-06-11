import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (id INT);");
        onlineSchemaChangeParameters.setDelimiter(";");
        
        assertNotNull(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema));
    }

}