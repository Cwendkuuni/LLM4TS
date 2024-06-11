package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.InfoSetUtil;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare op = new CoreOperationCompare(new Const(1), new Const(1));
        assertNotNull(op);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare op = new CoreOperationCompare(new Const(1), new Const(1));
        assertEquals(2, op.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare op = new CoreOperationCompare(new Const(1), new Const(1));
        assertTrue(op.isSymmetric());
    }

    @Test
    public void testEqualNullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const("foo"), new Const("bar"));
        assertFalse(op.equal(context, new Const(null), new Const(null)));
    }

    @Test
    public void testEqualNullLeft() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const(null), new Const("bar"));
        assertFalse(op.equal(context, new Const(null), new Const("bar")));
    }

    @Test
    public void testEqualNullRight() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const("foo"), new Const(null));
        assertFalse(op.equal(context, new Const("foo"), new Const(null)));
    }

    @Test
    public void testEqualPrimitiveTypes() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const(1), new Const(1));
        assertTrue(op.equal(context, new Const(1), new Const(1)));
    }

    @Test
    public void testEqualBooleanValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const(true), new Const(true));
        assertTrue(op.equal(context, new Const(true), new Const(true)));
    }

    @Test
    public void testEqualDifferentBooleanValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const(true), new Const(false));
        assertFalse(op.equal(context, new Const(true), new Const(false)));
    }

    @Test
    public void testEqualStrings() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const("foo"), new Const("foo"));
        assertTrue(op.equal(context, new Const("foo"), new Const("foo")));
    }

    @Test
    public void testEqualDifferentStrings() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const("foo"), new Const("bar"));
        assertFalse(op.equal(context, new Const("foo"), new Const("bar")));
    }

    @Test
    public void testEqualPointers() {
        EvalContext context = new EvalContext();
        Pointer p1 = new Pointer(new String("foo"));
        Pointer p2 = new Pointer(new String("foo"));
        CoreOperationCompare op = new CoreOperationCompare(new Const(p1), new Const(p2));
        assertTrue(op.equal(context, new Const(p1), new Const(p2)));
    }

    @Test
    public void testEqualDifferentPointers() {
        EvalContext context = new EvalContext();
        Pointer p1 = new Pointer(new String("foo"));
        Pointer p2 = new Pointer(new String("bar"));
        CoreOperationCompare op = new CoreOperationCompare(new Const(p1), new Const(p2));
        assertFalse(op.equal(context, new Const(p1), new Const(p2)));
    }

    @Test
    public void testEqualCollections() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(1))));
        assertTrue(op.equal(context, new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(1)))));
    }

    @Test
    public void testEqualDifferentCollectionsSize() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(1)), new Const(2)));
        assertFalse(op.equal(context, new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(1)), new Const(2))));
    }

    @Test
    public void testEqualDifferentCollections() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(2))));
        assertFalse(op.equal(context, new List(Collections.singletonList(new Const(1))), new List(Collections.singletonList(new Const(2)))));
    }

    @Test
    public void testContainsIteratorNullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.emptyIterator()), new Const(null));
        assertFalse(op.contains(context, new Const(null)));
    }

    @Test
    public void testContainsIteratorNullIterator() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new Const(null), new IteratorWrapper(Collections.emptyIterator()));
        assertFalse(op.contains(context, new Const(null)));
    }

    @Test
    public void testContainsIteratorEmptyCollections() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.emptyIterator()), new IteratorWrapper(Collections.emptyIterator()));
        assertFalse(op.contains(context, new Const(null)));
    }

    @Test
    public void testContainsIteratorMatched() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new Const(1));
        assertTrue(op.contains(context, new Const(1)));
    }

    @Test
    public void testContainsIteratorNotMatched() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new Const(2));
        assertFalse(op.contains(context, new Const(2)));
    }

    @Test
    public void testFindMatchNullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()));
        HashSet left = new HashSet();
        assertFalse(op.findMatch(context, left, new IteratorWrapper(Collections.emptyIterator())));
    }

    @Test
    public void testFindMatchEmptySets() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()));
        HashSet left = new HashSet();
        HashSet right = new HashSet();
        assertFalse(op.findMatch(context, left, right));
    }

    @Test
    public void testFindMatchMatched() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()));
        HashSet left = new HashSet();
        HashSet right = new HashSet();
        left.add(new Const(1));
        assertTrue(op.findMatch(context, left, right));
    }

    @Test
    public void testFindMatchNotMatched() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(new IteratorWrapper(Collections.singletonList(new Const(1)).iterator()), new IteratorWrapper(Collections.singletonList(new Const(2)).iterator()));
        HashSet left = new HashSet();
        HashSet right = new HashSet();
        left.add(new Const(1));
        assertFalse(op.findMatch(context, left, right));
    }

    private static class Const implements Expression {
        private Object value;

        public Const(Object value) {
            this.value = value;
        }

        public Object compute(EvalContext context) {
            return value;
        }

        public void reset() {
            // Do nothing
        }

        public void setExpressionContext(EvalContext context) {
            // Do nothing
        }

        public String toString() {
            return value.toString();
        }
    }

    private static class IteratorWrapper implements Iterator {
        private Iterator wrapped;

        public IteratorWrapper(Iterator wrapped) {
            this.wrapped = wrapped;
        }

        public boolean hasNext() {
            return wrapped.hasNext();
        }

        public Object next() {
            return wrapped.next();
        }

        public void remove() {
            wrapped.remove();
        }
    }

    private static class List implements Iterator {
        private List list;

        public List(List list) {
            this.list = list;
        }

        public Iterator iterator() {
            return this;
        }

        public boolean hasNext() {
            return list.hasNext();
        }

        public Object next() {
            return list.next();
        }

        public void remove() {
            list.remove();
        }
    }
}