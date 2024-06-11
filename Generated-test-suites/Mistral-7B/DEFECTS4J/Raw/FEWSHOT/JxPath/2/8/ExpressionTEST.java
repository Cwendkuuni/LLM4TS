package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

    @Test
    public void testAbstractExpression() {
        Expression expr = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                // Placeholder for subclass implementation
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // Placeholder for subclass implementation
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // Placeholder for subclass implementation
                return null;
            }
        };

        assertFalse(expr.isContextDependent());
    }

    @Test
    public void testContextDependentExpression() {
        Expression expr = new ContextDependentExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };

        assertTrue(expr.isContextDependent());
    }

    @Test
    public void testContextIndependentExpression() {
        Expression expr = new ContextIndependentExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        assertFalse(expr.isContextDependent());
    }

    @Test
    public void testEvaluateValueContextDependentExpression() {
        Expression expr = new ContextDependentExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return context.getCurrentNodePointer();
            }
        };

        EvalContext context = new EvalContext(null, null, null, null);
        assertNull(expr.computeValue(context));

        context.setCurrentNodePointer(new NodePointer("root", null, null));
        Pointer nodePointer = new NodePointer("child", null, null);
        context.setCurrentNodePointer(nodePointer);
        assertNotNull(expr.computeValue(context));
    }

    @Test
    public void testEvaluateValueContextIndependentExpression() {
        Expression expr = new ContextIndependentExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return ZERO;
            }
        };

        EvalContext context1 = new EvalContext(null, null, null, null);
        assertEquals(ZERO, expr.computeValue(context1));

        EvalContext context2 = new EvalContext(null, null, null, null);
        assertEquals(ZERO, expr.computeValue(context2));
    }

    @Test
    public void testEvaluateExpressionContextDependent() {
        Expression expr = new ContextDependentExpression() {
            @Override
            public Object compute(EvalContext context) {
                return context.getCurrentNodePointer();
            }
        };

        EvalContext context = new EvalContext(null, null, null, null);
        assertNull(expr.compute(context));

        context.setCurrentNodePointer(new NodePointer("root", null, null));
        Pointer nodePointer = new NodePointer("child", null, null);
        context.setCurrentNodePointer(nodePointer);
        assertNotNull(expr.compute(context));
    }

    @Test
    public void testEvaluateExpressionContextIndependent() {
        Expression expr = new ContextIndependentExpression() {
            @Override
            public Object compute(EvalContext context) {
                return ZERO;
            }
        };

        EvalContext context1 = new EvalContext(null, null, null, null);
        assertEquals(ZERO, expr.compute(context1));

        EvalContext context2 = new EvalContext(null, null, null, null);
        assertEquals(ZERO, expr.compute(context2));
    }

    @Test
    public void testIterateContextDependentExpression() {
        Expression expr = new ContextDependentExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return context.getCurrentNodePointer().getChildren().iterator();
            }
        };

        EvalContext context = new EvalContext(null, null, null, null);
        assertNotNull(expr.iterate(context).hasNext());

        context.setCurrentNodePointer(new NodePointer("root", null, null));
        Pointer nodePointer = new NodePointer("child1", null, null);
        context.setCurrentNodePointer(nodePointer);
        Pointer child1 = (Pointer) expr.iterate(context).next();
        assertEquals(nodePointer, child1);

        Pointer child2 = (Pointer) expr.iterate(context).next();
        assertNotNull(child2);

        context.setCurrentNodePointer(null);
        assertNull(expr.iterate(context).hasNext());
    }

    @Test
    public void testIterateContextIndependentExpression() {
        Expression expr = new ContextIndependentExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return Collections.singletonList(ZERO).iterator();
            }
        };

        EvalContext context1 = new EvalContext(null, null, null, null);
        Iterator iterator1 = expr.iterate(context1);
        assertTrue(iterator1.hasNext());
        assertEquals(ZERO, iterator1.next());
        assertFalse(iterator1.hasNext());

        EvalContext context2 = new EvalContext(null, null, null, null);
        Iterator iterator2 = expr.iterate(context2);
        assertTrue(iterator2.hasNext());
        assertEquals(ZERO, iterator2.next());
        assertFalse(iterator2.hasNext());
    }

    @Test
    public void testIteratePointersContextDependentExpression() {
        Expression expr = new ContextDependentExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return context.getCurrentNodePointer().getChildren().iterator();
            }
        };

        EvalContext context = new EvalContext(null, null, null, null);
        Iterator iterator = expr.iteratePointers(context);
        assertNotNull(iterator.hasNext());

        Pointer pointer = (Pointer) iterator.next();
        assertNotNull(pointer);
        assertNull(pointer.getValue());

        iterator = expr.iteratePointers(context);
        Pointer nextPointer = (Pointer) iterator.next();
        assertNotNull(nextPointer);
        assertNull(nextPointer.getValue());

        context.setCurrentNodePointer(null);
        iterator = expr.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersContextIndependentExpression() {
        Expression expr = new ContextIndependentExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return Collections.singletonList(new NodePointer("constant", null, null)).iterator();
            }
        };

        EvalContext context1 = new EvalContext(null, null, null, null);
        Iterator iterator1 = expr.iteratePointers(context1);
        assertTrue(iterator1.hasNext());
        Pointer pointer1 = (Pointer) iterator1.next();
        assertNotNull(pointer1);
        assertNotNull(pointer1.getValue());

        EvalContext context2 = new EvalContext(null, null, null, null);
        Iterator iterator2 = expr.iteratePointers(context2);
        assertTrue(iterator2.hasNext());
        Pointer pointer2 = (Pointer) iterator2.next();
        assertNotNull(pointer2);
        assertNotNull(pointer2.getValue());

        assertEquals(pointer1, pointer2);
    }
}