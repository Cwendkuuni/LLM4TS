package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;

/**
 * Test cases for CoreOperationLessThanOrEqual class
 */
public class CoreOperationLessThanOrEqualTest {

    // Testing constructor
    @org.junit.Test
    public void testCoreOperationLessThanOrEqualConstructor() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreExpression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("Expected CoreOperationLessThanOrEqual, but got " + coreOperationLessThanOrEqual.getClass().getName(),
                CoreOperationLessThanOrEqual.class, coreOperationLessThanOrEqual.getClass());
    }

    // Testing computeValue method with valid arguments
    @org.junit.Test
    public void testComputeValueWithValidArguments() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreExpression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(1.0);
        arg2 = new CoreConstantExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing getSymbol method
    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1.0),
                new CoreConstantExpression(2.0));
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }

    // Testing computeValue method with same arguments
    @org.junit.Test
    public void testComputeValueWithSameArguments() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg1);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing computeValue method with equal arguments
    @org.junit.Test
    public void testComputeValueWithEqualArguments() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreExpression arg2 = new CoreConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing computeValue method with null context
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullContext() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreExpression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = null;
        coreOperationLessThanOrEqual.computeValue(context);
    }

    // Testing computeValue method with non-numeric arguments
    @org.junit.Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArguments() {
        CoreExpression arg1 = new CoreStringExpression("1");
        CoreExpression arg2 = new CoreStringExpression("2");
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationLessThanOrEqual.computeValue(context);
    }

    // Testing computeValue method with NaN arguments
    @org.junit.Test
    public void testComputeValueWithNaNArguments() {
        CoreExpression arg1 = new CoreConstantExpression(Double.NaN);
        CoreExpression arg2 = new CoreConstantExpression(Double.NaN);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);
    }

    // Testing computeValue method with negative arguments
    @org.junit.Test
    public void testComputeValueWithNegativeArguments() {
        CoreExpression arg1 = new CoreConstantExpression(-1.0);
        CoreExpression arg2 = new CoreConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertSame(Boolean.FALSE, result);
    }

    // Testing computeValue method with positive arguments
    @org.junit.Test
    public void testComputeValueWithPositiveArguments() {
        CoreExpression arg1 = new CoreConstantExpression(1.0);
        CoreExpression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertSame(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(1.0);
        arg2 = new CoreConstantExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing computeValue method with zero arguments
    @org.junit.Test
    public void testComputeValueWithZeroArguments() {
        CoreExpression arg1 = new CoreConstantExpression(0.0);
        CoreExpression arg2 = new CoreConstantExpression(0.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing computeValue method with negative and positive arguments
    @org.junit.Test
    public void testComputeValueWithNegativeAndPositiveArguments() {
        CoreExpression arg1 = new CoreConstantExpression(-1.0);
        CoreExpression arg2 = new CoreConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertSame(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(1.0);
        arg2 = new CoreConstantExpression(-1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    // Testing computeValue method with large positive and negative arguments
    @org.junit.Test
    public void testComputeValueWithLargePositiveAndNegativeArguments() {
        CoreExpression arg1 = new CoreConstantExpression(Double.MAX_VALUE);
        CoreExpression arg2 = new CoreConstantExpression(Double.MAX_VALUE);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(Double.MIN_VALUE);
        arg2 = new CoreConstantExpression(Double.MIN_VALUE);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}