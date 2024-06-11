package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected int getPrecedence() {
                return 4; // Overriding the precedence to test
            }
        };
        assertEquals(3, expression.getPrecedence()); // Testing base class precedence
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean isSymmetric() {
                return true; // Overriding the symmetry to test
            }
        };
        assertFalse(expression.isSymmetric()); // Testing base class symmetry
    }

    /**
     * Test functionality of > relational operation with two integers.
     */
    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(3));
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of > relational operation with two integers, incorrect order.
     */
    @Test(expected = AssertionError.class)
    public void testGreaterThanIncorrectOrder() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(3), new ConstantExpression(5));
        expression.evaluate(new Context()); // Should throw an exception
    }

    /**
     * Test functionality of >= relational operation with two integers.
     */
    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(3));
        assertTrue(expression.evaluate(new Context()));
        CoreOperationRelationalExpression expression2 = new GreaterThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));
        assertTrue(expression2.evaluate(new Context()));
    }

    /**
     * Test functionality of >= relational operation with two integers, incorrect order.
     */
    @Test(expected = AssertionError.class)
    public void testGreaterThanOrEqualIncorrectOrder() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));
        expression.evaluate(new Context()); // Should throw an exception
    }

    /**
     * Test functionality of < relational operation with two integers.
     */
    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(3), new ConstantExpression(5));
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of < relational operation with two integers, incorrect order.
     */
    @Test(expected = AssertionError.class)
    public void testLessThanIncorrectOrder() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(5), new ConstantExpression(3));
        expression.evaluate(new Context()); // Should throw an exception
    }

    /**
     * Test functionality of <= relational operation with two integers.
     */
    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));
        assertFalse(expression.evaluate(new Context()));
        CoreOperationRelationalExpression expression2 = new LessThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(3));
        assertTrue(expression2.evaluate(new Context()));
    }

    /**
     * Test functionality of <= relational operation with two integers, incorrect order.
     */
    @Test(expected = AssertionError.class)
    public void testLessThanOrEqualIncorrectOrder() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));
        expression.evaluate(new Context()); // Should throw an exception
    }

    /**
     * Test functionality of relational operation with two strings.
     */
    @Test
    public void testRelationalOperationStrings() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new StringConstantExpression("apple"), new StringConstantExpression("orange"));
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of relational operation with two strings, correct order.
     */
    @Test
    public void testRelationalOperationStringsCorrectOrder() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new StringConstantExpression("apple"), new StringConstantExpression("orange"));
        assertFalse(expression.evaluate(new Context()));
        CoreOperationRelationalExpression expression2 = new GreaterThanOrEqualExpression(new StringConstantExpression("orange"), new StringConstantExpression("apple"));
        assertTrue(expression2.evaluate(new Context()));
    }

    /**
     * Test functionality of relational operation with two strings, incorrect order.
     */
    @Test(expected = AssertionError.class)
    public void testRelationalOperationStringsIncorrectOrder() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new StringConstantExpression("orange"), new StringConstantExpression("apple"));
        expression.evaluate(new Context()); // Should throw an exception
    }
}