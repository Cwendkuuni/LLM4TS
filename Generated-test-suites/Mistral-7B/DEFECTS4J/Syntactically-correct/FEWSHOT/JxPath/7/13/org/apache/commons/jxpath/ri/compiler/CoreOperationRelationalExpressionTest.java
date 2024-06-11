package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationGt(arg1, arg2), arg2);

        assertEquals(CoreOperationRelationalExpression.class, expression.getClass());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
        assertEquals(2, expression.getNumArgs());
        assertSame(arg1, expression.getArg(0));
        assertSame(arg2, expression.getArg(1));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testWithDifferentTypes() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression = new CoreOperationGt(arg1, arg2);

        assertNotSame(arg1, expression.getArg(0));
        assertNotSame(arg2, expression.getArg(1));
    }

    @Test
    public void testWithSameTypes() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(1);

        CoreOperationRelationalExpression expression = new CoreOperationGt(arg1, arg2);

        assertNotSame(arg1, expression.getArg(0));
        assertNotSame(arg2, expression.getArg(1));
    }

    @Test
    public void testToString() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression = new CoreOperationGt(arg1, arg2);

        String expected = "gt(1, x)";
        String result = expression.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(1);
        Expression arg3 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression1 = new CoreOperationGt(arg1, arg2);
        CoreOperationRelationalExpression expression2 = new CoreOperationGt(arg1, arg3);

        assertFalse(expression1.equals(expression2));
        assertFalse(expression1.equals(expression1.getArg(0)));
        assertTrue(expression1.equals(expression1));
    }

    @Test
    public void testHashCode() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression1 = new CoreOperationGt(arg1, arg2);
        CoreOperationRelationalExpression expression2 = new CoreOperationGt(arg1, arg2);

        int hashCode1 = expression1.hashCode();
        int hashCode2 = expression2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetArg() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");

        CoreOperationRelationalExpression expression = new CoreOperationGt(arg1, arg2);

        assertSame(arg1, expression.getArg(0));
        assertSame(arg2, expression.getArg(1));
    }

    @Test
    public void testSetArg() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationVariable("x");
        Expression arg3 = new CoreOperationConstant(2);

        CoreOperationRelationalExpression expression = new CoreOperationGt(arg1, arg2);

        expression.setArg(0, arg3);

        assertSame(arg3, expression.getArg(0));
    }

    @Test
    public void testGetPrecedence_GT() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testGetPrecedence_GE() {
        CoreOperationRelationalExpression expression = new CoreOperationGtOrEq(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testGetPrecedence_LT() {
        CoreOperationRelationalExpression expression = new CoreOperationLt(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testGetPrecedence_LE() {
        CoreOperationRelationalExpression expression = new CoreOperationLtOrEq(new CoreOperationVariable("x"), new CoreOperationConstant(1));
        assertEquals(3, expression.getPrecedence());
    }
}