import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    // Add tests for other methods
    // ...

    @Test
    public void testGenerateSubTaskParameters() {
        
        // You should prepare a suitable ConnectionConfig for these tests
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";

        // Assuming that split SQLs and generation of OnlineSchemaChangeScheduleTaskParameters will not throw exception
        parameters.setSqlContent("good SQL goes here");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<OnlineSchemaChangeScheduleTaskParameters> output = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(output);
	
        // Assuming that split SQLs and generation of OnlineSchemaChangeScheduleTaskParameters will throw exception
        parameters.setSqlContent("bad SQL goes here");
        try {
            output = parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Expected exception did not occur");
        } catch(UnexpectedException e) {
            // Exception handling test passed
        }
      
	// For comprehensiveness, various edge cases and bad inputs are left as an exercise to the implementer

    }
}