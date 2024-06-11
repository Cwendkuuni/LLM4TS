import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("invalid sql content");
        parameters.generateSubTaskParameters(new ConnectionConfig(), "schema");
    }

}