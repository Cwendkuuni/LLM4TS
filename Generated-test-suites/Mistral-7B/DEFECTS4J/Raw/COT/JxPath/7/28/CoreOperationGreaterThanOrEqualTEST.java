import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.RuntimeContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFalseResult() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(10.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNonDoubleArg1() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new StringConstantExpression("5"),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNonDoubleArg2() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new StringConstantExpression("5"));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFloatArg1() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Float(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFloatArg2() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Float(5.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeNumbers() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(-3.5)),
                new ConstantExpression(new Double(-2.5)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(0.0)),
                new ConstantExpression(new Double(0.0)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZeroAndNegativeNumber() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(0.0)),
                new ConstantExpression(new Double(-3.5)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithVeryLargeNumbers() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(Double.POSITIVE_INFINITY)),
                new ConstantExpression(new Double(Double.POSITIVE_INFINITY)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithVeryLargeNumberAndVerySmallNumber() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(Double.POSITIVE_INFINITY)),
                new ConstantExpression(new Double(Double.MIN_VALUE)));

        EvalContext context = new EvalContext(new RuntimeContext(), new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}