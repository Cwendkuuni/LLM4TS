package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        Expression[] args = new Expression[0];
        new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expr.getPrecedence());
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expr.isSymmetric());
    }

    @org.junit.Test
    public void testEquals() {
        Expression[] arg1 = new Expression[2];
        Expression[] arg2 = new Expression[2];
        arg1[0] = new Constant(1);
        arg1[1] = new Constant(2);
        arg2[0] = new Constant(1);
        arg2[1] = new Constant(2);
        CoreOperationRelationalExpression expr1 = new CoreOperationRelationalExpression(arg1);
        CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(arg2);
        assertTrue(expr1.equals(expr2));
    }

    @org.junit.Test
    public void testNotEquals() {
        Expression[] arg1 = new Expression[2];
        Expression[] arg2 = new Expression[2];
        arg1[0] = new Constant(1);
        arg1[1] = new Constant(2);
        arg2[0] = new Constant(1);
        arg2[1] = new Constant(3);
        CoreOperationRelationalExpression expr1 = new CoreOperationRelationalExpression(arg1);
        CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(arg2);
        assertFalse(expr1.equals(expr2));
        assertNotEquals(expr1, expr2);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        expr.hashCode();
    }

    @org.junit.Test
    public void testToString() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        String expected = "CoreOperationRelationalExpression[args=[Constant[1], Constant[2]], precedence=3, symmetric=false]";
        assertEquals(expected, expr.toString());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testEvaluate() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        expr.evaluate();
    }

    @org.junit.Test
    public void testEvaluateWithValidArgs() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        Double result = (Double) expr.evaluate();
        assertTrue(result != null);
    }

    @org.junit.Test
    public void testEvaluateWithInvalidArgs() {
        Expression[] args = new Expression[1];
        args[0] = new CoreOperationRelationalExpression(new Expression[0]);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        try {
            expr.evaluate();
            fail("Should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testEvaluateWithNullArgs() {
        Expression[] args = new Expression[1];
        args[0] = null;
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        try {
            expr.evaluate();
            fail("Should have thrown an exception.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testEvaluateWithMismatchedTypes() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(1);
        args[1] = new CoreOperationAddition(new Expression[2]);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        try {
            expr.evaluate();
            fail("Should have thrown an exception.");
        } catch (ClassCastException e) {
            // Expected
        }
    }

}