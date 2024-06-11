import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual.CoreOperationCompareArgs;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testEqualBooleanValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationCompareArgs(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.TRUE)), null);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqualBooleanValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationCompareArgs(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.FALSE)), null);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqualIntegerValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationCompareArgs(new ConstantExpression(Integer.valueOf(5)), new ConstantExpression(Integer.valueOf(5))), null);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqualIntegerValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationCompareArgs(new ConstantExpression(Integer.valueOf(5)), new ConstantExpression(Integer.valueOf(7))), null);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalStateException.class)
    public void testArg1IsNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, new CoreOperationCompareArgs(new ConstantExpression(Integer.valueOf(5)), null));
    }

    @Test(expected = IllegalStateException.class)
    public void testArg2IsNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationCompareArgs(new ConstantExpression(Integer.valueOf(5)), null), null);
    }
}