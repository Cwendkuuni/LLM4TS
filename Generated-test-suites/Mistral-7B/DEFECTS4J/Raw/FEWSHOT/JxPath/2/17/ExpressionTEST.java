package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for {@link Expression} class.
 */
public class ExpressionTest {

    /**
     * Test for isContextDependent() method.
     */
    @Test
    public void testIsContextDependent() {
        Expression exp1 = new ContextIndependentExpression();
        Expression exp2 = new ContextDependentExpression();

        assertTrue(exp1.isContextDependent());
        assertFalse(exp2.isContextDependent());
    }

    /**
     * Test for computeContextDependent() method of ContextIndependentExpression.
     */
    @Test
    public void testComputeContextDependent_ContextIndependentExpression() {
        ContextIndependentExpression exp = new ContextIndependentExpression();
        boolean contextDependent = exp.computeContextDependent();

        assertFalse(contextDependent);
    }

    /**
     * Test for computeContextDependent() method of ContextDependentExpression.
     */
    @Test
    public void testComputeContextDependent_ContextDependentExpression() {
        ContextDependentExpression exp = new ContextDependentExpression();
        boolean contextDependent = exp.computeContextDependent();

        assertTrue(contextDependent);
    }

    /**
     * Test for computeValue() method of ContextIndependentExpression.
     */
    @Test
    public void testComputeValue_ContextIndependentExpression() {
        ContextIndependentExpression exp = new ContextIndependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        Object result = exp.computeValue(context);

        if (result instanceof Double) {
            assertEquals(1.0, ((Double) result).doubleValue());
        } else {
            fail("Unexpected result type: " + result.getClass().getName());
        }
    }

    /**
     * Test for computeValue() method of ContextDependentExpression.
     */
    @Test
    public void testComputeValue_ContextDependentExpression() {
        ContextDependentExpression exp = new ContextDependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        try {
            exp.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test for compute() method of ContextIndependentExpression.
     */
    @Test
    public void testCompute_ContextIndependentExpression() {
        ContextIndependentExpression exp = new ContextIndependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        Object result = exp.compute(context);

        if (result instanceof Double) {
            assertEquals(1.0, ((Double) result).doubleValue());
        } else {
            fail("Unexpected result type: " + result.getClass().getName());
        }
    }

    /**
     * Test for compute() method of ContextDependentExpression.
     */
    @Test
    public void testCompute_ContextDependentExpression() {
        ContextDependentExpression exp = new ContextDependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        try {
            exp.compute(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test for iterate() method of ContextIndependentExpression.
     */
    @Test
    public void testIterate_ContextIndependentExpression() {
        ContextIndependentExpression exp = new ContextIndependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        Iterator<Object> iterator = exp.iterate(context);

        Object result = iterator.next();

        if (result instanceof Double) {
            assertEquals(1.0, ((Double) result).doubleValue());
        } else {
            fail("Unexpected result type: " + result.getClass().getName());
        }

        assertFalse(iterator.hasNext());
    }

    /**
     * Test for iterate() method of ContextDependentExpression.
     */
    @Test
    public void testIterate_ContextDependentExpression() {
        ContextDependentExpression exp = new ContextDependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        try {
            exp.iterate(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test for iteratePointers() method of ContextIndependentExpression.
     */
    @Test
    public void testIteratePointers_ContextIndependentExpression() {
        ContextIndependentExpression exp = new ContextIndependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        Iterator<Iterator<Pointer>> pointersIterator = exp.iteratePointers(context);

        Iterator<Pointer> pointers = pointersIterator.next();

        assertNull(pointers.next());
        assertFalse(pointers.hasNext());
    }

    /**
     * Test for iteratePointers() method of ContextDependentExpression.
     */
    @Test
    public void testIteratePointers_ContextDependentExpression() {
        ContextDependentExpression exp = new ContextDependentExpression();
        EvalContext context = new EvalContext(null, null, null);

        try {
            exp.iteratePointers(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test for PointerIterator class.
     */
    @Test
    public void testPointerIterator() {
        EvalContext context = new EvalContext(null, null, null);
        Iterator<Pointer> pointers = new Expression.PointerIterator(
                ValueUtils.iterate(new Double(1.0)),
                new QName("test"),
                context.getRootContext().getCurrentNodePointer().getLocale());

        assertIterableEquals(
                Utils.arrayToList(new Pointer[]{new NodePointer(new QName("test"), 1.0, null)}),
                Iterators.asList(pointers)
        );

        try {
            pointers.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //expected
        }
    }

    /**
     * Test for ValueIterator class.
     */
    @Test
    public void testValueIterator() {
        EvalContext context = new EvalContext(null, null, null);
        Iterator<Object> values = new Expression.ValueIterator(
                ValueUtils.iterate(new Double(1.0)));

        assertIterableEquals(
                Utils.arrayToList(new Object[]{1.0}),
                Iterators.asList(values)
        );

        try {
            values.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //expected
        }
    }

    private static class ContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ONE;
        }

        @Override
        public Object compute(EvalContext context) {
            return ONE;
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object compute(EvalContext context) {
            throw new UnsupportedOperationException();
        }
    }
}