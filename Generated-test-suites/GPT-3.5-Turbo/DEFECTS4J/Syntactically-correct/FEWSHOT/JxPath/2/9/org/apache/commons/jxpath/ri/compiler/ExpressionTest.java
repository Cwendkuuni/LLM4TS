
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setup() {
        expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
    }

    @Test
    public void testIsContextDependent() {
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        assertTrue(expression.computeContextDependent());
    }

    @Test
    public void testIterate() {
        EvalContext context = mock(EvalContext.class);
        Iterator iterator = expression.iterate(context);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = mock(EvalContext.class);
        Iterator iterator = expression.iteratePointers(context);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        QName qname = new QName(null, "value");
        Locale locale = Locale.getDefault();
        Iterator it = Collections.EMPTY_LIST.iterator();

        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(it, qname, locale);

        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testPointerIteratorNext() {
        QName qname = new QName(null, "value");
        Locale locale = Locale.getDefault();
        Iterator it = Collections.singleton(mock(Pointer.class)).iterator();

        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(it, qname, locale);

        assertTrue(pointerIterator.next() instanceof Pointer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Iterator it = Collections.singleton(mock(Pointer.class)).iterator();

        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(it, null, null);
        pointerIterator.remove();
    }

    @Test
    public void testValueIterator() {
        Iterator it = Collections.EMPTY_LIST.iterator();

        Expression.ValueIterator valueIterator = new Expression.ValueIterator(it);

        assertFalse(valueIterator.hasNext());
    }

    @Test
    public void testValueIteratorNext() {
        Object value = new Object();
        Iterator it = Collections.singleton(value).iterator();

        Expression.ValueIterator valueIterator = new Expression.ValueIterator(it);

        assertEquals(value, valueIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Iterator it = Collections.singleton(new Object()).iterator();

        Expression.ValueIterator valueIterator = new Expression.ValueIterator(it);
        valueIterator.remove();
    }
}
