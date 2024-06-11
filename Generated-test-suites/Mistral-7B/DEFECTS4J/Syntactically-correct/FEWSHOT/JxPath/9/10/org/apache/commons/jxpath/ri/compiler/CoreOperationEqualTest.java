package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_Boolean_Boolean() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(true), new ConstantExpression(true));
        EvalContext context = new EvalContext();
        context.setCurrentNodeInfo(new NodeInfo(Path.EMPTY_PATH, null, null));
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_Boolean_False() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(true), new ConstantExpression(false));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_Integer_Integer() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_Integer_Integer_Different() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_String_String() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression("foo"), new ConstantExpression("foo"));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_String_String_Different() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression("foo"), new ConstantExpression("bar"));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_NodeInfo_NodeInfo() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(new NodeInfo(Path.EMPTY_PATH, null, null)),
                new ConstantExpression(new NodeInfo(Path.EMPTY_PATH, null, null)));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCoreOperationEqual_NodeInfo_NodeInfo_Different() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(new NodeInfo(Path.EMPTY_PATH, null, null)),
                new ConstantExpression(new NodeInfo(Path.EMPTY_PATH, null, null, "differentValue")));
        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }
}