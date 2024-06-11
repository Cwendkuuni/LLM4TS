package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Util;
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
 * Test cases for {@code Expression} class.
 */
public class ExpressionTest {

    // Subclass for testing purposes
    static class TestExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 42;
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return ValueUtils.iterate(computeValue(context));
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testIsContextDependent() {
        TestExpression testExpression = new TestExpression();
        Assert.assertTrue(testExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext() {
            @Override
            public Pointer getCurrentNodePointer() {
                return new NodePointer(new QName(null, "root"), null, null);
            }
        };
        Object value = testExpression.computeValue(context);
        Assert.assertEquals(42, value);
    }

    @Test
    public void testCompute() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext() {
            @Override
            public Pointer getCurrentNodePointer() {
                return new NodePointer(new QName(null, "root"), null, null);
            }
        };
        Object result = testExpression.compute(context);
        Assert.assertEquals(42, result);
    }

    @Test
    public void testIterate() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext() {
            @Override
            public Pointer getCurrentNodePointer() {
                return new NodePointer(new QName(null, "root"), null, null);
            }
        };
        Iterator iterator = testExpression.iterate(context);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(42, iterator.next().getValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext() {
            @Override
            public Pointer getCurrentNodePointer() {
                return new NodePointer(new QName(null, "root"), null, null);
            }
        };
        Iterator iterator = testExpression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());
    }
}