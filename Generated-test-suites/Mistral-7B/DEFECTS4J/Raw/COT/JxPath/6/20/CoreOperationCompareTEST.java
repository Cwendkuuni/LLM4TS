package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestHelper;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.SimplePath;
import org.apache.commons.jxpath.ri.compiler.SimpleVariable;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.apache.commons.jxpath.ri.iter.StandardIterator;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.Text;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;

public class CoreOperationCompareTest {

    @SuppressWarnings("unchecked")
    private static <T> Expression<T> createExpression(Class<T> clazz, Object value) {
        ExpressionFactory<T> factory = CoreOperationTestHelper.getExpressionFactory();
        return factory.createConstantExpression(value);
    }

    private static Expression<Boolean> createBooleanExpression(boolean value) {
        return createExpression(Boolean.class, value);
    }

    private static Expression<Number> createNumberExpression(double value) {
        return createExpression(Number.class, value);
    }

    private static Expression<String> createStringExpression(String value) {
        return createExpression(String.class, value);
    }

    private static Expression<Iterator> createIteratorExpression(Iterator iterator) {
        SimplePath path = new SimplePath();
        path.add(new SimpleVariable("iterator"));
        return new CoreOperationCompare(
            new CoreOperationTestHelper.ConstantExpressionOperation(path),
            createExpression(Iterator.class, iterator)
        );
    }

    private static Iterator createIteratorWithValues(Object... values) {
        List list = Arrays.asList(values);
        return new StandardIterator(new HashSet(list).iterator());
    }

    @org.junit.Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(String.class, "abc"),
            createExpression(String.class, "abc")
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Boolean() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createBooleanExpression(true),
            createBooleanExpression(true)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Number() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createNumberExpression(1.2),
            createNumberExpression(1.2)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Null() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(String.class, null),
            createExpression(String.class, null)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_SameInstance() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(String.class, "abc"),
            createExpression(String.class, "abc")
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testNotEqual_Boolean() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createBooleanExpression(true),
            createBooleanExpression(false)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testNotEqual_Number() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createNumberExpression(1.2),
            createNumberExpression(2.3)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testNotEqual_NullAndNotNull() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(String.class, null),
            createExpression(String.class, "abc")
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testNotEqual_NullAndNull() {
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(String.class, null),
            createExpression(String.class, null)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Iterator_SameElements() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createIteratorExpression(it),
            createIteratorExpression(it)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Iterator_DifferentIterator() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        Iterator it2 = createIteratorWithValues("d", "e", "f");
        CoreOperationCompare compare = new CoreOperationCompare(
            createIteratorExpression(it1),
            createIteratorExpression(it2)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Iterator_NullAndIterator() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, null),
            createIteratorExpression(it)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testEqual_Iterator_IteratorAndNull() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createIteratorExpression(it),
            createExpression(Iterator.class, null)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testContains() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it),
            createExpression(String.class, "a")
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testContains_EmptyIterator() {
        Iterator it = Collections.EMPTY_ITERATOR;
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it),
            createExpression(String.class, "a")
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testContains_Null() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it),
            createExpression(String.class, null)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testContains_NonMatchingValue() {
        Iterator it = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it),
            createExpression(String.class, "d")
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testFindMatch() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        Iterator it2 = createIteratorWithValues("c", "b", "a");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(Iterator.class, it2)
        );
        assertTrue(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testFindMatch_EmptyIterator1() {
        Iterator it1 = Collections.EMPTY_ITERATOR;
        Iterator it2 = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(Iterator.class, it2)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testFindMatch_EmptyIterator2() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        Iterator it2 = Collections.EMPTY_ITERATOR;
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(Iterator.class, it2)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testFindMatch_NullIterator1() {
        Iterator it2 = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, null),
            createExpression(Iterator.class, it2)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test
    public void testFindMatch_NullIterator2() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(Iterator.class, null)
        );
        assertFalse(compare.compute(new EvalContext(new NodeInfo(new Text("")))));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindMatch_DifferentTypes() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(String.class, "c")
        );
        compare.compute(new EvalContext(new NodeInfo(new Text(""))));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindMatch_NullValues() {
        Iterator it1 = createIteratorWithValues("a", "b", "c");
        CoreOperationCompare compare = new CoreOperationCompare(
            createExpression(Iterator.class, it1),
            createExpression(String.class, null)
        );
        compare.compute(new EvalContext(new NodeInfo(new Text(""))));
    }
}