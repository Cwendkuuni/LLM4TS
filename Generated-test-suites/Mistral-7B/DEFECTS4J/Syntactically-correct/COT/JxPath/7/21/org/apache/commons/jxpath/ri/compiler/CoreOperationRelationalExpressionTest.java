package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Operator;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;

public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testConstructor() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression.Operator[]{Operator.GT}, arg1, arg2);
        assertInstanceOf(CoreOperationRelationalExpression.class, expr);
    }

    @org.junit.Test
    public void testGetPrecedence_GT() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, new NumberValue(1), new NumberValue(2));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testGetPrecedence_GE() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, new NumberValue(1), new NumberValue(2));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testGetPrecedence_LT() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, new NumberValue(1), new NumberValue(2));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testGetPrecedence_LE() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, new NumberValue(1), new NumberValue(2));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidOperator() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        new CoreOperationRelationalExpression(new CoreOperationRelationalExpression.Operator[]{Operator.INVALID}, arg1, arg2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidNumberOfOperators() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        new CoreOperationRelationalExpression(new CoreOperationRelationalExpression.Operator[]{Operator.GT, Operator.GE}, arg1, arg2);
    }

    @org.junit.Test
    public void testIsSymmetric_GT() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, new NumberValue(1), new NumberValue(2));
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test
    public void testIsSymmetric_GE() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, new NumberValue(1), new NumberValue(2));
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test
    public void testIsSymmetric_LT() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, new NumberValue(1), new NumberValue(2));
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test
    public void testIsSymmetric_LE() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, new NumberValue(1), new NumberValue(2));
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test
    public void testGt_Numbers() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(3);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testGt_Numbers_False() {
        Expression arg1 = new NumberValue(3);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGt_Numbers_Equal() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGe_Numbers() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(3);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGe_Numbers_True() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testGe_Numbers_Equal() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLt_Numbers() {
        Expression arg1 = new NumberValue(3);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLt_Numbers_False() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(3);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testLt_Numbers_Equal() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testLe_Numbers() {
        Expression arg1 = new NumberValue(3);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLe_Numbers_True() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLe_Numbers_Equal() {
        Expression arg1 = new NumberValue(5);
        Expression arg2 = new NumberValue(5);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testGt_Strings() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("banana");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGt_Strings_False() {
        Expression arg1 = new StringValue("banana");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGt_Strings_Equals() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGe_Strings() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("banana");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testGe_Strings_True() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testGe_Strings_Equals() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLt_Strings() {
        Expression arg1 = new StringValue("banana");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLt_Strings_False() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("banana");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testLt_Strings_Equals() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LT, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testLe_Strings() {
        Expression arg1 = new StringValue("banana");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLe_Strings_True() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testLe_Strings_Equals() {
        Expression arg1 = new StringValue("apple");
        Expression arg2 = new StringValue("apple");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.LE, arg1, arg2);
        boolean result = expr.evaluate(new TestContext());
        assertTrue(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluate_NullContext() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(Operator.GT, arg1, arg2);
        expr.evaluate(null);
    }
}