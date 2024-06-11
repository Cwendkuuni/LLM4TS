package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));

        EvalContext context = new EvalContext();

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(1.0));
        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.5), new ConstantExpression(2.0));
        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.5), new ConstantExpression(1.5));
        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(null), new ConstantExpression(1.0));

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(new Double("NaN")), new ConstantExpression(1.0));

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(new Double("NaN")));

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}