package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreConstantExpression(new NodeInfo("1"));
        Expression arg2 = new CoreConstantExpression(new NodeInfo("2"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        arg1 = new CoreConstantExpression(new NodeInfo("2"));
        arg2 = new CoreConstantExpression(new NodeInfo("1"));
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        arg1 = new CoreConstantExpression(new NodeInfo("1"));
        arg2 = new CoreConstantExpression(new NodeInfo("1"));
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
        assertNull(coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        Expression arg1 = new CoreConstantExpression(new NodeInfo("1"));
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertNull(coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationNotEqualNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstantExpression(new NodeInfo("1"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertNull(coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationNotEqualNullArg2() {
        Expression arg1 = new CoreConstantExpression(new NodeInfo("1"));
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());
    }
}