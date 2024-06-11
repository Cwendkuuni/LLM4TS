package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {
    
    private class TestExpression extends Expression {
        private boolean isContextDependent;
        private Object value;

        public TestExpression(boolean isContextDependent, Object value) {
            this.isContextDependent = isContextDependent;
            this.value = value;
        }

        @Override
        public boolean computeContextDependent() {
            return isContextDependent;
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

    @Test
    public void testIsContextDependent() {
        Expression expression1 = new TestExpression(true, null);
        Expression expression2 = new TestExpression(false, null);

        assertTrue(expression1.isContextDependent());
        assertFalse(expression2.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        Expression expression1 = new TestExpression(true, "value1");
        Expression expression2 = new TestExpression(false, "value2");

        assertEquals("value1", expression1.computeValue(context));
        assertEquals("value2", expression2.computeValue(context));
    }

    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();

        Expression expression1 = new TestExpression(true, "value1");
        Expression expression2 = new TestExpression(false, "value2");

        assertEquals("value1", expression1.compute(context));
        assertEquals("value2", expression2.compute(context));
    }

    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();

        Expression expression1 = new TestExpression(true, new String[] {"value1", "value2"});
        Expression expression2 = new TestExpression(false, new String[] {"value3", "value4"});

        Iterator iterator1 = expression1.iterate(context);
        Iterator iterator2 = expression2.iterate(context);

        assertTrue(iterator1.hasNext());
        assertEquals("value1", iterator1.next());
        assertTrue(iterator1.hasNext());
        assertEquals("value2", iterator1.next());
        assertFalse(iterator1.hasNext());

        assertTrue(iterator2.hasNext());
        assertEquals("value3", iterator2.next());
        assertTrue(iterator2.hasNext());
        assertEquals("value4", iterator2.next());
        assertFalse(iterator2.hasNext());
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();

        Expression expression1 = new TestExpression(true, new String[] {"value1", "value2"});
        Expression expression2 = new TestExpression(false, new String[] {"value3", "value4"});

        Iterator iterator1 = expression1.iteratePointers(context);
        Iterator iterator2 = expression2.iteratePointers(context);

        assertTrue(iterator1.hasNext());
        assertEquals("value1", iterator1.next());
        assertTrue(iterator1.hasNext());
        assertEquals("value2", iterator1.next());
        assertFalse(iterator1.hasNext());

        assertTrue(iterator2.hasNext());
        assertEquals("value3", iterator2.next());
        assertTrue(iterator2.hasNext());
        assertEquals("value4", iterator2.next());
        assertFalse(iterator2.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Iterator iterator = new Expression.PointerIterator(Collections.singletonList(new Object()).iterator(), new QName(null, "value"), Locale.getDefault());

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Iterator iterator = new Expression.ValueIterator(Collections.singletonList(new Pointer(new Object())).iterator());

        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}