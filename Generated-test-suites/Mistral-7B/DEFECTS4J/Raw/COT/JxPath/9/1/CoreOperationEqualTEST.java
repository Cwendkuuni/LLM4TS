import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_sameValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_differentValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_invalidArguments() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(new EvalContext());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    @Test
    public void testComputeValue_equalNulls() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_equalDifferentTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}