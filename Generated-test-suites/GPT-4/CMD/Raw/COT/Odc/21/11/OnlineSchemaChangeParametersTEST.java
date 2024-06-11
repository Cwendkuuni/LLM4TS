import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig config = new ConnectionConfig();
        // set up config as needed

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("your SQL content");
        // other setup for parameters ...

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(config, "schema");
        assertFalse(subTaskParameters.isEmpty());
    }

    @Test
    public void testIsContinueOnError_NotSet() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError()); // default should be false
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_NullConfig() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("your SQL content");
        // This should throw UnexpectedException as config is null
        parameters.generateSubTaskParameters(null, "schema");
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_NullSchema() {
        ConnectionConfig config = new ConnectionConfig();
        // set up config as needed

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("your SQL content");
        // other setup for parameters ...

        // This should throw UnexpectedException as schema is null
        parameters.generateSubTaskParameters(config, null);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_EmptySqlContent() {
        ConnectionConfig config = new ConnectionConfig();
        // set up config as needed

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        // other setup for parameters ...

        // This should throw UnexpectedException as sqlContent is not set
        parameters.generateSubTaskParameters(config, "schema");
    }

}