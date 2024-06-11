package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberConstant;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new Constant("5");
        args[1] = new Constant("10");

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
        assertTrue(expression instanceof CoreOperationRelationalExpression);
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGreaterThanExpression() {
        Expression[] args = new Expression[2];
        args[0] = new Constant("5");
        args[1] = new Constant("10");

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);

        JXPathContext context = new JXPathContext(new Object[]{5, 10});
        Object result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(false, result);

        expression = new CoreOperationGreaterThan(args);
        context = new JXPathContext(new Object[]{10, 5});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }

    @Test
    public void testGreaterThanOrEqualExpression() {
        Expression[] args = new Expression[2];
        args[0] = new Constant("5");
        args[1] = new Constant("5");

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(args);

        JXPathContext context = new JXPathContext(new Object[]{5, 5});
        Object result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        args[1] = new NumberConstant(6);

        context = new JXPathContext(new Object[]{5, 6});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(false, result);
    }

    @Test
    public void testLessThanExpression() {
        Expression[] args = new Expression[2];
        args[0] = new Constant("5");
        args[1] = new Constant("10");

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(args);

        JXPathContext context = new JXPathContext(new Object[]{5, 10});
        Object result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        args[0] = new NumberConstant(10);
        args[1] = new NumberConstant(5);

        expression = new CoreOperationLessThan(args);
        context = new JXPathContext(new Object[]{10, 5});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(false, result);
    }

    @Test
    public void testLessThanOrEqualExpression() {
        Expression[] args = new Expression[2];
        args[0] = new Constant("5");
        args[1] = new Constant("5");

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(args);

        JXPathContext context = new JXPathContext(new Object[]{5, 5});
        Object result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        args[1] = new NumberConstant(6);

        context = new JXPathContext(new Object[]{5, 6});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        context = new JXPathContext(new Object[]{5, 4});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        context = new JXPathContext(new Object[]{5, 3});
        result = expression.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(false, result);
    }

    @Test
    public void testNullArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertNull(expression);
    }
}