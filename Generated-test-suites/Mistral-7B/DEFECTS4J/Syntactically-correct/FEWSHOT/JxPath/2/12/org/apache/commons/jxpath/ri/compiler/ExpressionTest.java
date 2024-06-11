package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setup() {
        context = new EvalContext(new NodePointer("root", null), null, null, new Locale("en", "US"));
    }

    @Test
    public void testComputeValue_ReturnsFirstElementOfNodeSet() {
        Iterator nodeIterator = ValueUtils.iterate(new Object[]{new Pointer("n1"), new Pointer("n2")});
        Expression expression = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return nodeIterator.next();
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Pointer);
        assertSame(result, new Pointer("n1"));
    }

    @Test
    public void testComputeValue_ReturnsSimpleValue() {
        Expression expression = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "foo";
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Object result = expression.computeValue(context);
        assertEquals("foo", result);
    }

    @Test
    public void testCompute_ReturnsSimpleValue() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "foo";
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Object result = expression.compute(context);
        assertEquals("foo", result);
    }

    @Test
    public void testIterate_ReturnsEmptyIterator() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Iterator iterator = expression.iterate(context);
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testIterate_ReturnsNodeSetIterator() {
        Iterator nodeIterator = ValueUtils.iterate(new Object[]{new Pointer("n1"), new Pointer("n2")});
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return nodeIterator;
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Iterator resultIterator = expression.iterate(context);
        assertTrue(resultIterator.hasNext());
        assertTrue(resultIterator.next() instanceof Pointer);
        assertTrue(resultIterator.hasNext());
        assertTrue(resultIterator.next() instanceof Pointer);
        assertFalse(resultIterator.hasNext());
    }

    @Test
    public void testIteratePointers_ReturnsEmptyIterator() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Iterator iterator = expression.iteratePointers(context);
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testIteratePointers_ReturnsNodePointerIterator() {
        Iterator nodeIterator = ValueUtils.iterate(new Object[]{new Pointer("n1"), new Pointer("n2")});
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return nodeIterator;
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        Iterator resultIterator = expression.iteratePointers(context);
        assertTrue(resultIterator.hasNext());
        assertTrue(resultIterator.next() instanceof NodePointer);
        assertTrue(resultIterator.hasNext());
        assertTrue(resultIterator.next() instanceof NodePointer);
        assertFalse(resultIterator.hasNext());
    }

    @Test
    public void testIsContextDependent_ReturnsFalseForStaticExpression() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "foo";
            }

            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_ReturnsTrueForContextDependentExpression() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return context.getCurrentNodePointer();
            }

            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_CachesResult() {
        Expression expression = new Expression() {
            private boolean contextDependent;

            @Override
            public Object compute(EvalContext context) {
                return context.getCurrentNodePointer();
            }

            @Override
            public boolean computeContextDependent() {
                if (!contextDependencyKnown) {
                    contextDependencyKnown = true;
                    contextDependent = context.getCurrentNodePointer() != null;
                }
                return contextDependent;
            }
        };

        assertTrue(expression.isContextDependent());
        assertTrue(expression.isContextDependent());
    }
}