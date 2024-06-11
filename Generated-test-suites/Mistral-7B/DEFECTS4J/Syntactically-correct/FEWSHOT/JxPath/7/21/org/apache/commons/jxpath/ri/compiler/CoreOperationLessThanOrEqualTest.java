package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstExpression(1.5), new ConstExpression(2.0));
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", 1.5);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", 2.0);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", 1.5);
        InfoSetUtil.setDoubleValue(context.getInfoset(), "y", 2.1);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", 1.5);
        InfoSetUtil.setDoubleValue(context.getInfoset(), "y", 1.5);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", null);
        result = operation.computeValue(context);
        assertNull(result);

        context = new EvalContext();
        InfoSetUtil.setDoubleValue(context.getInfoset(), "x", 1.5);
        InfoSetUtil.setDoubleValue(context.getInfoset(), "y", null);
        result = operation.computeValue(context);
        assertNull(result);
    }
}