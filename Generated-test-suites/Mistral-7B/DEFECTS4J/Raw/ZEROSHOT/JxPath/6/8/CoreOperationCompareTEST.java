package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test with null values
        CoreOperationCompare compare = new CoreOperationCompare(
                new ConstantExpression(new InitialContext()),
                new ConstantExpression(new InitialContext()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new ConstantExpression(null),
                new ConstantExpression(null));
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(1)));
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new ConstantExpression(new String("test")),
                new ConstantExpression(new String("test")));
        assertTrue(compare.evaluate(context));

        // Test with non-equal values
        compare = new CoreOperationCompare(
                new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(2)));
        context = new EvalContext(new Object[0], new Object[0]);
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new ConstantExpression(new String("test")),
                new ConstantExpression(new String("test2")));
        assertFalse(compare.evaluate(context));

        // Test with different types
        compare = new CoreOperationCompare(
                new ConstantExpression(new Integer(1)),
                new ConstantExpression(new String("test")));
        assertFalse(compare.evaluate(context));

        // Test with Iterators
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        List<Integer> list3 = Arrays.asList(1, 2, 3, 4);

        CoreOperationCompare compareIterator1 = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new ConstantExpression(new Integer(2)));
        assertFalse(compareIterator1.evaluate(context));

        CoreOperationCompare compareIterator2 = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list2.iterator()));
        assertFalse(compareIterator2.evaluate(context));

        CoreOperationCompare compareIterator3 = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list3.iterator()));
        assertTrue(compareIterator3.evaluate(context));
    }

    @Test
    public void testContains() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(list.iterator()),
                new ConstantExpression(new Integer(3)));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new IteratorExpression(list.iterator()),
                new ConstantExpression(new Integer(6)));
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new IteratorExpression(list.iterator()),
                new IteratorExpression(Arrays.asList(3, 4).iterator()));
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(3, 4).iterator()),
                new IteratorExpression(list.iterator()));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);

        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list2.iterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));

        list2 = Arrays.asList(1, 2, 3, 4, 5);
        assertFalse(compare.evaluate(context));

        list2 = Arrays.asList(6, 7);
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithPointer() {
        EvalContext context = new EvalContext(new Object[0], new Object[0]);

        Pointer pointer1 = new Pointer(Collections.EMPTY_LIST, 0);
        Pointer pointer2 = new Pointer(Collections.EMPTY_LIST, 0);
        CoreOperationCompare compare = new CoreOperationCompare(
                new ConstantExpression(pointer1),
                new ConstantExpression(pointer2));
        assertTrue(compare.evaluate(context));

        pointer1 = new Pointer(Arrays.asList(new Object[] { new Integer(1) }), 0);
        pointer2 = new Pointer(Arrays.asList(new Object[] { new Integer(1) }), 0);
        compare = new CoreOperationCompare(
                new ConstantExpression(pointer1),
                new ConstantExpression(pointer2));
        assertTrue(compare.evaluate(context));

        pointer1 = new Pointer(Arrays.asList(new Object[] { new Integer(1) }), 0);
        pointer2 = new Pointer(Arrays.asList(new Object[] { new Integer(2) }), 0);
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithDifferentTypesPointer() {
        EvalContext context = new EvalContext(new Object[0], new Object[0]);

        Pointer pointerInt = new Pointer(Arrays.asList(new Object[] { new Integer(1) }), 0);
        Pointer pointerString = new Pointer(Arrays.asList(new Object[] { new String("test") }), 0);
        CoreOperationCompare compare = new CoreOperationCompare(
                new ConstantExpression(pointerInt),
                new ConstantExpression(pointerString));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testFindMatchWithDifferentTypesIterator() {
        List<Object> listInt = Arrays.asList(new Integer(1), new Integer(2));
        List<Object> listString = Arrays.asList(new String("test"), new String("test2"));

        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(listInt.iterator()),
                new IteratorExpression(listString.iterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testFindMatchWithSameTypesIteratorButDiffrentValues() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 3, 4, 5, 6);

        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list2.iterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testFindMatchWithSameTypesIteratorAndValues() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);

        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list2.iterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testFindMatchWithSameTypesAndValuesButDifferentOrder() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(5, 4, 3, 2, 1);

        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(list1.iterator()),
                new IteratorExpression(list2.iterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testFindMatchWithEmptyIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new IteratorExpression(Collections.emptyIterator()),
                new IteratorExpression(Collections.emptyIterator()));
        EvalContext context = new EvalContext(new Object[0], new Object[0]);
        assertTrue(compare.evaluate(context));
    }
}