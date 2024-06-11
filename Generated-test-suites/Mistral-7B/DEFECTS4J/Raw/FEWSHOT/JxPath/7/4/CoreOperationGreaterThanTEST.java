package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext(null, null)) instanceof Boolean);
        assertTrue(((Boolean) coreOperationGreaterThan.computeValue(new EvalContext(null, null))).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        Expression arg1 = new LiteralExpression(new Double(3.0));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext(null, null)) instanceof Boolean);
        assertFalse(((Boolean) coreOperationGreaterThan.computeValue(new EvalContext(null, null))).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertNull(coreOperationGreaterThan.computeValue(new EvalContext(null, null)));
    }

    @Test
    public void testCoreOperationGreaterThanWithDifferentTypes() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Integer(3));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext(null, null)) instanceof Boolean);
        assertTrue(((Boolean) coreOperationGreaterThan.computeValue(new EvalContext(null, null))).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithEqualValues() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext(null, null)) instanceof Boolean);
        assertFalse(((Boolean) coreOperationGreaterThan.computeValue(new EvalContext(null, null))).booleanValue());
    }
}