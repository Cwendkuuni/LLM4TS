package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        assertTrue(expression.isContextDependent());

        AbstractExpression contextDependentExpression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };
        assertTrue(contextDependentExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Object();
            }

            @Override
            public Object compute(EvalContext context) {
                return computeValue(context);
            }
        };
        Object result = expression.compute(new EvalContext(null, null));
        assertSame(result, new Object());

        AbstractExpression contextDependentExpression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return context.getRootContext().getCurrentNodePointer();
            }

            @Override
            public Object compute(EvalContext context) {
                return computeValue(context);
            }
        };
        assertNull(contextDependentExpression.compute(new EvalContext(null, null)));
    }

    @Test
    public void testIterate() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.iterate(new Object());
            }
        };
        Iterator iterator = expression.iterate(new EvalContext(null, null));
        assertFalse(iterator.hasNext());

        AbstractExpression contextDependentExpression = new AbstractExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return context.getRootContext().getChildren();
            }
        };
        boolean hasNext = contextDependentExpression.iterate(new EvalContext(null, null)).hasNext();
        assertTrue(hasNext);

        AbstractExpression pointersExpression = new AbstractExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return ValueUtils.iterate(new Object());
            }
        };
        Iterator pointersIterator = pointersExpression.iteratePointers(new EvalContext(null, null));
        assertFalse(pointersIterator.hasNext());

        AbstractExpression contextDependentPointersExpression = new AbstractExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return context.getRootContext().getChildren();
            }
        };
        Iterator pointers = contextDependentPointersExpression.iteratePointers(new EvalContext(null, null));
        assertTrue(pointers.hasNext());
        Pointer pointer = (Pointer) pointers.next();
        assertSame(pointer.getValue(), new Object());
    }
}

class AbstractExpression {
}