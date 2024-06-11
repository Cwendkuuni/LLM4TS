package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        assertEquals(3, expression.getPrecedence());
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test equals method with same expression.
     */
    @Test
    public void testEqualsSameExpression() {
        Expression lhs1 = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        Expression lhs2 = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        Expression rhs = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(2), new ConstantExpression(1)});

        assertTrue(lhs1.equals(lhs1));
        assertFalse(lhs1.equals(rhs));
        assertFalse(lhs1.equals(null));
    }

    /**
     * Test equals method with different expression.
     */
    @Test
    public void testEqualsDifferentExpression() {
        Expression lhs = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        Expression rhs = new CoreOperationEquals(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});

        assertFalse(lhs.equals(rhs));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Expression expression1 = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        Expression expression2 = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(3)});

        assertTrue(expression1.hashCode() != expression2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Expression expression = new CoreOperationGreaterThan(new Expression[] {new ConstantExpression(1), new ConstantExpression(2)});
        String expected = "(&gt;(1, 2))";
        String actual = expression.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test evaluate method with true result.
     */
    @Test
    public void testEvaluateTrue() {
        Expression lhs = new ConstantExpression(1);
        Expression rhs = new ConstantExpression(2);
        Expression expression = new CoreOperationGreaterThan(new Expression[] {lhs, rhs});
        Object result = expression.evaluate(new TestContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test evaluate method with false result.
     */
    @Test
    public void testEvaluateFalse() {
        Expression lhs = new ConstantExpression(1);
        Expression rhs = new ConstantExpression(3);
        Expression expression = new CoreOperationGreaterThan(new Expression[] {lhs, rhs});
        Object result = expression.evaluate(new TestContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test evaluate method with null lhs.
     */
    @Test(expected = IllegalStateException.class)
    public void testEvaluateNullLhs() {
        Expression rhs = new ConstantExpression(2);
        Expression expression = new CoreOperationGreaterThan(new Expression[] {null, rhs});
        expression.evaluate(new TestContext());
    }

    /**
     * Test evaluate method with null rhs.
     */
    @Test(expected = IllegalStateException.class)
    public void testEvaluateNullRhs() {
        Expression lhs = new ConstantExpression(1);
        Expression expression = new CoreOperationGreaterThan(new Expression[] {lhs, null});
        expression.evaluate(new TestContext());
    }

    /**
     * Test evaluate method with equal values.
     */
    @Test
    public void testEvaluateEqualValues() {
        Expression lhs = new ConstantExpression(1);
        Expression rhs = new ConstantExpression(1);
        Expression expression = new CoreOperationGreaterThan(new Expression[] {lhs, rhs});
        Object result = expression.evaluate(new TestContext());
        assertNull(result);
    }

}