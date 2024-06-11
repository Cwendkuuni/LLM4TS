package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test class for Expression class
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConstantExpression(new Double(1));
        Assert.assertFalse(expression.isContextDependent());

        Expression contextDependentExpression = new ContextDependentExpression();
        Assert.assertTrue(contextDependentExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ConstantExpression(new Double(1));
        Object value = expression.computeValue(new EvalContext());
        Assert.assertEquals(new Double(1), value);

        Expression contextDependentExpression = new ContextDependentExpression();
        value = contextDependentExpression.computeValue(new EvalContext());
        Assert.assertNotNull(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateWithoutEvalContext() {
        Expression expression = new ConstantExpression(new Double(1));
        expression.iterate(null);
    }

    @Test
    public void testIterate() {
        Expression expression = new ConstantExpression(new Double[]{1, 2, 3});
        Iterator<Object> iterator = expression.iterate(new EvalContext());
        Assert.assertEquals(3, iterator.next().hashCode());
        Assert.assertEquals(2, iterator.next().hashCode());
        Assert.assertEquals(1, iterator.next().hashCode());
        Assert.assertFalse(iterator.hasNext());

        expression = new ContextDependentExpression();
        iterator = expression.iterate(new EvalContext());
        Assert.assertEquals(1, iterator.next().hashCode());
        Assert.assertEquals(2, iterator.next().hashCode());
        Assert.assertEquals(3, iterator.next().hashCode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new ConstantExpression(new NodePointer[]{new NodePointer("root", 1), new NodePointer("root", 2)});
        Iterator<Pointer> iterator = expression.iteratePointers(new EvalContext());
        Assert.assertEquals(new NodePointer("root", 1), iterator.next());
        Assert.assertEquals(new NodePointer("root", 2), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        expression = new ContextDependentExpression();
        iterator = expression.iteratePointers(new EvalContext());
        Assert.assertEquals(new NodePointer("root", 1), iterator.next());
        Assert.assertEquals(new NodePointer("root", 2), iterator.next());
        Assert.assertEquals(new NodePointer("root", 3), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test class for ConstantExpression
     */
    public static class ConstantExpression extends Expression {
        private Double value;

        public ConstantExpression(Double value) {
            this.value = value;
        }

        public ConstantExpression(Double[] values) {
            this.value = values[0];
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    /**
     * Test class for ContextDependentExpression
     */
    public static class ContextDependentExpression extends Expression {
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
            return context.getCurrentNodePointer();
        }
    }
}