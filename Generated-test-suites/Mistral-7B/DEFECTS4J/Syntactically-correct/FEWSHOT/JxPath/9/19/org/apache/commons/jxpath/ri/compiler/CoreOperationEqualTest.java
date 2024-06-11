package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new LiteralExpression(new NodeInfo(new Object(), "1"));
        Expression arg2 = new LiteralExpression(new NodeInfo(new Object(), "1"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), null, null);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualDifferentValues() {
        Expression arg1 = new LiteralExpression(new NodeInfo(new Object(), "1"));
        Expression arg2 = new LiteralExpression(new NodeInfo(new Object(), "2"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), null, null);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqualNullArg1() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new NodeInfo(new Object(), "1"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), null, null);
        Object result = coreOperationEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualNullArg2() {
        Expression arg1 = new LiteralExpression(new NodeInfo(new Object(), "1"));
        Expression arg2 = new LiteralExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), null, null);
        Object result = coreOperationEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes() {
        Expression arg1 = new LiteralExpression(new NodeInfo(new Integer(1), "1"));
        Expression arg2 = new LiteralExpression(new NodeInfo(new String("1"), "1s"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), null, null);
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}