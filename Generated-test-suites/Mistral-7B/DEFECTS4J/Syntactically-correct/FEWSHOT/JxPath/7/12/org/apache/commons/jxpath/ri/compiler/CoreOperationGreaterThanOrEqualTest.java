package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.5), new ConstantExpression(4.5));
        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.NullInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.5), new ConstantExpression(5.5));
        context = new EvalContext(new Object(), new InfoSetUtil.NullInfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.5), new ConstantExpression(5.5));
        context = new EvalContext(new Object(), new InfoSetUtil.NullInfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(null), new ConstantExpression(4.5));
        context = new EvalContext(new Object(), new InfoSetUtil.NullInfoSet());
        assertNull(coreOperationGreaterThanOrEqual.computeValue(context));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.5), new ConstantExpression(null));
        context = new EvalContext(new Object(), new InfoSetUtil.NullInfoSet());
        assertNull(coreOperationGreaterThanOrEqual.computeValue(context));
    }
}