package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Expression.
 */
public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setUp() {
        context = new EvalContext(null, null, null, null);
    }

    @Test
    public void testIsContextDependent() {
        Expression expression = new StubExpression();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new StubExpression();
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithContext() {
        Expression expression = new StubExpressionWithValue();
        Object result = expression.computeValue(context);
        assertEquals("expected value", expression.getValue(), result);
    }

    @Test
    public void testCompute() {
        Expression expression = new StubExpression();
        Object result = expression.compute(context);
        assertNull(result);
    }

    @Test
    public void testComputeWithContext() {
        Expression expression = new StubExpressionWithValue();
        Object result = expression.compute(context);
        assertEquals("expected value", expression.getValue(), result);
    }

    @Test
    public void testIterate() {
        Expression expression = new StubExpressionWithCollection();
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        Object next = iterator.next();
        assertEquals("expected value", expression.getValue(), next);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new StubExpressionWithCollectionOfPointers();
        Iterator<Pointer> iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        Pointer next = iterator.next();
        assertNotNull(next);
        assertEquals("expected value", expression.getValue(), next.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateRemove() {
        Expression expression = new StubExpressionWithCollection();
        Iterator iterator = expression.iterate(context);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratePointersRemove() {
        Expression expression = new StubExpressionWithCollectionOfPointers();
        Iterator<Pointer> iterator = expression.iteratePointers(context);
        iterator.remove();
    }

    private static class StubExpression implements Expression {
        private Object value;

        public StubExpression() {
            this.value = null;
        }

        public StubExpression(Object value) {
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

        @Override
        public Iterator iterate(EvalContext context) {
            return ValueUtils.EMPTY_ITERATOR;
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return ValueUtils.EMPTY_ITERATOR;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class StubExpressionWithValue extends StubExpression {
        private StubExpressionWithValue(Object value) {
            super(value);
        }
    }

    private static class StubExpressionWithCollection extends StubExpression {
        private Object[] values = new Object[1];

        {
            values[0] = new Integer(42);
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return values;
        }

        @Override
        public Object compute(EvalContext context) {
            return values;
        }
    }

    private static class StubExpressionWithCollectionOfPointers extends StubExpression {
        private Pointer[] pointers = new Pointer[1];

        {
            pointers[0] = new NodePointer(new QName("test"), "test", new Locale("en", "US"));
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return pointers;
        }

        @Override
        public Object compute(EvalContext context) {
            return pointers;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return ValueUtils.iterate(pointers);
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return new PointerIterator(ValueUtils.iterate(pointers),
                    new QName(null, "value"), context.getRootContext().getCurrentNodePointer().getLocale());
        }
    }
}