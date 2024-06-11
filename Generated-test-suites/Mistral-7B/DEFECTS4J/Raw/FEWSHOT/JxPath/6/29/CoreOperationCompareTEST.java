package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqualSimple() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(5);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualBoolean() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(true);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualString() {
        Expression left = new LiteralExpression("foo");
        Expression right = new LiteralExpression("foo");
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualNumber() {
        Expression left = new LiteralExpression(5.5);
        Expression right = new LiteralExpression(5.5);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualPointer() {
        Expression left = new LiteralExpression(new Pointer("1"));
        Expression right = new LiteralExpression(new Pointer("1"));
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(null);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalSimple() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(6);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalBoolean() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(false);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalString() {
        Expression left = new LiteralExpression("foo");
        Expression right = new LiteralExpression("bar");
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalNumber() {
        Expression left = new LiteralExpression(5.5);
        Expression right = new LiteralExpression(6.5);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalPointer() {
        Expression left = new LiteralExpression(new Pointer("1"));
        Expression right = new LiteralExpression(new Pointer("2"));
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualCollection() {
        Collection coll1 = new ArrayList();
        coll1.add(new LiteralExpression(5));
        coll1.add(new LiteralExpression(6));

        Collection coll2 = new ArrayList();
        coll2.add(new LiteralExpression(5));
        coll2.add(new LiteralExpression(6));

        Expression left = new CollectionExpression(coll1);
        Expression right = new CollectionExpression(coll2);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testUnequalCollectionDifferentSize() {
        Collection coll1 = new ArrayList();
        coll1.add(new LiteralExpression(5));
        coll1.add(new LiteralExpression(6));

        Collection coll2 = new ArrayList();
        coll2.add(new LiteralExpression(5));

        Expression left = new CollectionExpression(coll1);
        Expression right = new CollectionExpression(coll2);
        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testContains() {
        Collection coll = new ArrayList();
        coll.add(new LiteralExpression("foo"));
        coll.add(new LiteralExpression("bar"));

        Expression left = new CollectionExpression(coll.iterator());
        Object right = new LiteralExpression("foo");

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testContainsFalse() {
        Collection coll = new ArrayList();
        coll.add(new LiteralExpression("foo"));
        coll.add(new LiteralExpression("bar"));

        Expression left = new CollectionExpression(coll.iterator());
        Object right = new LiteralExpression("baz");

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatch() {
        Collection coll1 = new ArrayList();
        coll1.add(new LiteralExpression("foo"));
        coll1.add(new LiteralExpression("bar"));
        coll1.add(new LiteralExpression("baz"));

        Collection coll2 = new ArrayList();
        coll2.add(new LiteralExpression("foo"));
        coll2.add(new LiteralExpression("qux"));

        Expression left = new CollectionExpression(coll1.iterator());
        Expression right = new CollectionExpression(coll2.iterator());

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatchTrue() {
        Collection coll1 = new ArrayList();
        coll1.add(new LiteralExpression("foo"));
        coll1.add(new LiteralExpression("bar"));
        coll1.add(new LiteralExpression("baz"));

        Collection coll2 = new ArrayList();
        coll2.add(new LiteralExpression("foo"));
        coll2.add(new LiteralExpression("foo"));

        Expression left = new CollectionExpression(coll1.iterator());
        Expression right = new CollectionExpression(coll2.iterator());

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualsNullAndLiteral() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression("foo");

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualsLiteralAndNull() {
        Expression left = new LiteralExpression("foo");
        Expression right = new LiteralExpression(null);

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertFalse(coreOperation.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualsNullAndNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(null);

        CoreOperationCompare coreOperation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance());
        assertTrue(coreOperation.evaluate(context).booleanValue());
    }

}