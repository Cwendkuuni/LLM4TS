package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testCreateInstance() {
        Expression arg1 = new CoreOperation("1");
        Expression arg2 = new CoreOperation("2");
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        assertNotNull(expr);
        assertSame(CoreOperationRelationalExpression.class, expr.getClass());
        assertEquals(new CoreOperationGreaterThan(arg1, arg2), expr);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithInvalidNumberOfArguments() {
        Expression arg1 = new CoreOperation("1");
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {arg1});
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithNullArgument() {
        Expression arg1 = new CoreOperation(null);
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {arg1});
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperation("1"), new CoreOperation("2")});
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperation("1"), new CoreOperation("2")});
        assertFalse(expr.isSymmetric());
    }

    @org.junit.Test
    public void testEquals() {
        Expression arg1 = new CoreOperation("1");
        Expression arg2 = new CoreOperation("2");
        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThan(new CoreOperation[] {arg2, arg1});

        assertEquals(expr1, expr1);
        assertEquals(expr1, expr2);
        assertNotSame(expr1, expr3);
    }

    @org.junit.Test
    public void testHashCode() {
        Expression arg1 = new CoreOperation("1");
        Expression arg2 = new CoreOperation("2");
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        int hashCode = expr.hashCode();
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(new CoreOperation[] {arg2, arg1});
        int hashCode2 = expr2.hashCode();
        assertNotSame(hashCode, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        Expression arg1 = new CoreOperation("1");
        Expression arg2 = new CoreOperation("2");
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        String toString = expr.toString();
        String expected = "(&gt; (1) (2))";
        assertEquals(expected, toString);
    }

}