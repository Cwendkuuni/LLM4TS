import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters params;
    
    @Before
    public void setUp() {
        params = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError_NON_CONTINUE() {
        params.setErrorStrategy(TaskErrorStrategy.NON_CONTINUE);
        Assert.assertFalse(params.isContinueOnError());
    }

    @Test
    public void testIsContinueOnError_CONTINUE() {
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(params.isContinueOnError());
    }
    
    // Here you must set up a valid ConnectionConfig, which is not specified in the current problem
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Failed() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        params.generateSubTaskParameters(connectionConfig, "schema");
    }
}