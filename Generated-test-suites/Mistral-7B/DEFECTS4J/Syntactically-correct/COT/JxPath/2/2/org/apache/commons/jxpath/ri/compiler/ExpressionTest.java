import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(true);

        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependentfalse() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(false);

        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);
        Object result = new Object();

        when(expression.computeValue(context)).thenReturn(result);

        Object computedValue = expression.computeValue(context);
        Assert.assertEquals(result, computedValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputeValueUnsupportedOperationException() {
        Expression expression = mock(Expression.class);

        expression.computeValue(null);
    }

    @Test
    public void testCompute() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);
        Object result = new Object();

        when(expression.compute(context)).thenReturn(result);

        Object computed = expression.compute(context);
        Assert.assertEquals(result, computed);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputeUnsupportedOperationException() {
        Expression expression = mock(Expression.class);

        expression.compute(null);
    }

    @Test
    public void testIterate() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);
        Object[] values = new Object[]{new Object(), new Object()};
        Iterator<Object> iterator = ValueUtils.iterate(values);

        when(expression.iterate(context)).thenReturn(iterator);

        Iterator<Object> result = expression.iterate(context);

        Assert.assertEquals(iterator.next(), result.next());
        Assert.assertEquals(iterator.next(), result.next());
    }

    @Test
    public void testIterateEmpty() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(expression.iterate(context)).thenReturn(Collections.emptyIterator());

        Iterator<Object> result = expression.iterate(context);

        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);
        NodePointer nodePointer1 = NodePointer.create("/test/node1");
        NodePointer nodePointer2 = NodePointer.create("/test/node2");
        Iterator<NodePointer> iterator = new Iterator<NodePointer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer next() {
                return (NodePointer) (hasNext() ? nodePointer1 : nodePointer2);
            }
        };

        when(expression.iteratePointers(context)).thenReturn(iterator);

        Iterator<NodePointer> result = expression.iteratePointers(context);

        Assert.assertEquals(nodePointer1, result.next());
        Assert.assertEquals(nodePointer2, result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointersEmpty() {
        Expression expression = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(expression.iteratePointers(context)).thenReturn(Collections.emptyList().iterator());

        Iterator<NodePointer> result = expression.iteratePointers(context);

        Assert.assertFalse(result.hasNext());
    }
}