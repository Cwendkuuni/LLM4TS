package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIter;
import org.apache.commons.jxpath.ri.IterateSequence;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoIterator;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Before
    public void setup() {
        expression = new GreaterThanExpression(new VariableExpression("a"), new VariableExpression("b"));
        context = TestUtil.newContext();
    }

    @Test
    public void testGreaterThan() {
        context.setNodes(new NodeInfo[]{new NodeInfo(1.0), new NodeInfo(2.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanFalse() {
        context.setNodes(new NodeInfo[]{new NodeInfo(1.0), new NodeInfo(1.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanNull() {
        context.setNodes(new Object[]{null, new NodeInfo(1.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanNullTrue() {
        context.setNodes(new Object[]{new NodeInfo(1.0), null});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanIterators() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(2.0, 3.0, 4.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanIteratorsFalse() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(2.0, 1.0, 3.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanIteratorsEmpty() {
        IterateSequence left = TestUtil.newList();
        IterateSequence right = TestUtil.newList(1.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanIteratorsSameOrder() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(1.0, 2.0, 3.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanIteratorsDifferentOrders() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(3.0, 2.0, 1.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        context.setNodes(new NodeInfo[]{new NodeInfo(1.0), new NodeInfo(1.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualFalse() {
        context.setNodes(new NodeInfo[]{new NodeInfo(1.0), new NodeInfo(0.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualNull() {
        context.setNodes(new Object[]{null, new NodeInfo(1.0)});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualNullTrue() {
        context.setNodes(new Object[]{new NodeInfo(1.0), null});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualIterators() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(1.0, 2.0, 3.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualIteratorsFalse() {
        IterateSequence left = TestUtil.newList(1.0, 2.0, 3.0);
        IterateSequence right = TestUtil.newList(0.0, 2.0, 3.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualIteratorsEmptyLeft() {
        IterateSequence left = TestUtil.newList();
        IterateSequence right = TestUtil.newList(1.0);

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualIteratorsEmptyRight() {
        IterateSequence left = TestUtil.newList(1.0);
        IterateSequence right = TestUtil.newList();

        context.setNodes(new Object[]{left, right});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}