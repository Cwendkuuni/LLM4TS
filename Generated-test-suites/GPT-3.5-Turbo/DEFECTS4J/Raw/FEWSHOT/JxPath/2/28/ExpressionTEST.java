package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.JXPathUtil;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.util.BasicNodeSet;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression exp = new Expression() {

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

        assertFalse(exp.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression exp = new Expression() {

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

        EvalContext context = new JXPathContext.RootContext(new JXPathTestCase.TestNodePointer(null));

        assertEquals("value", exp.computeValue(context));
    }

    @Test
    public void testCompute() {
        Expression exp = new Expression() {

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

        EvalContext context = new JXPathContext.RootContext(new JXPathTestCase.TestNodePointer(null));

        assertEquals("result", exp.compute(context));
    }

    @Test
    public void testIterate() {
        Expression exp = new Expression() {

            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new NodeSet(new BasicNodeSet());
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new JXPathContext.RootContext(new JXPathTestCase.TestNodePointer(null));

        Iterator<?> iterator = exp.iterate(context);

        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression exp = new Expression() {

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
                return new NodeSet(new BasicNodeSet());
            }
        };

        EvalContext context = new JXPathContext.RootContext(new JXPathTestCase.TestNodePointer(null));

        Iterator<?> iterator = exp.iteratePointers(context);

        assertTrue(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        iterator.remove();
    }
}