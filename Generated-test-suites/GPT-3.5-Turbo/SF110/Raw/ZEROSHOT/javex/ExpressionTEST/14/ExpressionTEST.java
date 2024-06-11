package org.javex;

import org.junit.Test;
import org.junit.Assert;
import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("");
        List result = expression.getExternalFunctions();
        Assert.assertNull(result);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("");
        ExpressionStack stack = expression.stack;
        Assert.assertNotNull(stack);
        String result = expression.expression;
        Assert.assertEquals("", result);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("");
        expression.prepare();
        boolean result = expression.prepared;
        Assert.assertTrue(result);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("");
        ExpressionStack result = expression.getStackCopy();
        Assert.assertNotNull(result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        Object result = expression.evaluate();
        Assert.assertNull(result);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression result = Expression.getReservedFunctions("");
        Assert.assertNull(result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isReservedFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isLogicalFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isMathFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isDateFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isStringFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("");
        FunctionExpression result = expression.getExternalFunction("");
        Assert.assertNull(result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isExternalFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("");
        FunctionExpression result = expression.getFunction("");
        Assert.assertNull(result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("");
        boolean result = expression.isFunction("");
        Assert.assertFalse(result);
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression();
        boolean result = expression.prepared;
        Assert.assertFalse(result);
    }
}