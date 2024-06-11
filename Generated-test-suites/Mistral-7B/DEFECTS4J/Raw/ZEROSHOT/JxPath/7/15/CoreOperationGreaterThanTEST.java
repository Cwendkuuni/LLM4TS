package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = TestUtils.createNumberNode("5.5");
        Expression arg2 = TestUtils.createNumberNode("4.5");

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertNotNull(coreOperationGreaterThan);
        assertEquals(">", coreOperationGreaterThan.getSymbol());
        assertNull(coreOperationGreaterThan.getLeftOperand());
        assertNull(coreOperationGreaterThan.getRightOperand());
    }

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = TestUtils.createNumberNode("10.5");
        Expression arg2 = TestUtils.createNumberNode("5.5");

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = TestUtils.createNumberNode("5.5");
        Expression arg2 = TestUtils.createNumberNode("10.5");

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentsNotFound() {
        Expression arg1 = null;
        Expression arg2 = null;

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }
}