package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
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

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "value";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext evalContext = new EvalContext();
        assertEquals("value", expression.computeValue(evalContext));
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
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
                return "result";
            }
        };

        EvalContext evalContext = new EvalContext();
        assertEquals("result", expression.compute(evalContext));
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
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
                return Collections.singletonList("value").iterator();
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iterate(evalContext);

        assertTrue(iterator.hasNext());
        assertEquals("value", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
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
                return Collections.singletonList("value").iterator();
            }
        };

        EvalContext evalContext = new EvalContext();
        Iterator iterator = expression.iteratePointers(evalContext);

        assertTrue(iterator.hasNext());
        assertEquals(NodePointer.newNodePointer(new QName(null, "value"), "value", Locale.getDefault()), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(Collections.singletonList("value").iterator(), new QName(null, "value"), Locale.getDefault());
        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.singletonList(new Pointer(null)).iterator());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.singletonList("value").iterator());
        iterator.remove();
    }
}