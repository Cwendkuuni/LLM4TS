package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreNumber(new Double(5));
        Expression arg2 = new CoreNumber(new Double(5));
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new NodeInfo(null, null, null, null, null, null));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg2 = new CoreNumber(new Double(6));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        Expression arg1 = new CoreNumber(new Double(5));
        Expression arg2 = null;
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new NodeInfo(null, null, null, null, null, null));
        Object result = op.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualEmptyString() {
        Expression arg1 = new CoreString(new String("foo"));
        Expression arg2 = new CoreString(new String(""));
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new NodeInfo(null, null, null, null, null, null));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualInvalidType() {
        Expression arg1 = new CoreNumber(new Double(5));
        Expression arg2 = new CoreString(new String("foo"));
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new NodeInfo(null, null, null, null, null, null));
        op.computeValue(context);
    }
}