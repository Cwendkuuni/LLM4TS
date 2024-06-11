package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        // test if expression is context-dependent
        // when computeContextDependent() returns true
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

        // test if expression is not context-dependent
        // when computeContextDependent() returns false
        expression = new Expression() {
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
        };
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        // test computeValue() method
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return 5; // arbitrary return value for testing
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertEquals(5, expression.computeValue(null));
    }

    @Test
    public void testCompute() {
        // test compute() method
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
                return 5; // arbitrary return value for testing
            }
        };
        assertEquals(5, expression.compute(null));
    }

    @Test
    public void testIterate() {
        // test iterate() method
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
                return Collections.singletonList(5).iterator(); // arbitrary return value for testing
            }
        };
        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        // test iteratePointers() method
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
                return null;
            }
        };
        Iterator iterator = expression.iteratePointers(null);
        assertNotNull(iterator);

        // test if iterator returns empty list when result is null
        expression = new Expression() {
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
        };
        iterator = expression.iteratePointers(null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        // test PointerIterator class
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(Collections.singletonList(5).iterator(), new QName(null, "value"), Locale.ENGLISH);
        assertTrue(pointerIterator.hasNext());
        assertEquals(5, pointerIterator.next());
        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        // test ValueIterator class
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(Collections.singletonList(new Pointer() {
            @Override
            public Object getNode() {
                return 5;
            }

            @Override
            public Object getValue() {
                return 10;
            }

            @Override
            public void setValue(Object value) {
                // do nothing
            }

            @Override
            public int compareChildNodePointers(Pointer pointer1, Pointer pointer2) {
                return 0;
            }

            @Override
            public int compareNodePointers(Pointer pointer1, Pointer pointer2) {
                return 0;
            }
        }).iterator());
        assertTrue(valueIterator.hasNext());
        assertEquals(10, valueIterator.next());
        assertFalse(valueIterator.hasNext());
    }
}