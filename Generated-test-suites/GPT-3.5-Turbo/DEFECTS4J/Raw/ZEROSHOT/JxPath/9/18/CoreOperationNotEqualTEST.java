import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operator = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Test case 1
        Object result1 = operator.computeValue(context);
        assertEquals(Boolean.TRUE, result1);

        // Test case 2
        // Add more test cases here

    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operator = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", operator.getSymbol());
    }

    // Add more test methods here

}