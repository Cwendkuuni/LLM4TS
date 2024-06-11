package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testComputeContextDependent() {
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

        assertTrue(expression.computeContextDependent());
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
                return "test";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertEquals("test", expression.computeValue(null));
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
                return "test";
            }
        };

        assertEquals("test", expression.compute(null));
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
                return Collections.singletonList("test").iterator();
            }
        };

        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
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
                return Collections.singletonList("test").iterator();
            }
        };

        Iterator iterator = expression.iteratePointers(null);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(
                Collections.singleton("test").iterator(), null, null);
        iterator.remove();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(
                Collections.singleton("test").iterator());
        iterator.remove();
    }
}