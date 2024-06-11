import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Test when arg1 and arg2 are not equal
        Expression arg1 = new ConstantExpr(2);
        Expression arg2 = new ConstantExpr(3);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test when arg1 and arg2 are equal
        arg1 = new ConstantExpr(2);
        arg2 = new ConstantExpr(2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new ConstantExpr(2);
        Expression arg2 = new ConstantExpr(3);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}