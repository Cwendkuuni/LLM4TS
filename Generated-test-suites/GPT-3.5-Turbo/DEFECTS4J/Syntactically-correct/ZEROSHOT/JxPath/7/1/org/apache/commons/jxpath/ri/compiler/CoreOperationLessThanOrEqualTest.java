import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result1 = operation.computeValue(context);
        assertTrue(result1 instanceof Boolean);

        // Test case 2
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Object result2 = operation.computeValue(context);
        assertTrue(result2 instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}