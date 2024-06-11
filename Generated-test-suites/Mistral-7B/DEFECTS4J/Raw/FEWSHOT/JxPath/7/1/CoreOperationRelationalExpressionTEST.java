package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThan(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThanOrEqual(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5));
        assertEquals(3, expr.getPrecedence());

        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5));
        assertEquals(3, expr.getPrecedence());

        expr = new CoreOperationLessThan(new ConstantExpression(5));
        assertEquals(3, expr.getPrecedence());

        expr = new CoreOperationLessThanOrEqual(new ConstantExpression(5));
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testCoreOperationRelationalExpressionWithTwoConstants() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(10);

        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationRelationalExpression[] { left, right });
        assertTrue(expr.evaluate(new Context()));

        expr = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpression[] { left, right });
        assertTrue(expr.evaluate(new Context()));

        expr = new CoreOperationLessThan(new CoreOperationRelationalExpression[] { left, right });
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpression[] { left, right });
        assertFalse(expr.evaluate(new Context()));
    }

    @Test
    public void testCoreOperationRelationalExpressionWithTwoVariables() {
        Expression left = new VariableExpression("x");
        Expression right = new VariableExpression("y");

        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationRelationalExpression[] { left, right });

        // x > y
    }

    @Test
    public void testCoreOperationRelationalExpressionWithTwoVariablesAndConstant() {
        Expression left = new VariableExpression("x");
        Expression right = new ConstantExpression(5);

        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationRelationalExpression[] { left, right });
        // x > 5

        expr = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpression[] { left, right });
        // x >= 5

        expr = new CoreOperationLessThan(new CoreOperationRelationalExpression[] { left, right });
        // x < 5

        expr = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpression[] { left, right });
        // x <= 5
    }

    @Test
    public void testCoreOperationRelationalExpressionWithThreeConstants() {
        Expression expr1 = new ConstantExpression(5);
        Expression expr2 = new ConstantExpression(10);
        Expression expr3 = new ConstantExpression(7);

        CoreOperationRelationalExpression op1 = new CoreOperationGreaterThan(new CoreOperationRelationalExpression[] { expr1, expr2 });
        CoreOperationRelationalExpression op2 = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpression[] { expr2, expr3 });
        CoreOperationRelationalExpression op3 = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpression[] { expr1, expr3 });

        CoreOperationRelationalExpression expr = new CoreOperationLogicalAnd(new CoreOperationRelationalExpression[] { op1, op2 });
        // (5 > 10) && (10 >= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalOr(new CoreOperationRelationalExpression[] { op1, op3 });
        // (5 > 10) || (5 <= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalAnd(new CoreOperationRelationalExpression[] { op2, op3 });
        // (10 >= 7) && (5 <= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalOr(new CoreOperationRelationalExpression[] { op1, op3 });
        // (5 > 10) || (5 <= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalAnd(new CoreOperationRelationalExpression[] { op1, op2 });
        // (5 > 10) && (10 >= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalOr(new CoreOperationRelationalExpression[] { op1, op2 });
        // (5 > 10) || (10 >= 7)
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalAnd(new CoreOperationRelationalExpression[] { op1, new CoreOperationConstantExpression(true) });
        // (5 > 10) && true
        assertFalse(expr.evaluate(new Context()));

        expr = new CoreOperationLogicalOr(new CoreOperationRelationalExpression[] { op1, new CoreOperationConstantExpression(false) });
        // (5 > 10) || false
        assertFalse(expr.evaluate(new Context()));
    }
}