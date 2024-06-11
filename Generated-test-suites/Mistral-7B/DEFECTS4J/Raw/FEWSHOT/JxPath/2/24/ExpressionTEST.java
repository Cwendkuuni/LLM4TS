package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.util.TestUtils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

public class ExpressionTest {
    private static final Expression EMPTY_EXPRESSION = new DummyExpression();

    @Test
    public void testIsContextDependent_Default() {
        Expression expression = new DummyExpression();
        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_ContextDependent() {
        Expression expression = new ContextDependentExpression();
        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_ContextIndependent() {
        Expression expression = new ContextIndependentExpression();
        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ContextIndependentExpression();
        Object result = expression.computeValue(TestUtils.createEvalContext());
        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputeValue_ThrowsException() {
        Expression expression = new DummyExpression();
        expression.computeValue(TestUtils.createEvalContext());
    }

    @Test
    public void testCompute() {
        Expression expression = new ContextIndependentExpression();
        Object result = expression.compute(TestUtils.createEvalContext());
        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompute_ThrowsException() {
        Expression expression = new DummyExpression();
        expression.compute(TestUtils.createEvalContext());
    }

    @Test
    public void testIterate() {
        Expression expression = new IterableExpression();
        Iterator<Object> iterator = expression.iterate(TestUtils.createEvalContext());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterate_ThrowsException() {
        Expression expression = new DummyExpression();
        expression.iterate(TestUtils.createEvalContext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new IterableExpression();
        Iterator<Pointer> pointerIterator = expression.iteratePointers(TestUtils.createEvalContext());
        Assert.assertTrue(pointerIterator.hasNext());
        Assert.assertNotNull(pointerIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratePointers_ThrowsException() {
        Expression expression = new DummyExpression();
        expression.iteratePointers(TestUtils.createEvalContext());
    }

    private static class DummyExpression extends Expression {
        // empty implementation
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }

    private static class ContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "constant value";
        }
    }

    private static class IterableExpression extends Expression {
        @Override
        public Iterator iterate(EvalContext context) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return new Object() {
                        @Override
                        public String toString() {
                            return "iterated value";
                        }
                    };
                }
            };
        }
    }
}