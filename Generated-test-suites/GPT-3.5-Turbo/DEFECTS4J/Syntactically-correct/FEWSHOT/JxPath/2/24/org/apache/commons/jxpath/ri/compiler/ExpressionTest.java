package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvaluationContext;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
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

        assertFalse(expression.isContextDependent());
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
                return "TestValue";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = null; // create an instance of EvalContext
        assertEquals("TestValue", expression.computeValue(context));
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
                return "TestCompute";
            }
        };

        EvalContext context = null; // create an instance of EvalContext
        assertEquals("TestCompute", expression.compute(context));
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
                return null;
            }
        };

        EvalContext context = null; // create an instance of EvalContext
        assertFalse(expression.iterate(context).hasNext());
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
                return null;
            }
        };

        EvalContext context = null; // create an instance of EvalContext
        assertFalse(expression.iteratePointers(context).hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator_next() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        assertEquals(null, iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIterator_remove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator_next() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        assertEquals(null, iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIterator_remove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        iterator.remove();
    }
}