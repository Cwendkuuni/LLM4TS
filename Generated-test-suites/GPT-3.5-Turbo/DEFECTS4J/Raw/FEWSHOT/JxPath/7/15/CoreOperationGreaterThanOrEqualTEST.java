package org.apache.commons.jxpath.ri.compiler;


import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = null;

        // Test case 1
        arg1 = null;
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = null;
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 2
        arg1 = null;
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 3
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = null;
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 4
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 5
        arg1 = new Expression();
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = null;
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 6
        arg1 = new Expression();
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 7
        arg1 = null;
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = null;
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 8
        arg1 = null;
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 9
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 10
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 11
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 12
        arg1 = new Expression();
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 13
        arg1 = null;
        arg2 = new Expression();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

        // Test case 14
        arg1 = new Expression();
        arg2 = null;
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        assertEquals(null, coreOperationGreaterThanOrEqual.computeValue(context));

    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());

    }
}