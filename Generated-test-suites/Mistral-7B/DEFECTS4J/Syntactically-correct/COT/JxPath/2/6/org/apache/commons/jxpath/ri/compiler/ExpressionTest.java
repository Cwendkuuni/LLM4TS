import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(false);

        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_contextDependent() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(true);

        Assert.assertTrue(expression.isContextDependent());
    }

    @Test (expected = IllegalStateException.class)
    public void testIsContextDependent_contextDependencyNotKnown() {
        Expression expression = mock(Expression.class);

        expression.isContextDependent();
    }

    @Test
    public void testComputeValue_nullContext() {
        Expression expression = new DummyExpression();

        Assert.assertNull(expression.computeValue(null));
    }

    @Test
    public void testComputeValue_emptyContext() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Object result = expression.computeValue(context);

        Assert.assertNull(result);
    }

    @Test
    public void testComputeValue_singleValue() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Object result = expression.computeValue(context);

        Assert.assertEquals(1, result.hashCode());
    }

    @Test
    public void testComputeValue_nodeSet() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        NodePointer nodePointer = mock(NodePointer.class);
        when(context.getCurrentNodePointer()).thenReturn(nodePointer);

        when(nodePointer.getValue()).thenReturn("test");

        Object result = expression.computeValue(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof NodePointer);
    }

    @Test
    public void testComputeValue_contextDependent_differentContexts() {
        Expression expression = new ContextDependentExpression();
        EvalContext context1 = mock(EvalContext.class);
        EvalContext context2 = mock(EvalContext.class);

        when(context1.getRootContext()).thenReturn(mock(EvalContext.class));
        when(context2.getRootContext()).thenReturn(mock(EvalContext.class));

        Object result1 = expression.computeValue(context1);
        Object result2 = expression.computeValue(context2);

        Assert.assertNotSame(result1, result2);
    }

    @Test (expected = IllegalStateException.class)
    public void testComputeValue_contextDependent_sameContextTwice() {
        Expression expression = new ContextDependentExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        expression.computeValue(context);
        expression.computeValue(context);
    }

    @Test
    public void testCompute_nullContext() {
        Expression expression = new DummyExpression();

        Assert.assertNull(expression.compute(null));
    }

    @Test
    public void testCompute_emptyContext() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Object result = expression.compute(context);

        Assert.assertNull(result);
    }

    @Test
    public void testCompute_singleValue() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Object result = expression.compute(context);

        Assert.assertEquals(1, result.hashCode());
    }

    @Test
    public void testCompute_nodeSet() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        NodePointer nodePointer = mock(NodePointer.class);
        when(context.getCurrentNodePointer()).thenReturn(nodePointer);

        when(nodePointer.getValue()).thenReturn("test");

        Object result = expression.compute(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Iterator);
    }

    @Test
    public void testIterate_nullContext() {
        Expression expression = new DummyExpression();

        Assert.assertNull(expression.iterate(null));
    }

    @Test
    public void testIterate_emptyContext() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Iterator result = expression.iterate(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIterate_singleValue() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Object value = new Integer(1);
        when(expression.computeValue(context)).thenReturn(value);

        Iterator result = expression.iterate(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertEquals(value, result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIterate_nodeSet() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);
        when(context.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.getValue()).thenReturn("test1");

        NodePointer nodePointer3 = mock(NodePointer.class);
        when(nodePointer1.getNextSibling()).thenReturn(nodePointer3);
        when(nodePointer3.getValue()).thenReturn("test2");

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Iterator result = expression.iterate(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertEquals("test1", result.next().toString());
        Assert.assertTrue(result.hasNext());
        Assert.assertEquals("test2", result.next().toString());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_nullContext() {
        Expression expression = new DummyExpression();

        Assert.assertEquals(Collections.emptyList(), expression.iteratePointers(null));
    }

    @Test
    public void testIteratePointers_emptyContext() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Iterator result = expression.iteratePointers(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_singleValue() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        Object value = new Integer(1);
        when(expression.computeValue(context)).thenReturn(value);

        Iterator result = expression.iteratePointers(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertTrue(result.next() instanceof NodePointer);
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_nodeSet() {
        Expression expression = new DummyExpression();
        EvalContext context = mock(EvalContext.class);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);
        when(context.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.getValue()).thenReturn("test1");

        NodePointer nodePointer3 = mock(NodePointer.class);
        when(nodePointer1.getNextSibling()).thenReturn(nodePointer3);
        when(nodePointer3.getValue()).thenReturn("test2");

        when(context.getRootContext()).thenReturn(mock(EvalContext.class));

        Iterator result = expression.iteratePointers(context);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasNext());
        Assert.assertTrue(result.next() instanceof NodePointer);
        Assert.assertTrue(result.next() instanceof NodePointer);
        Assert.assertFalse(result.hasNext());
    }

    private static class DummyExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 1;
        }

        @Override
        public Object compute(EvalContext context) {
            return 1;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return ValueUtils.iterate(1);
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return ValueUtils.iterate(new NodePointer(new QName(null, "test"), null, null));
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer().getValue();
        }
    }
}