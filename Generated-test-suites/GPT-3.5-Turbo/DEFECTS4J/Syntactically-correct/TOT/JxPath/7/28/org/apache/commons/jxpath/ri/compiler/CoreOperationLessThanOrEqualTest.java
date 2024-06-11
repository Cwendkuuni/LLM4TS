import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Typical use case
        EvalContext context = new EvalContext();
        context.setArg(arg1, 5); // Set arg1 value to 5
        context.setArg(arg2, 10); // Set arg2 value to 10
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Edge case
        context = new EvalContext();
        context.setArg(arg1, 10); // Set arg1 value to 10
        context.setArg(arg2, 10); // Set arg2 value to 10
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Error scenario
        context = new EvalContext();
        context.setArg(arg1, "invalid"); // Set arg1 value to invalid
        context.setArg(arg2, 10); // Set arg2 value to 10
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Typical use case
        assertEquals("<=", expression.getSymbol());
        
        // Edge case
        expression = new CoreOperationLessThanOrEqual(arg1, arg1);
        assertEquals("<=", expression.getSymbol());
    }
}