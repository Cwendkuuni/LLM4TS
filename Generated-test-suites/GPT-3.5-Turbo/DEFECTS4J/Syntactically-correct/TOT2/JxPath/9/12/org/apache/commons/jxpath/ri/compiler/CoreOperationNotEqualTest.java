import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Equal() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(5);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", op.getSymbol());
    }

    // Additional test cases for edge cases and exception handling scenarios go here

    // Possible edge case: passing null arguments
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArguments() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(null, null);
        op.computeValue(new EvalContext());
    }
}