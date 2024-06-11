package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;

public class CoreOperationLessThanTest extends AbstractJxPathTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(1.5, getTypeManager());
        Expression arg2 = new LiteralExpression(2.5, getTypeManager());
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(1.5, getTypeManager());
        arg2 = new LiteralExpression(1.5, getTypeManager());

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidArguments() {
        Expression arg1 = new LiteralExpression("invalid", getTypeManager());
        Expression arg2 = new LiteralExpression(2.5, getTypeManager());

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @org.junit.Test
    public void testCoreOperationLessThanWithNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN, getTypeManager());
        Expression arg2 = new LiteralExpression(Double.NaN, getTypeManager());

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertNull(result);
    }

    @org.junit.Test
    public void testCoreOperationLessThanWithInfiniteValues() {
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY, getTypeManager());
        Expression arg2 = new LiteralExpression(-1.0, getTypeManager());

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(-Double.POSITIVE_INFINITY, getTypeManager());
        arg2 = new LiteralExpression(1.0, getTypeManager());

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationLessThanWithZeroAndNegativeValues() {
        Expression arg1 = new LiteralExpression(0.0, getTypeManager());
        Expression arg2 = new LiteralExpression(-1.0, getTypeManager());

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(-1.0, getTypeManager());
        arg2 = new LiteralExpression(0.0, getTypeManager());

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}