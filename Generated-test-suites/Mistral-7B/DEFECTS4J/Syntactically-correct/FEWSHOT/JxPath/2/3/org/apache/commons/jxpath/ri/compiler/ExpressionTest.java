package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for the Expression class.
 */
public class ExpressionTest {

    private static class TestExpression extends Expression {
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

    @Test
    public void testIsContextDependent() {
        TestExpression testExpression = new TestExpression();
        Assert.assertFalse(testExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext(new NodePointer("root"));
        Object result = testExpression.computeValue(context);
        Assert.assertEquals(42, result);
    }

    @Test
    public void testCompute() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext(new NodePointer("root"));
        Object result = testExpression.compute(context);
        Assert.assertEquals(42, result);
    }

    @Test
    public void testIterate() {
        TestExpression testExpression = new TestExpression();
        List<Object> list = new ArrayList<>();
        list.add(new NodePointer("node1"));
        list.add(new NodePointer("node2"));

        EvalContext context = new EvalContext(new NodePointer("root"), list.iterator());
        Iterator<Object> iterator = testExpression.iterate(context);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(42, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        TestExpression testExpression = new TestExpression();
        List<Pointer> pointers = new ArrayList<>();
        pointers.add(new NodePointer("node1", "value1"));
        pointers.add(new NodePointer("node2", "value2"));

        EvalContext context = new EvalContext(new NodePointer("root"), pointers.iterator());
        Iterator<Pointer> iterator = testExpression.iteratePointers(context);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(pointers.get(0), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateRemove() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext(new NodePointer("root"), new ValueIterator(Collections.EMPTY_LIST.iterator()));
        Iterator<Object> iterator = testExpression.iterate(context);
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratePointersRemove() {
        TestExpression testExpression = new TestExpression();
        EvalContext context = new EvalContext(new NodePointer("root"), new PointerIterator(Collections.EMPTY_LIST.iterator(), new QName(null, "value"), Locale.US));
        Iterator<Pointer> iterator = testExpression.iteratePointers(context);
        iterator.remove();
    }
}