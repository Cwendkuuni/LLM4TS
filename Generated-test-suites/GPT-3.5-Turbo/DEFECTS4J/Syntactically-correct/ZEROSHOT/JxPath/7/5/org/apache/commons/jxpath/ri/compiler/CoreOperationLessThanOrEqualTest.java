import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Test case 1
        context.setNodeSet(new NodeSet());
        Object result1 = expression.computeValue(context);
        assertNull(result1);

        // Test case 2
        context.setNodeSet(new NodeSet());
        arg1 = new Constant(new Object());
        arg2 = new Constant(new Object());
        Object result2 = expression.computeValue(context);
        assertEquals(false, result2);

        // Test case 3
        context.setNodeSet(new NodeSet());
        arg1 = new Constant(5);
        arg2 = new Constant(10);
        Object result3 = expression.computeValue(context);
        assertEquals(true, result3);

        // Test case 4
        context.setNodeSet(new NodeSet());
        arg1 = new Constant(10);
        arg2 = new Constant(5);
        Object result4 = expression.computeValue(context);
        assertEquals(false, result4);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = expression.getSymbol();

        assertEquals("<=", symbol);
    }
}