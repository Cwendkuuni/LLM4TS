package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
    }

    /**
     * Test getPrecedence method
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    /**
     * Test isSymmetric method
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    /**
     * Test basic functionality of ">" relational expression
     */
    @Test
    public void testGreaterThan() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertTrue(result);
    }

    /**
     * Test basic functionality of ">=" relational expression
     */
    @Test
    public void testGreaterThanOrEqual() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertTrue(result);

        left = new CoreNumber(1);
        right = new CoreNumber(2);
        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        result = expression.evaluate(null, null);
        assertFalse(result);
    }

    /**
     * Test basic functionality of "<" relational expression
     */
    @Test
    public void testLessThan() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertFalse(result);
    }

    /**
     * Test basic functionality of "<=" relational expression
     */
    @Test
    public void testLessThanOrEqual() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertTrue(result);

        left = new CoreNumber(1);
        right = new CoreNumber(2);
        expression = new CoreOperationLessThanOrEqual(new CoreOperation[] { left, right });
        result = expression.evaluate(null, null);
        assertFalse(result);
    }

    /**
     * Test null left operand
     */
    @Test(expected = NullPointerException.class)
    public void testNullLeftOperandGreaterThan() {
        CoreOperation left = null;
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null left operand in ">="
     */
    @Test(expected = NullPointerException.class)
    public void testNullLeftOperandGreaterThanOrEqual() {
        CoreOperation left = null;
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null left operand in "<"
     */
    @Test(expected = NullPointerException.class)
    public void testNullLeftOperandLessThan() {
        CoreOperation left = null;
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null left operand in "<="
     */
    @Test(expected = NullPointerException.class)
    public void testNullLeftOperandLessThanOrEqual() {
        CoreOperation left = null;
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null right operand in ">"
     */
    @Test(expected = NullPointerException.class)
    public void testNullRightOperandGreaterThan() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null right operand in ">="
     */
    @Test(expected = NullPointerException.class)
    public void testNullRightOperandGreaterThanOrEqual() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null right operand in "<"
     */
    @Test(expected = NullPointerException.class)
    public void testNullRightOperandLessThan() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = null;
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test null right operand in "<="
     */
    @Test(expected = NullPointerException.class)
    public void testNullRightOperandLessThanOrEqual() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = null;
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperation[] { left, right });
        expression.evaluate(null, null);
    }

    /**
     * Test equality of same numbers
     */
    @Test
    public void testEquals() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertTrue(result);
    }

    /**
     * Test inequality of different numbers
     */
    @Test
    public void testInequality() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertFalse(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        result = expression.evaluate(null, null);
        assertFalse(result);

        expression = new CoreOperationLessThan(new CoreOperation[] { right, left });
        result = expression.evaluate(null, null);
        assertTrue(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { right, left });
        result = expression.evaluate(null, null);
        assertFalse(result);
    }

    /**
     * Test equality of a number and a string
     */
    @Test
    public void testNumberStringEquality() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreString("1");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertTrue(result);
    }

    /**
     * Test inequality of a number and a string
     */
    @Test
    public void testNumberStringInequality() {
        CoreOperation left = new CoreNumber(1);
        CoreOperation right = new CoreString("2");
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] { left, right });
        boolean result = expression.evaluate(null, null);
        assertFalse(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { left, right });
        result = expression.evaluate(null, null);
        assertFalse(result);

        expression = new CoreOperationLessThan(new CoreOperation[] { right, left });
        result = expression.evaluate(null, null);
        assertTrue(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { right, left });
        result = expression.evaluate(null, null);
        assertFalse(result);
    }
}