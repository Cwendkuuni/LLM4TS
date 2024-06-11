package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.annotations.ExpectedException;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.ConstantExpression;
import org.apache.commons.jxpath.ri.expression.ListExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {arg1, arg2});

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithStrings() {
        Expression arg1 = new ConstantExpression("apple");
        Expression arg2 = new ConstantExpression("banana");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {arg1, arg2});

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfArguments() {
        Expression arg1 = new ConstantExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {arg1});

        EvalContext context = new EvalContext();
        try {
            expression.computeValue(context);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {arg1, arg2});

        EvalContext context = new EvalContext();
        try {
            expression.computeValue(context);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEmptyCollections() {
        Expression arg1 = new ListExpression(new Object[] {});
        Expression arg2 = new ListExpression(new Object[] {});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {arg1, arg2});

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}