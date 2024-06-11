package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli. Util; //Assuming Util is in the same package
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test cases for Expression class.
 */
public class ExpressionTest {
    @Test
    public void testAbstractExpression() {
        AbstractExpression expression = new AbstractExpression() {
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

        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testExpressionWithContextIndependentComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                //This computeValue method should return a constant value,
                //so the expression is context-independent
                return new Double(42);
            }

            @Override
            public Object compute(EvalContext context) {
                return computeValue(context);
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.emptyIterator();
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return ValueUtils.emptyIterator();
            }
        };

        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testExpressionWithContextDependentComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                //This computeValue method should return a value dependent on the context
                return context.getCurrentNodePointer();
            }

            @Override
            public Object compute(EvalContext context) {
                return computeValue(context);
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.emptyIterator();
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return ValueUtils.emptyIterator();
            }
        };

        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testExpressionWithContextDependentIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new String[]{"a", "b", "c"};
            }

            @Override
            public Object compute(EvalContext context) {
                return computeValue(context);
            }

            @Override
            public Iterator iterate(EvalContext context) {
                Object array = computeValue(context);
                return new ArrayIterator((String[]) array);
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return ValueUtils.emptyIterator();
            }
        };

        Assert.assertFalse(expression.isContextDependent());

        Object[] expected = new Object[]{"a", "b", "c"};
        Iterator iterator = expression.iterate(new EvalContext(new NodePointer(null, null)));

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], iterator.next());
        }

        Assert.assertNull(iterator.hasNext());
    }

    private static class ArrayIterator implements Iterator {
        private Object[] array;

        public ArrayIterator(Object[] array) {
            this.array = array;
        }

        public boolean hasNext() {
            return array.length > 0;
        }

        public Object next() {
            return array[0];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}