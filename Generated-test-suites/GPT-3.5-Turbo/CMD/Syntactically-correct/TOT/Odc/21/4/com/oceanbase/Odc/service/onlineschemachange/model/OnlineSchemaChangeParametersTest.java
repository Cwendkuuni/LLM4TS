import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test case for generating subtask parameters
        // Include typical use cases, edge cases, and error scenarios
    }

    // Additional test cases for other methods in OnlineSchemaChangeParameters class

}