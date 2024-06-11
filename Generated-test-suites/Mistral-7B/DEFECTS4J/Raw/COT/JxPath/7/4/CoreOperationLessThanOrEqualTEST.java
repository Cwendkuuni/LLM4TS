package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationLessThanOrEqualConstructor() {
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationLessThanOrEqual);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationLessThanOrEqual.getClass());
    }

    /**
     * Test computeValue with positive numbers.
     */
    @Test
    public void testComputeValueWithPositiveNumbers() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(1.0);
        arg2 = new ConstantExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue with zero and positive numbers.
     */
    @Test
    public void testComputeValueWithZeroAndPositiveNumbers() {
        Expression arg1 = new ConstantExpression(0.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(0.0);
        arg2 = new ConstantExpression(0.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue with negative numbers.
     */
    @Test
    public void testComputeValueWithNegativeNumbers() {
        Expression arg1 = new ConstantExpression(-1.0);
        Expression arg2 = new ConstantExpression(-2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new ConstantExpression(-1.0);
        arg2 = new ConstantExpression(-1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue with NaN.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNaN() {
        Expression arg1 = new ConstantExpression(Double.NaN);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationLessThanOrEqual.computeValue(context);
    }

    /**
     * Test computeValue with null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNull() {
        Expression arg1 = new ConstantExpression((Object) null);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationLessThanOrEqual.computeValue(context);
    }

    /**
     * Test getSymbol.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression[] {});
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }
}