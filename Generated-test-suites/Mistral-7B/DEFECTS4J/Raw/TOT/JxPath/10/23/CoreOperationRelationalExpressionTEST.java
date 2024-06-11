package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterators;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoImpl;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.axes.AxisNameTest;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        Expression right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        CoreOperationRelationalExpression expr = new GreaterExpression(new Expression[] {left, right});

        EvalContext context = new EvalContext(new TestNodeIterator());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        expr = new GreaterExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2.5));
        expr = new GreaterExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 2.5)));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterOrEqual() {
        Expression left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        Expression right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        CoreOperationRelationalExpression expr = new GreaterOrEqualExpression(new Expression[] {left, right});

        EvalContext context = new EvalContext(new TestNodeIterator());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        expr = new GreaterOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2.5));
        expr = new GreaterOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 2.5)));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        expr = new GreaterOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 1.5)));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        Expression left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        Expression right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        CoreOperationRelationalExpression expr = new LessExpression(new Expression[] {left, right});

        EvalContext context = new EvalContext(new TestNodeIterator());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        expr = new LessExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2.5));
        expr = new LessExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 2.5)));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessOrEqual() {
        Expression left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        Expression right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2));
        CoreOperationRelationalExpression expr = new LessOrEqualExpression(new Expression[] {left, right});

        EvalContext context = new EvalContext(new TestNodeIterator());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1));
        expr = new LessOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 2.5));
        expr = new LessOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 2.5)));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        right = new LiteralExpression(new NodeInfoImpl(AxisNameTest.SELF, 1.5));
        expr = new LessOrEqualExpression(new Expression[] {left, right});

        context = new EvalContext(new TestNodeIterator());
        context.setValues(Iterators.iter(new NodeInfoImpl(AxisNameTest.SELF, 1.5),
                new NodeInfoImpl(AxisNameTest.SELF, 1.5)));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    private class TestNodeIterator implements NodeIterator {
        private Iterator<NodeInfo> it = IterateIterators.emptyIterator();

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public NodeInfo next() {
            return it.next();
        }
    }

}