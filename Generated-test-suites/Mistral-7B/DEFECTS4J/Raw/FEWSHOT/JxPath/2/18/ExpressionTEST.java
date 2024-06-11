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

/**
 * Test cases for the Expression class.
 */
public class ExpressionTest {

    /**
     * A test expression that always returns a context-independent value.
     */
    private static class ConstantExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 42;
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }

    /**
     * A test expression that always returns a context-dependent value.
     */
    private static class VariableExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getVariableValue("x");
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }

    /**
     * A test expression that returns a node set based on the current context.
     */
    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer();
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }

        @Override
        public Iterator iterate(EvalContext context) {
            Object result = compute(context);
            return result instanceof EvalContext ? new ValueIterator((EvalContext) result) : ValueUtils.iterate(result);
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            Object result = compute(context);
            return result == null ? Collections.EMPTY_LIST.iterator() : new PointerIterator(ValueUtils.iterate(result), new QName(null, "value"), context.getRootContext().getCurrentNodePointer().getLocale());
        }
    }

    @Test
    public void testIsContextDependent() {
        Expression constant = new ConstantExpression();
        Expression variable = new VariableExpression();
        Expression contextDependent = new ContextDependentExpression();

        Assert.assertFalse(constant.isContextDependent());
        Assert.assertTrue(variable.isContextDependent());
        Assert.assertTrue(contextDependent.isContextDependent());
    }

    @Test
    public void testContextDependentExpression() {
        Expression contextDependent = new ContextDependentExpression();

        EvalContext context = new EvalContext(null, null, null);
        context.setCurrentNodePointer(new NodePointer(new QName("foo"), null, null, null, null, null, null));

        Object result = contextDependent.compute(context);
        Assert.assertNotNull(result);
        Assert.assertEquals(new NodePointer(new QName("foo"), null, null, null, null, null, null), result);

        result = contextDependent.iterate(context).next();
        Assert.assertNotNull(result);
        Assert.assertEquals(new NodePointer(new QName("foo"), null, null, null, null, null, null), result);

        result = contextDependent.iteratePointers(context).next();
        Assert.assertNotNull(result);
        Assert.assertEquals(new NodePointer(new QName("foo"), null, null, null, null, null, null), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveFromIterator() {
        Expression contextDependent = new ContextDependentExpression();
        EvalContext context = new EvalContext(null, null, null);
        Iterator iterator = contextDependent.iterate(context);
        iterator.next();
        iterator.remove();
    }
}