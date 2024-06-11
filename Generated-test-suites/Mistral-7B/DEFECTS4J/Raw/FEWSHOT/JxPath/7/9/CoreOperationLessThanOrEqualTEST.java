package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new CoreNumberExpression(1.5);
        Expression arg2 = new CoreNumberExpression(2.5);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertTrue(coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean);
        assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        arg1 = new CoreNumberExpression(1.5);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertTrue(coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean);
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        arg1 = new CoreNumberExpression(-1.5);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertFalse(coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean);

        arg1 = new CoreNumberExpression(null);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNull(coreOperationLessThanOrEqual.computeValue(new EvalContext()));

        arg1 = new CoreNumberExpression(1.5);
        arg2 = new CoreNumberExpression(null);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNull(coreOperationLessThanOrEqual.computeValue(new EvalContext()));
    }
}