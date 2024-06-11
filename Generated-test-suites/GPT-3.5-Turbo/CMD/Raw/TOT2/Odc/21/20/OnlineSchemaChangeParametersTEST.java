
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test case for generating subtask parameters
    }

    // Add more test cases for other methods as per requirement

}
