package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new CoreNumberExpression(new Double(5.0));
        Expression arg2 = new CoreNumberExpression(new Double(5.0));

        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)).booleanValue());

        arg2 = new CoreNumberExpression(new Double(4.0));
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)).booleanValue());

        arg1 = new CoreNumberExpression(new Double(10.0));
        arg2 = new CoreNumberExpression(new Double(5.0));
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)).booleanValue());

        arg1 = new CoreNumberExpression(new Double(5.0));
        arg2 = new CoreNumberExpression(new Double(10.0));
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNull() {
        Expression arg1 = new CoreNumberExpression(new Double(5.0));
        Expression arg2 = null;

        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNull(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)));

        arg2 = new CoreNumberExpression(new Double(5.0));
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(null, null, null, null)).booleanValue());
    }
}