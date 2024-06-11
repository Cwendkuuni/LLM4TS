package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        SimpleNode left = new SimpleNode(null, 5.0, null);
        SimpleNode right = new SimpleNode(null, 3.0, null);
        EvalContext context = new EvalContext();

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(left, right);
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        SimpleNode left = new SimpleNode(null, 3.0, null);
        SimpleNode right = new SimpleNode(null, 5.0, null);
        EvalContext context = new EvalContext();

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(left, right);
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNull() {
        SimpleNode left = new SimpleNode(null, Double.NaN, null);
        SimpleNode right = new SimpleNode(null, 3.0, null);
        EvalContext context = new EvalContext();

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(left, right);
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new SimpleNode(null, 3.0, null);
        right = new SimpleNode(null, null, null);
        result = coreOperationGreaterThan.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZero() {
        SimpleNode left = new SimpleNode(null, 5.0, null);
        SimpleNode right = new SimpleNode(null, 0.0, null);
        EvalContext context = new EvalContext();

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(left, right);
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithSameValue() {
        SimpleNode left = new SimpleNode(null, 5.0, null);
        SimpleNode right = new SimpleNode(null, 5.0, null);
        EvalContext context = new EvalContext();

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(left, right);
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}