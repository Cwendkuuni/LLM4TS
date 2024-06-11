import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        // Initialize your connectionConfig here
        // TODO
        String schema = "testSchema";
        // Make sure that the connectionConfig and schema inputs are set up correctly for the test
        // TODO
        assertNotNull(parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testGenerateSubTaskParametersWithNullAndEmptyInputs() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        // should throw an UnexpectedException when connectionConfig is null
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(null, "testSchema"));

        // should throw an UnexpectedException when schema is empty
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(new ConnectionConfig(), ""));
    }
}