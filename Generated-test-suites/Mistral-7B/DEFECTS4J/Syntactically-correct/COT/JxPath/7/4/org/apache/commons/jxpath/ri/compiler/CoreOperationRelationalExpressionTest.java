package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertInstanceOf(CoreOperationRelationalExpression.class, expr);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[0]);
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[0]);
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    /**
     * Test basic functionality of ">" method.
     */
    @Test
    public void testGreaterThan() {
        CoreOperation left = new ConstantExpression(5);
        CoreOperation right = new ConstantExpression(10);
        CoreOperationRelationalExpression expr = new GreaterThanExpression(left, right);
        boolean result = expr.evaluate(new TestContext(new Object[0]));
        assertTrue(result);
    }

    /**
     * Test basic functionality of "<=" method with equal values.
     */
    @Test
    public void testLessThanOrEqualWithEqualValues() {
        CoreOperation left = new ConstantExpression(5);
        CoreOperation right = new ConstantExpression(5);
        CoreOperationRelationalExpression expr = new LessThanOrEqualExpression(left, right);
        boolean result = expr.evaluate(new TestContext(new Object[0]));
        assertTrue(result);
    }

    /**
     * Test basic functionality of "<=" method with non-equal values.
     */
    @Test
    public void testLessThanOrEqualWithNonEqualValues() {
        CoreOperation left = new ConstantExpression(5);
        CoreOperation right = new ConstantExpression(10);
        CoreOperationRelationalExpression expr = new LessThanOrEqualExpression(left, right);
        boolean result = expr.evaluate(new TestContext(new Object[0]));
        assertFalse(result);
    }

    /**
     * Test basic functionality of ">=" method with equal values.
     */
    @Test
    public void testGreaterThanOrEqualWithEqualValues() {
        CoreOperation left = new ConstantExpression(5);
        CoreOperation right = new ConstantExpression(5);
        CoreOperationRelationalExpression expr = new GreaterThanOrEqualExpression(left, right);
        boolean result = expr.evaluate(new TestContext(new Object[0]));
        assertTrue(result);
    }

    /**
     * Test basic functionality of ">=" method with non-equal values.
     */
    @Test
    public void testGreaterThanOrEqualWithNonEqualValues() {
        CoreOperation left = new ConstantExpression(5);
        CoreOperation right = new ConstantExpression(10);
        CoreOperationRelationalExpression expr = new GreaterThanOrEqualExpression(left, right);
        boolean result = expr.evaluate(new TestContext(new Object[0]));
        assertFalse(result);
    }

    /**
     * Test exception when negating a relational expression.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNegateRelationalExpression() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(10));
        CoreOperation negatedExpr = expr.negate();
    }

    /**
     * Test exception when trying to perform arithmetic operations on relational expressions.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testArithmeticOperationsOnRelationalExpressions() {
        CoreOperation left = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(10));
        CoreOperation right = new AddExpression(new ConstantExpression(1), left);
    }
}