import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ContextDependentExpression();
        Assert.assertTrue(expression.isContextDependent());

        Expression constantExpression = new ConstantExpression();
        Assert.assertFalse(constantExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ContextDependentExpression();
        Object result = expression.computeValue(new EvalContext());
        Assert.assertNotNull(result);

        Expression constantExpression = new ConstantExpression();
        Object constantResult = constantExpression.computeValue(new EvalContext());
        Assert.assertEquals(constantExpression.computeValue(new EvalContext()), constantResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterate_emptyResult() {
        Expression expression = new EmptyExpression();
        expression.iterate(new EvalContext());
    }

    @Test
    public void testIterate_valueIterator() {
        Expression valueExpression = new ValueExpression(new Object());
        Iterator iterator = valueExpression.iterate(new EvalContext());
        Object first = iterator.next();
        Assert.assertNotNull(first);
        Assert.assertEquals(first, valueExpression.computeValue(new EvalContext()));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_pointerIterator() {
        Expression pointerExpression = new PointerExpression(new NodePointer("nodeName", null, null));
        Iterator iterator = pointerExpression.iterate(new EvalContext());
        Object first = iterator.next();
        Assert.assertInstanceOf(Pointer.class, first);
        Pointer firstPointer = (Pointer) first;
        Assert.assertEquals(firstPointer.getValue(), pointerExpression.computeValue(new EvalContext()));
        Assert.assertTrue(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratePointers_emptyResult() {
        Expression expression = new EmptyExpression();
        expression.iteratePointers(new EvalContext());
    }

    @Test
    public void testIteratePointers_valueIterator() {
        Expression valueExpression = new ValueExpression(new Object());
        Iterator iterator = valueExpression.iteratePointers(new EvalContext());
        Assert.assertEquals(Collections.EMPTY_LIST, iterator);
    }

    @Test
    public void testIteratePointers_pointerIterator() {
        Expression pointerExpression = new PointerExpression(new NodePointer("nodeName", null, null));
        Iterator iterator = pointerExpression.iteratePointers(new EvalContext());
        Assert.assertFalse(iterator.hasNext());
        Pointer first = (Pointer) iterator.next();
        Assert.assertEquals(first.getValue(), pointerExpression.computeValue(new EvalContext()));
        Assert.assertTrue(iterator.hasNext());
    }

    static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getNode("contextNode");
        }

        @Override
        public Object compute(EvalContext context) {
            return context.getNode("contextNode").getValue();
        }
    }

    static class ConstantExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return new Object();
        }

        @Override
        public Object compute(EvalContext context) {
            return new Object();
        }
    }

    static class EmptyExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return Collections.EMPTY_LIST.iterator();
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return Collections.EMPTY_LIST.iterator();
        }
    }

    static class ValueExpression extends Expression {
        private Object value;

        public ValueExpression(Object value) {
            this.value = value;
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    static class PointerExpression extends Expression {
        private Pointer pointer;

        public PointerExpression(Pointer pointer) {
            this.pointer = pointer;
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return pointer;
        }

        @Override
        public Object compute(EvalContext context) {
            return pointer.getValue();
        }
    }
}