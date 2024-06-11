package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private EvalContext context;

    @Before
    public void setup() {
        context = new EvalContext(new JXPathContext(), new InfoSet());
    }

    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(null);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithNull() {
        Expression arg1 = new LiteralExpression("a");
        Expression arg2 = new LiteralExpression(null);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithBoolean() {
        Expression arg1 = new LiteralExpression(true);
        Expression arg2 = new LiteralExpression(true);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithBoolean() {
        Expression arg1 = new LiteralExpression(true);
        Expression arg2 = new LiteralExpression(false);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithString() {
        Expression arg1 = new LiteralExpression("a");
        Expression arg2 = new LiteralExpression("a");

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithString() {
        Expression arg1 = new LiteralExpression("a");
        Expression arg2 = new LiteralExpression("b");

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithIterators() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);

        Iterator<Integer> it1 = list1.iterator();
        Iterator<Integer> it2 = list2.iterator();

        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithIteratorsDifferentSize() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);

        Iterator<Integer> it1 = list1.iterator();
        Iterator<Integer> it2 = list2.iterator();

        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsSameElements() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);

        Iterator<Integer> it1 = list1.iterator();
        Iterator<Integer> it2 = list2.iterator();

        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsDifferentElements() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(3);

        Iterator<Integer> it1 = list1.iterator();
        Iterator<Integer> it2 = list2.iterator();

        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithArrays() {
        int[] array1 = new int[] { 1, 2 };
        int[] array2 = new int[] { 1, 2 };

        Expression arg1 = new ArrayExpression(array1);
        Expression arg2 = new ArrayExpression(array2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithArrays() {
        int[] array1 = new int[] { 1, 2 };
        int[] array2 = new int[] { 1, 3 };

        Expression arg1 = new ArrayExpression(array1);
        Expression arg2 = new ArrayExpression(array2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = new Pointer("1");
        Pointer pointer2 = new Pointer("1");

        Expression arg1 = new LiteralExpression(pointer1);
        Expression arg2 = new LiteralExpression(pointer2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithPointers() {
        Pointer pointer1 = new Pointer("1");
        Pointer pointer2 = new Pointer("2");

        Expression arg1 = new LiteralExpression(pointer1);
        Expression arg2 = new LiteralExpression(pointer2);

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqualWithSelfContextAndInitialContext() {
        Expression arg1 = new LiteralExpression(new SelfContext());
        Expression arg2 = new LiteralExpression(new InitialContext());

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertNull(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithSelfContextAndInitialContext() {
        Expression arg1 = new LiteralExpression(new SelfContext());
        Expression arg2 = new LiteralExpression(new SelfContext());

        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(compare.evaluate(context));
    }

}