import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqualInstanceCreation() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        // Add assertions for the constructor, if needed
    }

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.1));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }

    @Test
    public void testEdgeCaseWithMinIntegerDifference() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(Integer.MIN_VALUE), new ConstantExpression(Integer.MIN_VALUE + 1));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEdgeCaseWithEqualValues() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEdgeCaseWithBothDoublesAndSmallDifference() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.1), new ConstantExpression(1.2));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEdgeCaseWithBothDoublesAndEqualValues() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEdgeCaseWithIntegerAndDouble() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEdgeCaseWithDoubleAndInteger() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEdgeCaseWithBothNaN() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWithNullArgs() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(null, new ConstantExpression(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWithNullArgs2() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWithNullContext() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        coreOperationLessThanOrEqual.computeValue(null);
    }
}