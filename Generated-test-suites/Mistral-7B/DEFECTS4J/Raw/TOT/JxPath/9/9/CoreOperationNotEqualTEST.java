package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.expr.ExpressionConstant;
import org.apache.commons.jxpath.ri.expr.ExpressionVisitor;
import org.apache.commons.jxpath.ri.expr.Number;
import org.apache.commons.jxpath.ri.expr.StringLiteral;
import org.apache.commons.jxpath.ri.lang.SimpleName;
import org.apache.commons.jxpath.ri.test.BaseJXPathTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest extends BaseJXpathTestCase {

    @Test
    public void testNotEqualString() {
        Expression arg1 = new StringLiteral("abc");
        Expression arg2 = new StringLiteral("def");
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new StringLiteral("abc");
        arg2 = new StringLiteral("abc");
        op = new CoreOperationNotEqual(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testNotEqualNumber() {
        Number arg1 = new Number(1.0);
        Number arg2 = new Number(2.0);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Number(1.0);
        arg2 = new Number(1.0);
        op = new CoreOperationNotEqual(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testNotEqualConstant() {
        Expression arg1 = new ExpressionConstant(1);
        Expression arg2 = new ExpressionConstant(2);
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new ExpressionConstant(1);
        arg2 = new ExpressionConstant(1);
        op = new CoreOperationNotEqual(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithInvalidTypes() {
        Expression arg1 = new StringLiteral("abc");
        Expression arg2 = new ExpressionVisitor();

        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
    }
}