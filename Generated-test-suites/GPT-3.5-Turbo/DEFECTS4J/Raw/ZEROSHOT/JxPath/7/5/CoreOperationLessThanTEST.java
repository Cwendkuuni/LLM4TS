import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Create test instances
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                new CoreOperationVariable("arg1"),
                new CoreOperationVariable("arg2"));

        // Test values
        double arg1 = 10.0;
        double arg2 = 5.0;

        // Set up EvalContext
        EvalContext context = new EvalContext();

        // Set variable values in the EvalContext
        context.setVariable("arg1", arg1);
        context.setVariable("arg2", arg2);

        // Compute value and check result
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}