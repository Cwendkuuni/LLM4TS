package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArgs;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.SingleValueExpression;

public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testConstructor() {
        Expression[] args = new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertNotNull(coreOperationRelationalExpression);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArgs() {
        Expression[] args = new Expression[]{null};
        new CoreOperationRelationalExpression(args);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        boolean symmetric = coreOperationRelationalExpression.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testEqualsObject() {
        CoreOperationRelationalExpression coreOperationRelationalExpression1 = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        CoreOperationRelationalExpression coreOperationRelationalExpression2 = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        coreOperationRelationalExpression1.equals(coreOperationRelationalExpression2);
    }

    @org.junit.Test
    public void testHashCode() {
        CoreOperationRelationalExpression coreOperationRelationalExpression1 = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        CoreOperationRelationalExpression coreOperationRelationalExpression2 = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        int hashCode1 = coreOperationRelationalExpression1.hashCode();
        int hashCode2 = coreOperationRelationalExpression2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testToString() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        coreOperationRelationalExpression.toString();
    }

    @org.junit.Test
    public void testToStringWithArgs() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        String toString = coreOperationRelationalExpression.toString(">", new CoreOperationRelationalExpressionArgs());
        String expected = ">(1, 2)";
        assertEquals(expected, toString);
    }

    @org.junit.Test
    public void testGetExpressionType() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        assertEquals(CoreOperationRelationalExpression.class, coreOperationRelationalExpression.getType());
    }

    @org.junit.Test
    public void testEvaluateSingle() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{new SingleValueExpression(1), new SingleValueExpression(2)});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertNull(result);
    }

    @org.junit.Test
    public void testEvaluateSingleTrue() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleFalse() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(3);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleEqual() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleNotEqual() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleGreater() {
        Expression arg1 = new SingleValueExpression(2);
        Expression arg2 = new SingleValueExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleLess() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluateSingleNullArg() {
        Expression arg1 = null;
        Expression arg2 = new SingleValueExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        coreOperationRelationalExpression.evaluateSingle(null);
    }

    @org.junit.Test
    public void testEvaluateSingleNullArg2() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = null;
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertNull(result);
    }

    @org.junit.Test
    public void testEvaluateSingleSameValue() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleZeroAndNegative() {
        Expression arg1 = new SingleValueExpression(-1);
        Expression arg2 = new SingleValueExpression(0);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testEvaluateSingleZeroAndPositive() {
        Expression arg1 = new SingleValueExpression(0);
        Expression arg2 = new SingleValueExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testEvaluateSinglePositiveAndNegative() {
        Expression arg1 = new SingleValueExpression(1);
        Expression arg2 = new SingleValueExpression(-1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = coreOperationRelationalExpression.evaluateSingle(null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

}