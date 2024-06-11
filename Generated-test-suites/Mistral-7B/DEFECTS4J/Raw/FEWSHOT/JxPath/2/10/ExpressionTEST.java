package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test cases for the Expression class.
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        AbstractExpression exp1 = new FooExpression();
        AbstractExpression exp2 = new BarExpression();

        Assert.assertFalse(exp1.isContextDependent());
        Assert.assertTrue(exp2.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        FooExpression exp1 = new FooExpression();
        BarExpression exp2 = new BarExpression();

        EvalContext context = new EvalContext(null, null);

        Object result1 = exp1.computeValue(context);
        Assert.assertNotNull(result1);
        Assert.assertTrue(result1 instanceof Number);
        Assert.assertEquals(1.0, ((Number) result1).doubleValue(), 0.0);

        Object result2 = exp2.computeValue(context);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result2 instanceof Pointer);
        Pointer pointer = (Pointer) result2;
        Assert.assertNotNull(pointer.getNode());
        Assert.assertEquals("foo", pointer.getNode().getNodeName());
    }

    @Test
    public void testCompute() {
        FooExpression exp1 = new FooExpression();
        BarExpression exp2 = new BarExpression();

        EvalContext context = new EvalContext(null, null);

        Object result1 = exp1.compute(context);
        Assert.assertNotNull(result1);
        Assert.assertTrue(result1 instanceof Number);
        Assert.assertEquals(1.0, ((Number) result1).doubleValue(), 0.0);

        Object result2 = exp2.compute(context);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result2 instanceof Pointer);
        Pointer pointer = (Pointer) result2;
        Assert.assertNotNull(pointer.getNode());
        Assert.assertEquals("foo", pointer.getNode().getNodeName());
    }

    @Test
    public void testIterate() {
        FooExpression exp1 = new FooExpression();
        BarExpression exp2 = new BarExpression();

        EvalContext context = new EvalContext(null, null);

        Iterator iterator1 = exp1.iterate(context);
        Assert.assertNotNull(iterator1);
        Assert.assertTrue(iterator1.hasNext());
        Assert.assertTrue(iterator1.next() instanceof Number);
        Assert.assertFalse(iterator1.hasNext());

        Iterator iterator2 = exp2.iterate(context);
        Assert.assertNotNull(iterator2);
        Assert.assertTrue(iterator2.hasNext());
        Assert.assertTrue(iterator2.next() instanceof Pointer);
        Pointer pointer = (Pointer) iterator2.next();
        Assert.assertNotNull(pointer.getNode());
        Assert.assertEquals("bar", pointer.getNode().getNodeName());
        Assert.assertFalse(iterator2.hasNext());
    }

    @Test
    public void testIteratePointers() {
        FooExpression exp1 = new FooExpression();
        BarExpression exp2 = new BarExpression();

        EvalContext context = new EvalContext(null, null);

        Iterator iterator1 = exp1.iteratePointers(context);
        Assert.assertNotNull(iterator1);
        Assert.assertFalse(iterator1.hasNext());

        Iterator iterator2 = exp2.iteratePointers(context);
        Assert.assertNotNull(iterator2);
        Assert.assertTrue(iterator2.hasNext());
        Pointer pointer = (Pointer) iterator2.next();
        Assert.assertNotNull(pointer.getNode());
        Assert.assertEquals("bar", pointer.getNode().getNodeName());
        Assert.assertTrue(iterator2.hasNext());
        pointer = (Pointer) iterator2.next();
        Assert.assertNotNull(pointer.getNode());
        Assert.assertEquals("baz", pointer.getNode().getNodeName());
        Assert.assertFalse(iterator2.hasNext());
    }

    private static class FooExpression extends Expression {
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

        @Override
        public Iterator iterate(EvalContext context) {
            return new ValueIterator(Utils.emptyIterator());
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return Collections.emptyList().iterator();
        }
    }

    private static class BarExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return new Pointer(new NodePointer("foo"));
        }

        @Override
        public Object compute(EvalContext context) {
            return new Pointer(new NodePointer("foo"));
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return new ValueIterator(ValueUtils.iterate(new Pointer(new NodePointer("bar"))));
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return ValueUtils.iterate(new Pointer(new NodePointer("bar"))).iterator();
        }
    }
}