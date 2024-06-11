package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new Double(5.0)), new CoreNumberExpression(new Double(4.0)));
        EvalContext context = new EvalContext(InfoSetUtil.createSingletonInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreNumberExpression(new Double(4.0)), new CoreNumberExpression(new Double(5.0)));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with null arguments
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, new CoreNumberExpression(new Double(5.0)));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreNumberExpression(new Double(5.0)), null);
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}