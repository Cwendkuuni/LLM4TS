package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

/**
 * Test for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testCoreOperationLessThanOrEqualConstructionWithValidArguments() {
        Expression arg1 = ExpressionFactory.createLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.createLiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("CoreOperationLessThanOrEqual([1.0], [2.0])", coreOperationLessThanOrEqual.toString());
    }

    /**
     * Test computeValue method with valid arguments.
     */
    @Test
    public void testComputeValueWithValidArguments() {
        Expression arg1 = ExpressionFactory.createLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.createLiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestSupport.createTestInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);
        assertFalse("Expected false", result.booleanValue());

        arg2 = ExpressionFactory.createLiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertTrue("Expected true", result.booleanValue());
    }

    /**
     * Test computeValue method with NaN and infinities.
     */
    @Test
    public void testComputeValueWithNaNAndInfinities() {
        Expression arg1 = ExpressionFactory.createLiteralExpression(Double.NaN);
        Expression arg2 = ExpressionFactory.createLiteralExpression(Double.NaN);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestSupport.createTestInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull("Expected null", result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        arg2 = ExpressionFactory.createLiteralExpression(Double.NEGATIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull("Expected null", result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.NEGATIVE_INFINITY);
        arg2 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull("Expected null", result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.NaN);
        arg2 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);
        assertFalse("Expected false", result.booleanValue());
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.createLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.createLiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualConstructionWithInvalidArguments() {
        Expression arg1 = new CoreOperationRelationalExpression(null);
        Expression arg2 = new CoreOperationRelationalExpression(null);
        new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    /**
     * Test computeValue method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithInvalidArguments() {
        Expression arg1 = new CoreOperationRelationalExpression(null);
        Expression arg2 = new CoreOperationRelationalExpression(null);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestSupport.createTestInfoSet();
        EvalContext context = new EvalContext(infoSet);

        coreOperationLessThanOrEqual.computeValue(context);
    }
}