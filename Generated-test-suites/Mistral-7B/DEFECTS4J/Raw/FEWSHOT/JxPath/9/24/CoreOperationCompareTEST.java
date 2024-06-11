package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        assertEquals(true, coreOperationCompare.evaluate(context));

        l = new Integer(2);
        r = new Integer(1);
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Boolean() {
        Object l = new Boolean(true);
        Object r = new Boolean(true);
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        assertEquals(true, coreOperationCompare.evaluate(context));

        l = new Boolean(false);
        r = new Boolean(true);
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_String() {
        Object l = "foo";
        Object r = "foo";
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        assertEquals(true, coreOperationCompare.evaluate(context));

        l = "bar";
        r = "foo";
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Null() {
        Object l = null;
        Object r = null;
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        assertEquals(true, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Pointer() {
        Object l = new Pointer("x[0]", new InitialContext(), new SelfContext());
        Object r = new Pointer("x[0]", new InitialContext(), new SelfContext());
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        assertEquals(true, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Iterator() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        Iterator<Object> iterator1 = list1.iterator();

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);

        Iterator<Object> iterator2 = list2.iterator();

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(iterator1), new IteratorValue(iterator2));
        assertEquals(true, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_NotSameIterator() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        Iterator<Object> iterator1 = list1.iterator();

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(3);

        Iterator<Object> iterator2 = list2.iterator();

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(iterator1), new IteratorValue(iterator2));
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(list.iterator()), new Constant(2));
        assertEquals(true, coreOperationCompare.evaluate(context));

        coreOperationCompare = new CoreOperationCompare(new IteratorValue(list.iterator()), new Constant(4));
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(2);

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(list1.iterator()), new IteratorValue(list2.iterator()));
        assertEquals(true, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_NotMatching() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(6);

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(list1.iterator()), new IteratorValue(list2.iterator()));
        assertEquals(false, coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_EmptyList() {
        List<Integer> list1 = new ArrayList<>();

        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new IteratorValue(list1.iterator()), new IteratorValue(Collections.emptyIterator().iterator()));
        assertEquals(true, coreOperationCompare.evaluate(context));

        coreOperationCompare = new CoreOperationCompare(new IteratorValue(Collections.emptyIterator().iterator()), new IteratorValue(list1.iterator()));
        assertEquals(false, coreOperationCompare.evaluate(context));
    }
}