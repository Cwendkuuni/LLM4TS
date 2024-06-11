package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        Expression arg1 = new Const(new Integer(1));
        Expression arg2 = new Const(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testDifferentTypes() {
        Expression arg1 = new Const(new Integer(1));
        Expression arg2 = new Const("1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testNullLeft() {
        Expression arg1 = new Const(null);
        Expression arg2 = new Const(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testNullRight() {
        Expression arg1 = new Const(new Integer(1));
        Expression arg2 = new Const(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testBooleanValues() {
        Expression arg1 = new Const(true);
        Expression arg2 = new Const(true);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testDifferentBooleanValues() {
        Expression arg1 = new Const(true);
        Expression arg2 = new Const(false);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testDifferentNumberValues() {
        Expression arg1 = new Const(new Double(1.5));
        Expression arg2 = new Const(new Double(2.5));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testEqualStringValues() {
        Expression arg1 = new Const("foo");
        Expression arg2 = new Const("foo");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testDifferentStringValues() {
        Expression arg1 = new Const("foo");
        Expression arg2 = new Const("bar");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testNullComparison() {
        Expression arg1 = new Const(null);
        Expression arg2 = new Const(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.equal(new EvalContext(), arg1, arg2));
    }

    @Test
    public void testIteratorsComparison() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Integer> iterator1 = list1.iterator();

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        Iterator<Integer> iterator2 = list2.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Const(iterator1), new Const(iterator2));
        assertFalse(coreOperationCompare.equal(new EvalContext(), new Const(iterator1), new Const(iterator2)));
    }

    @Test
    public void testIteratorsContain() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Integer> iterator1 = list1.iterator();

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        Iterator<Integer> iterator2 = list2.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, new Const(iterator2));
        assertTrue(coreOperationCompare.contains(new EvalContext(), iterator1, iterator2));
    }

    @Test
    public void testIteratorsFindMatch() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Integer> iterator1 = list1.iterator();

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        Iterator<Integer> iterator2 = list2.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testPointerValues() {
        Pointer left = new Pointer(new Object(), new Object());
        Pointer right = new Pointer(new Object(), new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Const(left), new Const(right));
        assertTrue(coreOperationCompare.equal(new EvalContext(), new Const(left), new Const(right)));
    }

    @Test
    public void testDifferentPointerValues() {
        Pointer left = new Pointer(new Object(), new Object());
        Pointer right = new Pointer(new Object(), new Object());
        left.setValue(new Integer(1));
        right.setValue(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Const(left), new Const(right));
        assertFalse(coreOperationCompare.equal(new EvalContext(), new Const(left), new Const(right)));
    }

    @Test
    public void testNullPointerComparison() {
        Pointer left = new Pointer(new Object(), new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Const(left), new Const(null));
        assertFalse(coreOperationCompare.equal(new EvalContext(), new Const(left), new Const(null)));
    }

}