package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * Test cases for {@link Expression} class.
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new DummyContextIndependentExpression();
        assertFalse(expression.isContextDependent());

        expression = new DummyContextDependentExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new DummyContextIndependentExpression();
        Object value = expression.computeValue(new EvalContext(null));
        assertEquals(ZERO, value);

        expression = new DummyContextDependentExpression();
        value = expression.computeValue(new EvalContext(null));
        assertNull(value);
    }

    @Test
    public void testCompute() {
        Expression expression = new DummyContextIndependentExpression();
        Object result = expression.compute(new EvalContext(null));
        assertEquals(ZERO, result);

        expression = new DummyContextDependentExpression();
        result = expression.compute(new EvalContext(null));
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateThrowsException() {
        Expression expression = new Expression() {
            @Override
            public Iterator iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        expression.iterate(new EvalContext(null));
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new DummyContextDependentExpression();
        Iterator<Pointer> pointersIterator = expression.iteratePointers(new EvalContext(null));
        assertFalse(pointersIterator.hasNext());
    }

    private static class DummyContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ZERO;
        }

        @Override
        public Object compute(EvalContext context) {
            return ZERO;
        }
    }

    private static class DummyContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }
}