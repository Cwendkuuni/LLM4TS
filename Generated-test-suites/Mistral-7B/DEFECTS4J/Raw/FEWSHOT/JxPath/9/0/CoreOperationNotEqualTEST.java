package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.nodes.Node;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualWithBooleanValues() {
        Expression arg1 = new CoreConstantExpression(true);
        Expression arg2 = new CoreConstantExpression(false);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression(false);
        arg2 = new CoreConstantExpression(true);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualWithNullValues() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression(null);
        arg2 = new CoreConstantExpression("not null string");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression("not null string");
        arg2 = new CoreConstantExpression(null);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualWithIntegerValues() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(2);
        arg2 = new CoreConstantExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationNotEqualWithStringValues() {
        Expression arg1 = new CoreConstantExpression("foo");
        Expression arg2 = new CoreConstantExpression("bar");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression("bar");
        arg2 = new CoreConstantExpression("foo");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression("foo");
        arg2 = new CoreConstantExpression("");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression("");
        arg2 = new CoreConstantExpression("foo");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualWithNodes() {
        Node node1 = new Node("node1");
        Node node2 = new Node("node2");

        Expression arg1 = new CoreNodeExpression(node1);
        Expression arg2 = new CoreNodeExpression(node2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }
}