package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    // Testing constructor
    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertEquals("CoreOperationRelationalExpression", expr.getClass().getSimpleName());
        assertEquals(2, expr.getNumberOfArgs());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    // Basic test cases for each method
    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(3));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(3));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThan(new ConstantExpression(3), new ConstantExpression(5));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new ConstantExpression(3), new ConstantExpression(5));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    // Edge cases and exception handling test cases
    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        Expression[] args = new Expression[0];
        new CoreOperationRelationalExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidArgs() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpression(new Expression[0]);
        new CoreOperationRelationalExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanNullArg() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(null, new ConstantExpression(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanOrEqualNullArg() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(null, new ConstantExpression(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanNullArg() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThan(new ConstantExpression(3), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualNullArg() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new ConstantExpression(3), null);
    }

    @Test
    public void testGreaterThanEqualSameValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanEqualDifferentTypes() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanEqualSameValue() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new ConstantExpression(3), new ConstantExpression(3));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanEqualDifferentTypes() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new ConstantExpression(3.0), new ConstantExpression(3));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanWithBigDecimal() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(new BigDecimal("5.123")), new ConstantExpression(new BigDecimal("5.122")));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualWithBigDecimal() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(new BigDecimal("5.123")), new ConstantExpression(new BigDecimal("5.122")));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanWithBigDecimal() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThan(new ConstantExpression(new BigDecimal("3.123")), new ConstantExpression(new BigDecimal("5.122")));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithBigDecimal() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new ConstantExpression(new BigDecimal("3.123")), new ConstantExpression(new BigDecimal("5.122")));
        Object result = expr.evaluate(new Context());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}