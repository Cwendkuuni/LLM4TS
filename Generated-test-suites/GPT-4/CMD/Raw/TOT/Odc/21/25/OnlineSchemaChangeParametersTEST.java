import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test Class For OnlineSchemaChangeParameters.
 */
public class OnlineSchemaChangeParametersTest {
    private static OnlineSchemaChangeParameters parameters; 

    @BeforeClass
    public static void setUp() throws Exception {
        parameters = new OnlineSchemaChangeParameters();
        // initialize all properties of parameters object
    }

    @Test
    public void testIsContinueOnError() {
        // Assume error strategy is CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        // Assume error strategy is not CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        // initialized connectionConfig
        
        // testing with wrong schema that will throw UnexpectedException
        parameters.generateSubTaskParameters(connectionConfig, "wrong schema");
    }
    
    // more test cases
}