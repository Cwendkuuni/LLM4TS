package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        Object l1 = new Integer(1);
        Object r1 = new Integer(2);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l1), new ConstantExpression(r1)).evaluate(new EvalContext(), null));

        Object l2 = new Double(3.14);
        Object r2 = new Double(3.14);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l2), new ConstantExpression(r2)).evaluate(new EvalContext(), null));

        Object l3 = new Double(3.14);
        Object r3 = new Double(3.15);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l3), new ConstantExpression(r3)).evaluate(new EvalContext(), null));

        Object l4 = new String("hello");
        Object r4 = new String("hello");
        assertTrue(new CoreOperationCompare(new ConstantExpression(l4), new ConstantExpression(r4)).evaluate(new EvalContext(), null));

        Object l5 = new String("hello");
        Object r5 = new String("world");
        assertFalse(new CoreOperationCompare(new ConstantExpression(l5), new ConstantExpression(r5)).evaluate(new EvalContext(), null));

        Object l6 = new Boolean(true);
        Object r6 = new Boolean(true);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l6), new ConstantExpression(r6)).evaluate(new EvalContext(), null));

        Object l7 = new Boolean(true);
        Object r7 = new Boolean(false);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l7), new ConstantExpression(r7)).evaluate(new EvalContext(), null));

        Object l8 = new Pointer(new Object(), null);
        Object r8 = new Pointer(new Object(), null);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l8), new ConstantExpression(r8)).evaluate(new EvalContext(), null));

        Object l9 = new Pointer(new Object(), null);
        Object r9 = new Pointer(new Object(), null);
        l9.setValue("test");
        assertTrue(new CoreOperationCompare(new ConstantExpression(l9), new ConstantExpression(r9)).evaluate(new EvalContext(), null));

        Object l10 = new Pointer(new Object(), null);
        Object r10 = new Pointer(new Object(), null);
        l10.setValue("test");
        r10.setValue("test2");
        assertFalse(new CoreOperationCompare(new ConstantExpression(l10), new ConstantExpression(r10)).evaluate(new EvalContext(), null));

        Object l11 = new Pointer(new Object(), null);
        Object r11 = null;
        assertFalse(new CoreOperationCompare(new ConstantExpression(l11), new ConstantExpression(r11)).evaluate(new EvalContext(), null));

        Object l12 = null;
        Object r12 = new Pointer(new Object(), null);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l12), new ConstantExpression(r12)).evaluate(new EvalContext(), null));
    }

    @Test
    public void testContains() {
        List list = Collections.singletonList(new Integer(1));
        assertTrue(new CoreOperationCompare(new ConstantExpression(list.iterator()), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));

        List list1 = Collections.singletonList(new Integer(1));
        assertFalse(new CoreOperationCompare(new ConstantExpression(list1.iterator()), new ConstantExpression(new Integer(2))).evaluate(new EvalContext(), null));

        List list2 = Collections.singletonList(new Double(3.14));
        assertFalse(new CoreOperationCompare(new ConstantExpression(list2.iterator()), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));

        List list3 = Collections.singletonList(new String("hello"));
        assertTrue(new CoreOperationCompare(new ConstantExpression(list3.iterator()), new ConstantExpression(new String("hello"))).evaluate(new EvalContext(), null));

        List list4 = Collections.singletonList(new Boolean(true));
        assertTrue(new CoreOperationCompare(new ConstantExpression(list4.iterator()), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(), null));

        List list5 = Collections.singletonList(new Pointer(new Object(), null));
        Object value = new Pointer(new Object(), null);
        value.setValue("test");
        assertTrue(new CoreOperationCompare(new ConstantExpression(list5.iterator()), new ConstantExpression(value)).evaluate(new EvalContext(), null));

        List list6 = Collections.singletonList(new Pointer(new Object(), null));
        value = new Pointer(new Object(), null);
        value.setValue("test2");
        assertFalse(new CoreOperationCompare(new ConstantExpression(list6.iterator()), new ConstantExpression(value)).evaluate(new EvalContext(), null));

        List list7 = Collections.emptyList();
        assertFalse(new CoreOperationCompare(new ConstantExpression(list7.iterator()), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));
    }

    @Test
    public void testFindMatch() {
        List list = Collections.singletonList(new Integer(1));
        List list1 = Collections.singletonList(new Integer(1));
        assertTrue(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new ConstantExpression(list1.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list2 = Collections.singletonList(new Integer(1));
        List list3 = Collections.singletonList(new Integer(2));
        assertFalse(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list2.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new ConstantExpression(list3.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list4 = Collections.singletonList(new Double(3.14));
        List list5 = Collections.singletonList(new Integer(1));
        assertFalse(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list4.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new ConstantExpression(list5.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list6 = Collections.singletonList(new String("hello"));
        List list7 = Collections.singletonList(new String("hello"));
        assertTrue(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list6.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new ConstantExpression(list7.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list8 = Collections.singletonList(new Boolean(true));
        List list9 = Collections.singletonList(new Boolean(true));
        assertTrue(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list8.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new ConstantExpression(list9.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list10 = Collections.singletonList(new Pointer(new Object(), null));
        List list11 = Collections.singletonList(new Pointer(new Object(), null));
        value = new Pointer(new Object(), null);
        value.setValue("test");
        assertTrue(new CoreOperationCompare(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list10.iterator()), new ConstantExpression(null)), new ConstantExpression(value)), new CoreOperationCompare(new ConstantExpression(list11.iterator()), new ConstantExpression(null))).evaluate(new EvalContext(), null));

        List list12 = Collections.singletonList(new Pointer(new Object(), null));
        List list13 = Collections.singletonList(new Pointer(new Object(), null));
        value = new Pointer(new Object(), null);
        value.setValue("test2");
        assertFalse(new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list12.iterator()), new ConstantExpression(null)), new CoreOperationCompare(new CoreOperationCompare(new ConstantExpression(list13.iterator()), new ConstantExpression(null)), new ConstantExpression(value))).evaluate(new EvalContext(), null));
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1))).isSymmetric());
        assertFalse(new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2))).isSymmetric());
        assertFalse(new CoreOperationCompare(new ConstantExpression(new Double(3.14)), new ConstantExpression(new Double(3.15))).isSymmetric());
        assertTrue(new CoreOperationCompare(new ConstantExpression(new String("hello")), new ConstantExpression(new String("hello"))).isSymmetric());
        assertTrue(new CoreOperationCompare(new ConstantExpression(new Boolean(true)), new ConstantExpression(new Boolean(true))).isSymmetric());
        assertTrue(new CoreOperationCompare(new ConstantExpression(new Pointer(new Object(), null)), new ConstantExpression(new Pointer(new Object(), null))).isSymmetric());
    }
}