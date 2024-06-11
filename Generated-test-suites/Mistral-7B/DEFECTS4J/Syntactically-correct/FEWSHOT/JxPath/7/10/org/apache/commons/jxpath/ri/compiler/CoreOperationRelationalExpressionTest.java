package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertEquals(2, expr.getArgumentCount());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    // Test subclasses: GreaterThanExpression, GreaterThanOrEqualExpression,
    // LessThanExpression, LessThanOrEqualExpression

    @Test
    public void testGreaterThanExpression() {
        Expression left = new VariableExpression("x");
        Expression right = new NumberExpression(3);
        CoreOperationRelationalExpression expr = new GreaterThanExpression(left, right);
        assertEquals(2, expr.getArgumentCount());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
        assertSame(left, expr.getLeftArgument());
        assertSame(right, expr.getRightArgument());
    }

    @Test
    public void testGreaterThanOrEqualExpression() {
        Expression left = new VariableExpression("x");
        Expression right = new NumberExpression(3);
        CoreOperationRelationalExpression expr = new GreaterThanOrEqualExpression(left, right);
        assertEquals(2, expr.getArgumentCount());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
        assertSame(left, expr.getLeftArgument());
        assertSame(right, expr.getRightArgument());
    }

    @Test
    public void testLessThanExpression() {
        Expression left = new VariableExpression("x");
        Expression right = new NumberExpression(3);
        CoreOperationRelationalExpression expr = new LessThanExpression(left, right);
        assertEquals(2, expr.getArgumentCount());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
        assertSame(left, expr.getLeftArgument());
        assertSame(right, expr.getRightArgument());
    }

    @Test
    public void testLessThanOrEqualExpression() {
        Expression left = new VariableExpression("x");
        Expression right = new NumberExpression(3);
        CoreOperationRelationalExpression expr = new LessThanOrEqualExpression(left, right);
        assertEquals(2, expr.getArgumentCount());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
        assertSame(left, expr.getLeftArgument());
        assertSame(right, expr.getRightArgument());
    }
}