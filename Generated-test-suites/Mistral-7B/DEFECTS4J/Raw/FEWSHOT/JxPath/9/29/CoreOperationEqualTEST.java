package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypes() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(new String("1"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithNullArg1() {
        Expression arg1 = new LiteralNode(null);
        Expression arg2 = new LiteralNode(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithNullArg2() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentNodes() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(new Integer(2));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithSameNodesDifferentValues() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(new Integer(2));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context1 = new EvalContext();
        context1.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result1 = coreOperationEqual.computeValue(context1);

        assertFalse(result1 instanceof Boolean);
        assertNull(result1);

        EvalContext context2 = new EvalContext();
        context2.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result2 = coreOperationEqual.computeValue(context2);

        assertFalse(result2 instanceof Boolean);
        assertNull(result2);
    }

    @Test
    public void testCoreOperationEqualWithSameNodesSameValues() {
        Expression arg1 = new LiteralNode(new Integer(1));
        Expression arg2 = new LiteralNode(new Integer(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfo("root", null, null, null, null, null, null));

        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}