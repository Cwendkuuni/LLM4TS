package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Operator;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        Expression a = new CoreNumber(1);
        Expression b = new CoreNumber(2);

        CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression(Operator.GT, a, b);
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationRelationalExpression(Operator.GTE, a, b);
        CoreOperationRelationalExpression lessThan = new CoreOperationRelationalExpression(Operator.LT, a, b);
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationRelationalExpression(Operator.LTE, a, b);

        assertEquals(Operator.GT, greaterThan.getOperator());
        assertEquals(Operator.GTE, greaterThanOrEqual.getOperator());
        assertEquals(Operator.LT, lessThan.getOperator());
        assertEquals(Operator.LTE, lessThanOrEqual.getOperator());

        assertTrue(greaterThan.getPrecedence() > lessThan.getPrecedence());
        assertFalse(greaterThanOrEqual.getPrecedence() > lessThan.getPrecedence());
        assertTrue(greaterThan.isSymmetric() == false);
        assertFalse(greaterThanOrEqual.isSymmetric() == true);
    }

    @Test
    public void testGetPrecedence() {
        Expression a = new CoreNumber(1);
        Expression b = new CoreNumber(2);

        CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression(Operator.GT, a, b);
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationRelationalExpression(Operator.GTE, a, b);
        CoreOperationRelationalExpression lessThan = new CoreOperationRelationalExpression(Operator.LT, a, b);
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationRelationalExpression(Operator.LTE, a, b);

        assertEquals(3, greaterThan.getPrecedence());
        assertEquals(3, greaterThanOrEqual.getPrecedence());
        assertEquals(3, lessThan.getPrecedence());
        assertEquals(3, lessThanOrEqual.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression a = new CoreNumber(1);
        Expression b = new CoreNumber(2);

        CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression(Operator.GT, a, b);
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationRelationalExpression(Operator.GTE, a, b);
        CoreOperationRelationalExpression lessThan = new CoreOperationRelationalExpression(Operator.LT, a, b);
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationRelationalExpression(Operator.LTE, a, b);

        assertFalse(greaterThan.isSymmetric());
        assertFalse(greaterThanOrEqual.isSymmetric());
        assertFalse(lessThan.isSymmetric());
        assertTrue(lessThanOrEqual.isSymmetric());
    }

    @Test
    public void testCoreOperationRelationalExpressionNull() {
        Expression a = null;
        Expression b = new CoreNumber(2);

        try {
            new CoreOperationRelationalExpression(Operator.GT, a, b);
            assertFalse("Expected IllegalArgumentException when args are null", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.GTE, a, b);
            assertFalse("Expected IllegalArgumentException when args are null", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.LT, a, b);
            assertFalse("Expected IllegalArgumentException when args are null", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.LTE, a, b);
            assertFalse("Expected IllegalArgumentException when args are null", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationRelationalExpressionEmptyArgs() {
        Expression[] args = {};

        try {
            new CoreOperationRelationalExpression(Operator.GT, args);
            assertFalse("Expected IllegalArgumentException when args are empty", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.GTE, args);
            assertFalse("Expected IllegalArgumentException when args are empty", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.LT, args);
            assertFalse("Expected IllegalArgumentException when args are empty", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationRelationalExpression(Operator.LTE, args);
            assertFalse("Expected IllegalArgumentException when args are empty", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}