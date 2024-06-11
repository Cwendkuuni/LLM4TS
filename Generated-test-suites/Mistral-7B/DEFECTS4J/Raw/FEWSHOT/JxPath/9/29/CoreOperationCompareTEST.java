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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();

       assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("foo")).evaluate(context));
        assertTrue(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(1.0)).evaluate(context));
        assertTrue(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(true)).evaluate(context));
        assertTrue(new CoreOperationCompare(new Pointer(1), new Pointer(1)).evaluate(context));

        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("foo")).evaluate(context));
        assertTrue(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(1.0)).evaluate(context));
        assertTrue(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(true)).evaluate(context));
        assertTrue(new CoreOperationCompare(new Pointer(1), new Pointer(1)).evaluate(context));

        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("foo")).evaluate(context));
        assertTrue(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(1.0)).evaluate(context));
        assertTrue(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(true)).evaluate(context));
        assertTrue(new CoreOperationCompare(new Pointer(1), new Pointer(1)).evaluate(context));

        Pointer pointer1 = new Pointer(1);
        Pointer pointer2 = new Pointer(1);
        assertTrue(new CoreOperationCompare(pointer1, pointer2).evaluate(context));

        EvalContext context2 = new EvalContext();
        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("bar")).evaluate(context2));
        assertFalse(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(2.0)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(false)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new Pointer(1), new Pointer(2)).evaluate(context2));

        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("bar")).evaluate(context2));
        assertFalse(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(2.0)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(false)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new Pointer(1), new Pointer(2)).evaluate(context2));

        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("bar")).evaluate(context2));
        assertFalse(new CoreOperationCompare(new NumberLiteral(1.0), new NumberLiteral(2.0)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new BooleanLiteral(true), new BooleanLiteral(false)).evaluate(context2));
        assertFalse(new CoreOperationCompare(new Pointer(1), new Pointer(2)).evaluate(context2));

        Pointer pointer3 = new Pointer(1);
        Pointer pointer4 = new Pointer(2);
        assertFalse(new CoreOperationCompare(pointer1, pointer4).evaluate(context));

        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), obj1).evaluate(context));
        assertFalse(new CoreOperationCompare(obj1, obj2).evaluate(context));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext();

        List<Object> list1 = new ArrayList<>();
        list1.add("foo");
        list1.add("bar");

        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringLiteral("foo")).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list1.iterator())).evaluate(context));
        assertFalse(new CoreOperationCompare(new StringLiteral("bar"), new StringIterator(list1.iterator())).evaluate(context));

        List<Object> list2 = new ArrayList<>();
        list2.add("foo");
        list2.add("foo");

        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list2.iterator())).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list2.iterator())).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list2.iterator())).evaluate(context));

        List<Object> list3 = new ArrayList<>();
        list3.add("foo");
        list3.add("bar");

        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list3.iterator())).evaluate(context));
        assertFalse(new CoreOperationCompare(new StringLiteral("bar"), new StringIterator(list3.iterator())).evaluate(context));
        assertFalse(new CoreOperationCompare(new StringLiteral("baz"), new StringIterator(list3.iterator())).evaluate(context));

        List<Object> list4 = new ArrayList<>();
        list4.add("foo");
        list4.add("foo");
        list4.add("foo");
        list4.add("foo");

        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list4.iterator())).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list4.iterator())).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list4.iterator())).evaluate(context));
        assertTrue(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list4.iterator())).evaluate(context));

        List<Object> list5 = new ArrayList<>();
        list5.add("foo");
        list5.add("bar");
        list5.add("baz");

        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list5.iterator())).evaluate(context));
        assertFalse(new CoreOperationCompare(new StringLiteral("bar"), new StringIterator(list5.iterator())).evaluate(context));
        assertFalse(new CoreOperationCompare(new StringLiteral("baz"), new StringIterator(list5.iterator())).evaluate(context));

        List<Object> list6 = new ArrayList<>();

        assertFalse(new CoreOperationCompare(new StringLiteral("foo"), new StringIterator(list6.iterator())).evaluate(context));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext();

        List<Object> list1 = new ArrayList<>();
        list1.add("foo");
        list1.add("bar");
        list1.add("baz");

        List<Object> list2 = new ArrayList<>();
        list2.add("foo");
        list2.add("bar");

        assertTrue(new CoreOperationCompare(new StringIterator(list1.iterator()), new StringIterator(list2.iterator())).evaluate(context));

        List<Object> list3 = new ArrayList<>();
        list3.add("foo");
        list3.add("bar");
        list3.add("baz");

        List<Object> list4 = new ArrayList<>();
        list4.add("foo");
        list4.add("qux");

        assertFalse(new CoreOperationCompare(new StringIterator(list1.iterator()), new StringIterator(list4.iterator())).evaluate(context));

        List<Object> list5 = new ArrayList<>();
        list5.add("foo");
        list5.add("bar");
        list5.add("baz");
        list5.add("qux");

        HashSet set1 = new HashSet();
        set1.add("foo");
        set1.add("bar");

        assertTrue(new CoreOperationCompare(new StringIterator(list1.iterator()), new HashSetIterator(set1.iterator())).evaluate(context));

        HashSet set2 = new HashSet();
        set2.add("qux");

        assertFalse(new CoreOperationCompare(new StringIterator(list1.iterator()), new HashSetIterator(set2.iterator())).evaluate(context));

        List<Object> list6 = new ArrayList<>();
        list6.add("foo");
        list6.add("bar");
        list6.add("baz");
        list6.add("qux");

        HashSet set3 = new HashSet();
        set3.add("foo");
        set3.add("bar");
        set3.add("qux");

        assertTrue(new CoreOperationCompare(new StringIterator(list1.iterator()), new HashSetIterator(set3.iterator())).evaluate(context));
    }

    static class StringIterator implements Iterator<String> {
        private Iterator<String> iterator;

        StringIterator(Iterator<String> it) {
            this.iterator = it;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public String next() {
            return this.iterator.next();
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }

    static class HashSetIterator implements Iterator<Object> {
        private Iterator<Object> iterator;

        HashSetIterator(Iterator<Object> it) {
            this.iterator = it;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Object next() {
            return this.iterator.next();
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }
}