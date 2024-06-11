import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    
    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError(){
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test(expected = NullPointerException.class)
    public void testIsContinueOnErrorNullErrorStrategy(){
        onlineSchemaChangeParameters.setErrorStrategy(null);
        onlineSchemaChangeParameters.isContinueOnError();
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersNullArgs(){
        onlineSchemaChangeParameters.generateSubTaskParameters(null, null);
    }
    
    // More tests needed for the 'generateSubTaskParameters' method after mocking the necessary functions 
    // to return the intended test results.
}