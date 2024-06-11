package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.ComparisonType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidArgumentCount() {
        Expression[] args = new Expression[3];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test
    public void testGreaterThanExpression() {
        ComparisonType comparisonType = ComparisonType.GT;
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = new ConstantExpression(4);
        Expression expr = new CoreOperationGreaterThanExpression(comparisonType, leftOperand, rightOperand);
        CoreOperationRelationalExpression coreExpr = (CoreOperationRelationalExpression) expr;
        Double result = coreExpr.evaluate(new Context());
        assertTrue(result > 0.0);
    }

    @org.junit.Test
    public void testGreaterThanOrEqualExpression() {
        ComparisonType comparisonType = ComparisonType.GTE;
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = new ConstantExpression(3);
        Expression expr = new CoreOperationGreaterThanOrEqualExpression(comparisonType, leftOperand, rightOperand);
        CoreOperationRelationalExpression coreExpr = (CoreOperationRelationalExpression) expr;
        Double result = coreExpr.evaluate(new Context());
        assertTrue(result >= 0.0);
    }

    @org.junit.Test
    public void testLessThanExpression() {
        ComparisonType comparisonType = ComparisonType.LT;
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = new ConstantExpression(4);
        Expression expr = new CoreOperationLessThanExpression(comparisonType, leftOperand, rightOperand);
        CoreOperationRelationalExpression coreExpr = (CoreOperationRelationalExpression) expr;
        Double result = coreExpr.evaluate(new Context());
        assertTrue(result < 0.0);
    }

    @org.junit.Test
    public void testLessThanOrEqualExpression() {
        ComparisonType comparisonType = ComparisonType.LTE;
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = new ConstantExpression(3);
        Expression expr = new CoreOperationLessThanOrEqualExpression(comparisonType, leftOperand, rightOperand);
        CoreOperationRelationalExpression coreExpr = (CoreOperationRelationalExpression) expr;
        Double result = coreExpr.evaluate(new Context());
        assertTrue(result <= 0.0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidComparisonType() {
        ComparisonType invalidComparisonType = new ComparisonType(-1);
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = new ConstantExpression(4);
        Expression expr = new CoreOperationGreaterThanOrEqualExpression(invalidComparisonType, leftOperand, rightOperand);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullLeftOperand() {
        ComparisonType comparisonType = ComparisonType.GTE;
        Expression leftOperand = null;
        Expression rightOperand = new ConstantExpression(3);
        Expression expr = new CoreOperationGreaterThanOrEqualExpression(comparisonType, leftOperand, rightOperand);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullRightOperand() {
        ComparisonType comparisonType = ComparisonType.GTE;
        Expression leftOperand = new ConstantExpression(3);
        Expression rightOperand = null;
        Expression expr = new CoreOperationGreaterThanOrEqualExpression(comparisonType, leftOperand, rightOperand);
    }

}