package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertTrue(expression.isContextDependent());

        Expression expression2 = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertFalse(expression2.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return "test";
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertEquals("test", expression.computeValue(null));
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return "test";
            }
        };
        assertEquals("test", expression.compute(null));
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return Collections.singletonList("test").iterator();
            }
        };
        assertTrue(expression.iterate(null).hasNext());
        assertEquals("test", expression.iterate(null).next());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return Collections.singletonList("test").iterator();
            }
        };
        assertTrue(expression.iteratePointers(null).hasNext());
        assertEquals("test", expression.iteratePointers(null).next());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(Collections.singletonList("test").iterator(), new QName(null, "value"), Locale.getDefault());
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.singletonList("test").iterator());
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }
}