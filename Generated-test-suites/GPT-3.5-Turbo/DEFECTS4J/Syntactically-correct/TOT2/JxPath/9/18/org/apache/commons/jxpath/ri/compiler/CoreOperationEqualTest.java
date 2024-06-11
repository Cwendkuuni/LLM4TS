import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();

        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);

        assertEquals(arg1, opEqual.getArgs()[0]);
        assertEquals(arg2, opEqual.getArgs()[1]);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);

        // Test for equal values
        context.setCurrentNode("test");
        boolean result = (boolean) opEqual.computeValue(context);
        assertTrue(result);

        // Test for non-equal values
        context.setCurrentNode(null);
        result = (boolean) opEqual.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);

        assertEquals("=", opEqual.getSymbol());
    }

    // Add additional test cases for potential edge cases and exception handling scenarios

}