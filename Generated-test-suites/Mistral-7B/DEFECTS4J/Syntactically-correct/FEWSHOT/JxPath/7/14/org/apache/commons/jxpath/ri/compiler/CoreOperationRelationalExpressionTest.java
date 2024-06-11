package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.ComparisonType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        Expression left = ExpressionFactory.createNumber(1);
        Expression right = ExpressionFactory.createNumber(2);
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(left), right);

        assertTrue(expr.evaluate(null, null) == -1);
    }

    @Test
    public void testCoreOperationRelationalExpression_symmetric() {
        Expression left = ExpressionFactory.createNumber(1);
        Expression right = ExpressionFactory.createNumber(2);
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(left, right);

        assertFalse(expr.evaluate(null, null) == -1);
        assertTrue(expr.evaluate(null, null) <= 0);
    }

    @Test
    public void testCoreOperationRelationalExpression_precedence() {
        Expression left1 = ExpressionFactory.createNumber(1);
        Expression right1 = ExpressionFactory.createNumber(2);
        Expression left2 = ExpressionFactory.createNumber(3);
        Expression right2 = ExpressionFactory.createNumber(2);
        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(left1, right1);
        CoreOperationRelationalExpression expr2 = new CoreOperationPlus(ExpressionFactory.createNumber(1), expr1);
        CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThanOrEqual(left2, right2);

        CoreOperationRelationalExpression expr4 = new CoreOperationMinus(expr2, expr3);

        assertTrue(expr4.getPrecedence() == 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpression_invalidArgs() {
        Expression left = ExpressionFactory.createNumber(1);
        Expression right = null;
        new CoreOperationGreaterThan(left, right);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpression_invalidComparisonType() {
        Expression left = ExpressionFactory.createNumber(1);
        ComparisonType comparisonType = ComparisonType.GREATER_THAN_OR_EQUAL;
        new CoreOperationRelationalExpression(new Expression[] {left}, comparisonType);
    }
}