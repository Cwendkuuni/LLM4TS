package org.apache.commons.jxpath.ri.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqualSimpleTypes() {
        Object l = "foo";
        Object r = "foo";
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        l = 1.0;
        r = 1.0;
        op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        l = true;
        r = true;
        op = new CoreOperationCompare(new BooleanConstant(l), new BooleanConstant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }

    @Test
    public void testEqualComplexTypes() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        l = new String("foo");
        r = new String("foo");
        op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        l = new Boolean(true);
        r = new Boolean(true);
        op = new CoreOperationCompare(new BooleanConstant(l), new BooleanConstant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }

    @Test
    public void testEqualPointerTypes() {
        Object l = new Pointer("test:1");
        Object r = new Pointer("test:1");
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }

    @Test
    public void testEqualCollections() {
        Collection l = new ArrayList();
        l.add("foo");
        l.add("bar");

        Collection r = new ArrayList();
        r.add("foo");
        r.add("bar");

        CoreOperationCompare op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }

    @Test
    public void testUnequalSimpleTypes() {
        Object l = "foo";
        Object r = "bar";
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l = 1.0;
        r = 2.0;
        op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l = true;
        r = false;
        op = new CoreOperationCompare(new BooleanConstant(l), new BooleanConstant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);
    }

    @Test
    public void testUnequalComplexTypes() {
        Object l = new Integer(1);
        Object r = new Integer(2);
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l = new String("foo");
        r = new String("bar");
        op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l = new Boolean(true);
        r = new Boolean(false);
        op = new CoreOperationCompare(new BooleanConstant(l), new BooleanConstant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);
    }

    @Test
    public void testUnequalPointerTypes() {
        Object l = new Pointer("test:1");
        Object r = new Pointer("test:2");
        CoreOperationCompare op = new CoreOperationCompare(new Constant(l), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);
    }

    @Test
    public void testUnequalCollections() {
        Collection l = new ArrayList();
        l.add("foo");
        l.add("bar");

        Collection r = new ArrayList();
        r.add("foo");
        r.add("baz");

        CoreOperationCompare op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);
    }

    @Test
    public void testContainsIterator() {
        Object lList = new ArrayList();
        List<Object> l = (List<Object>) lList;
        l.add("foo");
        l.add("bar");

        Object r = "foo";
        CoreOperationCompare op = new CoreOperationCompare(
            new IteratorExpression(l.iterator()), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        r = "bar";
        op = new CoreOperationCompare(new IteratorExpression(l.iterator()), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        r = "baz";
        op = new CoreOperationCompare(new IteratorExpression(l.iterator()), new Constant(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);
    }

    @Test
    public void testFindMatchIterator() {
        Object lList = new ArrayList();
        List<Object> l = (List<Object>) lList;
        l.add("foo1");
        l.add("foo2");
        l.add("bar");

        Object rList = new ArrayList();
        List<Object> r = (List<Object>) rList;
        r.add("foo1");
        r.add("bar");

        CoreOperationCompare op = new CoreOperationCompare(
            new IteratorExpression(l.iterator()), new IteratorExpression(r.iterator()));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        r.add("foo3");
        op = new CoreOperationCompare(
            new IteratorExpression(l.iterator()), new IteratorExpression(r.iterator()));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l.add("foo3");
        op = new CoreOperationCompare(
            new IteratorExpression(l.iterator()), new IteratorExpression(r.iterator()));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }

    @Test
    public void testFindMatchHashSet() {
        Object lList = new ArrayList();
        List<Object> l = (List<Object>) lList;
        l.add("foo1");
        l.add("foo2");
        l.add("bar");

        Object rList = new ArrayList();
        List<Object> r = (List<Object>) rList;
        r.add("foo1");
        r.add("foo2");
        r.add("bar");
        r.add("baz");

        CoreOperationCompare op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);

        r.remove("foo2");
        op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        r.add("foo3");
        op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 1);

        l.add("foo3");
        op = new CoreOperationCompare(
            new CollectionExpression(l), new CollectionExpression(r));
        Assert.assertTrue(op.compare(new EvalContext(), null) == 0);
    }
}