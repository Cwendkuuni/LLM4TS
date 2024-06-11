package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreStringComparisonExpression;
import org.apache.commons.jxpath.ri.compiler.CoreStringComparisonExpression.StringComparisonOperator;
import org.apache.commons.jxpath.ri.compiler.CoreValueComparisonExpression;
import org.apache.commons.jxpath.ri.compiler.CoreValueComparisonExpression.ValueComparisonOperator;
import org.apache.commons.jxpath.ri.model.Context;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 *
 * @author Andras Csanady
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testSimpleComparison() {
        CoreOperationRelationalExpression op = new CoreValueComparisonExpression(ValueComparisonOperator.GT,
                new CoreValueExpression(new Const(5)), new Const(10));
        assertFalse(op.computeValue(createEvalContext(Collections.singletonList(new Node(1)))));
        assertTrue(op.computeValue(createEvalContext(Collections.singletonList(new Node(11)))));
    }

    @Test
    public void testComparisonWithNullValues() {
        CoreOperationRelationalExpression op = new CoreValueComparisonExpression(ValueComparisonOperator.GT,
                new CoreValueExpression(new Const(5)), new Const(null));
        assertFalse(op.computeValue(createEvalContext(Collections.singletonList(new Node(1)))));
        assertFalse(op.computeValue(createEvalContext(Collections.singletonList(new Node(null)))));
    }

    @Test
    public void testComparisonWithSameNullValues() {
        CoreOperationRelationalExpression op = new CoreValueComparisonExpression(ValueComparisonOperator.EQ,
                new CoreValueExpression(new Const(null)), new CoreValueExpression(new Const(null)));
        assertTrue(op.computeValue(createEvalContext(Collections.singletonList(new Node(null)))));
    }

    @Test
    public void testComparisonWithDifferentNullValues() {
        CoreOperationRelationalExpression op = new CoreValueComparisonExpression(ValueComparisonOperator.EQ,
                new CoreValueExpression(new Const(null)), new CoreValueExpression(new Const(new Object())));
        assertFalse(op.computeValue(createEvalContext(Collections.singletonList(new Node(null)))));
    }

    @Test
    public void testComparisonWithNaNValues() {
        CoreOperationRelationalExpression op = new CoreValueComparisonExpression(ValueComparisonOperator.GT,
                new CoreValueExpression(new Const(Double.NaN)), new Const(Double.NaN));
        assertFalse(op.computeValue(createEvalContext(Collections.singletonList(new Node(Double.NaN)))));
    }

    @Test
    public void testComparisonWithIterators() {
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        NodeIterator iter1 = new StubNodeIterator(coll.iterator());
        NodeIterator iter2 = new StubNodeIterator(coll.iterator());
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                iter1, iter2);
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.EQ, new CoreStringExpression("1"), new CoreStringExpression("1")), iter1,
                iter2);
        assertTrue(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.NEQ,
                new CoreStringExpression("1"), new CoreStringExpression("2")), iter1, iter2);
        assertTrue(op.computeValue(createContextWithNodeAtZero(2)));
    }

    @Test
    public void testComparisonWithArrays() {
        Object[] arr1 = new Object[] { 1, 2, 3 };
        Object[] arr2 = new Object[] { 1, 2, 3 };
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new ArrayIterator(arr1), new ArrayIterator(arr2));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new ArrayIterator(arr1),
                new ArrayIterator(arr2));
        assertTrue(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.NEQ,
                new CoreStringExpression("1"), new CoreStringExpression("2")), new ArrayIterator(arr1),
                new ArrayIterator(arr2));
        assertTrue(op.computeValue(createContextWithArrayAtZero(1, 2)));
    }

    @Test
    public void testComparisonWithCollections() {
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new CollectionIterator(coll.iterator()), new CollectionIterator(coll.iterator()));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new CollectionIterator(coll.iterator()),
                new CollectionIterator(coll.iterator()));
        assertTrue(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.NEQ,
                new CoreStringExpression("1"), new CoreStringExpression("2")), new CollectionIterator(coll.iterator()),
                new CollectionIterator(coll.iterator()));
        assertTrue(op.computeValue(createContextWithCollectionAtZero(1, 2)));
    }

    @Test
    public void testComparisonWithLists() {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new ListIterator(list.iterator()), new ListIterator(list.iterator()));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new ListIterator(list.iterator()),
                new ListIterator(list.iterator()));
        assertTrue(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.NEQ,
                new CoreStringExpression("1"), new CoreStringExpression("2")), new ListIterator(list.iterator()),
                new ListIterator(list.iterator()));
        assertTrue(op.computeValue(createContextWithListAtZero(1, 2)));
    }

    @Test
    public void testComparisonWithSets() {
        Collection coll = new HashSet();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new SetIterator(coll.iterator()), new SetIterator(coll.iterator()));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new SetIterator(coll.iterator()),
                new SetIterator(coll.iterator()));
        assertTrue(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.NEQ,
                new CoreStringExpression("1"), new CoreStringExpression("2")), new SetIterator(coll.iterator()),
                new SetIterator(coll.iterator()));
        assertTrue(op.computeValue(createContextWithSetAtZero(1, 2)));
    }

    @Test
    public void testComparisonWithMixedIterators() {
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new CollectionIterator(coll.iterator()), new ArrayIterator(new Object[] { 1, 2, 3 }));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new ArrayIterator(new Object[] { 1, 2, 3 }),
                new CollectionIterator(coll.iterator()));
        assertTrue(op.computeValue(createContextWithArrayAtZero(1, 2)));
    }

    @Test
    public void testComparisonWithMixedIterators2() {
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(
                StringComparisonOperator.NEQ, new CoreStringExpression("1"), new CoreStringExpression("2")),
                new ArrayIterator(new Object[] { 1, 2, 3 }), new CollectionIterator(coll.iterator()));
        assertFalse(op.computeValue(createContext()));
        op = new CoreOperationRelationalExpression(new CoreStringComparisonExpression(StringComparisonOperator.EQ,
                new CoreStringExpression("1"), new CoreStringExpression("1")), new CollectionIterator(coll.iterator()),
                new ArrayIterator(new Object[] { 1, 2, 3 }));
        assertTrue(op.computeValue(createContextWithCollectionAtZero(1, 2)));
    }

    private EvalContext createEvalContext(Collection nodes) {
        InitialContext context = new InitialContext();
        context.setNodes(nodes);
        return new EvalContext(context, new Context());
    }

    private EvalContext createEvalContextWithNodeAtZero(int index) {
        InitialContext context = new InitialContext();
        context.setNodes(Collections.singletonList(new Node(index)));
        return new EvalContext(context, new Context());
    }

    private EvalContext createEvalContextWithArrayAtZero(int index1, int index2) {
        InitialContext context = new InitialContext();
        context.setNodes(new Object[] { new Node(index1), new Node(index2) });
        return new EvalContext(context, new Context());
    }

    private EvalContext createEvalContextWithCollectionAtZero(int index1, int index2) {
        HashSet set = new HashSet();
        set.add(new Node(index1));
        set.add(new Node(index2));
        InitialContext context = new InitialContext();
        context.setNodes(set.iterator());
        return new EvalContext(context, new Context());
    }

    private Context createContext() {
        return new Context();
    }

    private Context createContextWithNodeAtZero(int index) {
        Context context = new Context();
        context.setNodeAt(0, new Node(index));
        return context;
    }

    private Context createContextWithArrayAtZero(int index1, int index2) {
        Context context = new Context();
        context.setNodeAt(0, new Node(index1));
        context.setNodeAt(1, new Node(index2));
        return context;
    }

    private Context createContextWithCollectionAtZero(int index1, int index2) {
        Context context = new Context();
        context.setNodeAt(0, new Node(index1));
        context.setNodeAt(1, new Node(index2));
        return context;
    }

    /**
     * Iterator wrapper for arrays.
     */
    private static class ArrayIterator implements Iterator {

        private final Object[] array;

        private int index;

        public ArrayIterator(Object[] array) {
            this.array = array;
            this.index = 0;
        }

        public boolean hasNext() {
            return index < array.length;
        }

        public Object next() {
            return array[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Iterator wrapper for collections.
     */
    private static class CollectionIterator implements Iterator {

        private final Iterator iterator;

        public CollectionIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            return iterator.next();
        }

        public void remove() {
            iterator.remove();
        }
    }

    /**
     * Iterator wrapper for lists.
     */
    private static class ListIterator implements Iterator {

        private final Iterator iterator;

        private int index;

        public ListIterator(Iterator iterator) {
            this.iterator = iterator;
            this.index = 0;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            Object next = iterator.next();
            index++;
            return next;
        }

        public void remove() {
            iterator.remove();
        }
    }

    /**
     * Iterator wrapper for sets.
     */
    private static class SetIterator implements Iterator {

        private final Iterator iterator;

        public SetIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            return iterator.next();
        }

        public void remove() {
            iterator.remove();
        }
    }

    /**
     * Stub Iterator.
     */
    private static class StubNodeIterator implements Iterator {

        private final Iterator iterator;

        public StubNodeIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            return iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}