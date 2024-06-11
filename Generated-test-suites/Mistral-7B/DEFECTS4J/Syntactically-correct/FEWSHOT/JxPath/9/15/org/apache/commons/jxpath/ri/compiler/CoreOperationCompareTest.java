package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iterator.SimpleIterator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {
    private EvalContext context;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setup() {
        context = new EvalContext();
        arg1 = new LiteralExpression(new Pointer("1"));
        arg2 = new LiteralExpression(new Pointer("1"));
    }

    @Test
    public void testEqual_SameValues() {
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_DifferentValues() {
        arg2 = new LiteralExpression(new Pointer("2"));
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testEqual_SamePointers() {
        Pointer pointer = new Pointer("1");
        arg1 = new LiteralExpression(pointer);
        arg2 = new LiteralExpression(pointer);
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_DifferentPointers() {
        Pointer pointer1 = new Pointer("1");
        Pointer pointer2 = new Pointer("2");
        arg1 = new LiteralExpression(pointer1);
        arg2 = new LiteralExpression(pointer2);
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testEqual_BooleanValues() {
        arg1 = new LiteralExpression(true);
        arg2 = new LiteralExpression(true);
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_BooleanValuesDifferent() {
        arg1 = new LiteralExpression(true);
        arg2 = new LiteralExpression(false);
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testEqual_NullAndNotNull() {
        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(new Pointer("1"));
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testEqual_NullAndNull() {
        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(null);
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_NumberValues() {
        arg1 = new LiteralExpression(new Double(1.0));
        arg2 = new LiteralExpression(new Double(1.0));
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_DifferentNumberValues() {
        arg1 = new LiteralExpression(new Double(1.0));
        arg2 = new LiteralExpression(new Double(2.0));
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testEqual_StringValues() {
        arg1 = new LiteralExpression("foo");
        arg2 = new LiteralExpression("foo");
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testEqual_DifferentStringValues() {
        arg1 = new LiteralExpression("foo");
        arg2 = new LiteralExpression("bar");
        CoreOperationCompare operation = new EqualOperation(arg1, arg2);
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_EmptyCollection_NullValue() {
        Iterator it = Collections.emptyIterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(null));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_EmptyCollection_EmptyString() {
        Iterator it = Collections.emptyIterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(""));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_EmptyCollection_EmptyList() {
        Iterator it = Collections.emptyIterator();
        List list = Collections.emptyList();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(list));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_EmptyCollection_SingleElement() {
        Iterator it = Collections.emptyIterator();
        it = new SimpleIterator(new Object[]{"foo"});
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression("foo"));
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testContains_EmptyCollection_NullPointer() {
        Iterator it = Collections.emptyIterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(null));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_CollectionWithSingleElement_NullValue() {
        List list = new ArrayList();
        list.add("foo");
        Iterator it = list.iterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(null));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_CollectionWithSingleElement_EmptyString() {
        List list = new ArrayList();
        list.add("foo");
        Iterator it = list.iterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(""));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_CollectionWithSingleElement_EmptyList() {
        List list = new ArrayList();
        list.add("foo");
        Iterator it = list.iterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression(Collections.emptyList()));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testContains_CollectionWithSingleElement_SingleElement() {
        List list = new ArrayList();
        list.add("foo");
        Iterator it = list.iterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression("foo"));
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testContains_CollectionWithSingleElement_DifferentString() {
        List list = new ArrayList();
        list.add("foo");
        Iterator it = list.iterator();
        CoreOperationCompare operation = new ContainsOperation(new LiteralExpression(it), new LiteralExpression("bar"));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_EmptySet_EmptySet() {
        HashSet set1 = new HashSet();
        HashSet set2 = new HashSet();
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_EmptySet_SingleElementSet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        HashSet set2 = new HashSet();
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_SingleElementSet_EmptySet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        HashSet set2 = new HashSet();
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set2.iterator()), new LiteralExpression(set1.iterator()));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_SingleElementSet_SingleElementSet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        HashSet set2 = new HashSet();
        set2.add("foo");
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_SingleElementSet_DifferentElementSet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        HashSet set2 = new HashSet();
        set2.add("bar");
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_MultipleElementsSet_EmptySet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        set1.add("bar");
        HashSet set2 = new HashSet();
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set2.iterator()), new LiteralExpression(set1.iterator()));
        assertFalse(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_MultipleElementsSet_SingleElementSet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        set1.add("bar");
        HashSet set2 = new HashSet();
        set2.add("foo");
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertTrue(operation.evaluate(context));
    }

    @Test
    public void testFindMatch_MultipleElementsSet_DifferentElementsSet() {
        HashSet set1 = new HashSet();
        set1.add("foo");
        set1.add("bar");
        HashSet set2 = new HashSet();
        set2.add("baz");
        CoreOperationCompare operation = new FindMatchOperation(new LiteralExpression(set1.iterator()), new LiteralExpression(set2.iterator()));
        assertFalse(operation.evaluate(context));
    }
}