package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(10.0));
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertFalse((Boolean) result);

        arg2 = new LiteralExpression(new Double(5.0));

        result = op.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNegativeNumbers() {
        Expression arg1 = new LiteralExpression(new Double(-5.0));
        Expression arg2 = new LiteralExpression(new Double(-10.0));
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue((Boolean) result);

        arg2 = new LiteralExpression(new Double(-5.0));

        result = op.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithZero() {
        Expression arg1 = new LiteralExpression(new Double(0.0));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertFalse((Boolean) result);

        arg2 = new LiteralExpression(new Double(0.0));

        result = op.computeValue(context);

        assertTrue((Boolean) result);
    }
}