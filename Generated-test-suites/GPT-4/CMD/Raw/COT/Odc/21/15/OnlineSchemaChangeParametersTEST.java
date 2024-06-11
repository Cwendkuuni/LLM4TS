  
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    
    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE Test (ID INT, NAME VARCHAR(20), AGE INT)");
        
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
            fail("Should have thrown an exception because the SubTaskParameterFactory can't be instanciated in tests");
        } catch (RuntimeException e) {
            assertEquals("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters, e.getMessage());
        }
    }

    // Test edge cases for GenerateSubTaskParameters where SQLException or other Exception occurs in 'try' block
    // This assures that any unexpected Exception can be handled using the generateSubTaskParameters method
    // This test mocks the Exception scenario
    @Test
    public void testGenerateSubTaskParametersExceptionScenario() {
        // setup
        ConnectionConfig connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters.setSqlContent("INVALID SQL CONTENT");
        onlineSchemaChangeParameters.setDelimiter(";");

        try {
            // method invocation
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
            fail("an exception should have been thrown");
        } catch (Exception e) {
            // verify the result
            assertEquals("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters, e.getMessage());
        }
    }

}
