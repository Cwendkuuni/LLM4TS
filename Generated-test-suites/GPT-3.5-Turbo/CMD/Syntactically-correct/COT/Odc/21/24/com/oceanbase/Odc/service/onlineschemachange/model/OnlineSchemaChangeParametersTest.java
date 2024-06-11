
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    // Edge case: Empty sqlContent
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSubTaskParametersEmptySqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("");
        params.generateSubTaskParameters(new ConnectionConfig(), "schema");
    }

    // Edge case: Null connectionConfig
    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParametersNullConnectionConfig() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test");
        params.generateSubTaskParameters(null, "schema");
    }

    // Exception handling scenario: Exception when generating subtasks
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test");
        params.generateSubTaskParameters(new ConnectionConfig(), "schema");
    }

}