package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Operator;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Constant;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
        assertEquals(Operator.GT, expression.getOperator());
        assertEquals(2, expression.getArity());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArgCount() {
        Expression[] args = new Expression[0];
        new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArgType() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpression(new Expression[0]);
        args[1] = new Constant(2);
        new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        expression.setOperator(Operator.GT);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        expression.setOperator(Operator.GT);
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }

    @org.junit.Test
    public void testGt() {
        Expression arg1 = new Constant(3);
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.GT);
        assertTrue(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test
    public void testGtWithInvalidArgs() {
        Expression arg1 = new Constant("invalid");
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.GT);
        assertFalse(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test
    public void testGte() {
        Expression arg1 = new Constant(3);
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.GTE);
        assertTrue(expression.evaluate(new Object[0], new Object[0][], null));
        Expression arg1_1 = new Constant(3);
        Expression arg2_1 = new Constant(3);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1_1, arg2_1}, Operator.GTE);
        assertTrue(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test
    public void testLt() {
        Expression arg1 = new Constant(1);
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.LT);
        assertFalse(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test
    public void testLtWithInvalidArgs() {
        Expression arg1 = new Constant("invalid");
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.LT);
        assertFalse(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test
    public void testLte() {
        Expression arg1 = new Constant(1);
        Expression arg2 = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}, Operator.LTE);
        assertFalse(expression.evaluate(new Object[0], new Object[0][], null));
        Expression arg1_1 = new Constant(1);
        Expression arg2_1 = new Constant(1);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1_1, arg2_1}, Operator.LTE);
        assertTrue(expression.evaluate(new Object[0], new Object[0][], null));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetOperatorWithInvalidOperator() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        expression.setOperator((Operator) null);
    }

}