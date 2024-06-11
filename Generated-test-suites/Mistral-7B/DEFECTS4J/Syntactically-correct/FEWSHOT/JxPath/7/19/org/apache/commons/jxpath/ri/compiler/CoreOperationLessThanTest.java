package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg2 = new ConstantExpression(1.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new ConstantExpression(2.0);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNulls() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertNull(result);

        arg1 = new ConstantExpression(null);
        arg2 = new ConstantExpression(1.0);
        op = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = op.computeValue(context);
        assertNull(result);

        arg1 = new ConstantExpression(null);
        arg2 = new ConstantExpression(null);
        op = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = op.computeValue(context);
        assertNull(result);
    }
}