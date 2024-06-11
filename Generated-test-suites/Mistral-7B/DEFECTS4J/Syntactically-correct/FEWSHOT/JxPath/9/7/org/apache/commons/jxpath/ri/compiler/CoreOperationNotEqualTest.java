package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Node;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreConstantExpression(new Node(1));
        Expression arg2 = new CoreConstantExpression(new Node(2));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualTrue() {
        Expression arg1 = new CoreConstantExpression(new Node("foo"));
        Expression arg2 = new CoreConstantExpression(new Node("bar"));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(new Node("foo"));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationNotEqualSame() {
        Expression arg1 = new CoreConstantExpression(new Node(1));
        Expression arg2 = new CoreConstantExpression(new Node(1));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualSameValue() {
        Expression arg1 = new CoreConstantExpression(new Node("foo"));
        Expression arg2 = new CoreConstantExpression(new Node("foo"));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualDifferentValues() {
        Expression arg1 = new CoreConstantExpression(new Node("foo"));
        Expression arg2 = new CoreConstantExpression(new Node("bar"));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualDifferentValuesTypes() {
        Expression arg1 = new CoreConstantExpression(new Node(1));
        Expression arg2 = new CoreConstantExpression(new Node("foo"));
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}