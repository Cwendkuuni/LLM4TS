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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for CoreOperationCompare class.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual_sameObjects() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_sameValues() {
        Object value = new Integer(42);
        Expression left = new InitialContext(value);
        Expression right = new InitialContext(value);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_differentObjects() {
        Object leftValue = new Integer(42);
        Expression left = new InitialContext(leftValue);
        Object rightValue = new Boolean(true);
        Expression right = new InitialContext(rightValue);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_differentValues_boolean() {
        Expression left = new InitialContext(Boolean.TRUE);
        Expression right = new InitialContext(Boolean.FALSE);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_differentValues_number() {
        Expression left = new InitialContext(new Double(3.14));
        Expression right = new InitialContext(new Double(2.718));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_differentValues_string() {
        Expression left = new InitialContext("foo");
        Expression right = new InitialContext("bar");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_differentValues_pointers() {
        Object value = new Integer(42);
        Expression left = new SelfContext(new Pointer(value));
        Expression right = new SelfContext(new Pointer(value));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.equal(new EvalContext(), left, right));

        Pointer leftPointer = new Pointer(value);
        Pointer rightPointer = new Pointer(value);
        Expression left2 = new SelfContext(leftPointer);
        Expression right2 = new SelfContext(rightPointer);
        compare = new CoreOperationCompare(left2, right2);
        assertTrue(compare.equal(new EvalContext(), left2, right2));

        leftPointer = new Pointer(new Integer(42));
        rightPointer = new Pointer(new Integer(43));
        Expression left3 = new SelfContext(leftPointer);
        Expression right3 = new SelfContext(rightPointer);
        compare = new CoreOperationCompare(left3, right3);
        assertFalse(compare.equal(new EvalContext(), left3, right3));
    }

    @Test
    public void testEqual_collectionWithSameElements_iterator() {
        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 42));
        Expression left = new InitialContext(collection.iterator());
        Expression right = new InitialContext(collection.iterator());
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_collectionWithSameElements_array() {
        Object[] array = new Object[]{1, 2, 3, 4, 42};
        Expression left = new InitialContext(Arrays.asList(array).iterator());
        Expression right = new InitialContext(Arrays.asList(array).iterator());
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testEqual_collectionWithDifferentElements() {
        Collection<Integer> collection1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 42));
        Collection<Integer> collection2 = new ArrayList<>(Arrays.asList(1, 2, 3, 5, 42));
        Expression left = new InitialContext(collection1.iterator());
        Expression right = new InitialContext(collection2.iterator());
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.equal(new EvalContext(), left, right));
    }

    @Test
    public void testFindMatch() {
        Collection<Integer> collection1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 42));
        Collection<Integer> collection2 = new ArrayList<>(Arrays.asList(4, 2, 3, 4, 42));
        Expression left = new InitialContext(collection1.iterator());
        Expression right = new InitialContext(collection2.iterator());
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.findMatch((Iterator) collection1.iterator(), (Iterator) collection2.iterator()));
    }

    @Test
    public void testContains() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 42);
        Expression iterator = new InitialContext(list.iterator());
        Expression value = new InitialContext(new Integer(42));
        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertTrue(compare.contains((Iterator) list.iterator(), value.compute(new EvalContext())));
    }

    @Test
    public void testContains_emptyIterator() {
        Expression iterator = new InitialContext(new HashSet<>().iterator());
        Expression value = new InitialContext(new Integer(42));
        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertFalse(compare.contains((Iterator) new HashSet<>().iterator(), value.compute(new EvalContext())));
    }

    @Test
    public void testContains_nullIterator() {
        Expression iterator = new InitialContext(null);
        Expression value = new InitialContext(new Integer(42));
        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertFalse(compare.contains(null, value.compute(new EvalContext())));
    }

    @Test
    public void testContains_nullValue() {
        Expression iterator = new InitialContext(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 42)).iterator());
        Expression value = new InitialContext(null);
        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertFalse(compare.contains((Iterator) new ArrayList<>(Arrays.asList(1, 2, 3, 4, 42)).iterator(), value));
    }
}