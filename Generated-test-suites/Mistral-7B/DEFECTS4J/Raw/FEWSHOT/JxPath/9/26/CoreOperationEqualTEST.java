package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()) instanceof Boolean);
        assertTrue(((Boolean) coreOperationEqual.computeValue(new EvalContext())).booleanValue());

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression("foo"), new ConstantExpression("foo"));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()) instanceof Boolean);
        assertTrue(((Boolean) coreOperationEqual.computeValue(new EvalContext())).booleanValue());

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
        assertTrue(coreOperationEqual.computeValue(new EvalContext()) instanceof Boolean);
        assertFalse(((Boolean) coreOperationEqual.computeValue(new EvalContext())).booleanValue());

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(null));
        assertNull(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualInvalidArgs() {
        new CoreOperationEqual(null, new ConstantExpression(1));
    }
}