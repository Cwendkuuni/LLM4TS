package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = "foo";
        r = "foo";
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Boolean(true);
        r = new Boolean(true);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Pointer("id('foo')");
        r = new Pointer("id('foo')");
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Integer(1);
        r = new Integer(2);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Double(1.0);
        r = new Double(2.0);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = "foo";
        r = "bar";
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Boolean(true);
        r = new Boolean(false);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Pointer("id('foo')");
        r = new Pointer("id('bar')");
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = null;
        r = new Integer(1);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));

        l = new Integer(1);
        r = null;
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), null));
    }

    @Test
    public void testContains() {
        List list = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        Iterator it = list.iterator();

        assertTrue(new CoreOperationCompare(it, new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));
        assertTrue(new CoreOperationCompare(it, new ConstantExpression(new Integer(2))).evaluate(new EvalContext(), null));
        assertTrue(new CoreOperationCompare(it, new ConstantExpression(new Integer(3))).evaluate(new EvalContext(), null));
        assertFalse(new CoreOperationCompare(it, new ConstantExpression(new Integer(4))).evaluate(new EvalContext(), null));

        assertTrue(new CoreOperationCompare(new ConstantExpression(list), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));
        assertTrue(new CoreOperationCompare(new ConstantExpression(list), new ConstantExpression(new Integer(2))).evaluate(new EvalContext(), null));
        assertTrue(new CoreOperationCompare(new ConstantExpression(list), new ConstantExpression(new Integer(3))).evaluate(new EvalContext(), null));
        assertFalse(new CoreOperationCompare(new ConstantExpression(list), new ConstantExpression(new Integer(4))).evaluate(new EvalContext(), null));
    }

    @Test
    public void testFindMatch() {
        List list1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        List list2 = Arrays.asList(new Integer(1), new Integer(3), new Integer(4));
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        assertTrue(new CoreOperationCompare(it1, it2).evaluate(new EvalContext(), null));

        list1 = Collections.singletonList(new Integer(1));
        list2 = Arrays.asList(new Integer(1), new Integer(3), new Integer(4));
        it1 = list1.iterator();
        it2 = list2.iterator();

        assertFalse(new CoreOperationCompare(it1, it2).evaluate(new EvalContext(), null));

        list1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        list2 = Collections.singletonList(new Integer(1));
        it1 = list1.iterator();
        it2 = list2.iterator();

        assertFalse(new CoreOperationCompare(it1, it2).evaluate(new EvalContext(), null));

        list1 = Collections.singletonList(new Integer(1));
        list2 = Collections.singletonList(new Integer(2));
        it1 = list1.iterator();
        it2 = list2.iterator();

        assertFalse(new CoreOperationCompare(it1, it2).evaluate(new EvalContext(), null));
    }

    @Test
    public void testNull() {
        Iterator it = null;
        assertNull(new CoreOperationCompare(new ConstantExpression(it), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), null));

        it = new HashSet<Object>().iterator();
        assertNull(new CoreOperationCompare(null, new ConstantExpression(it)).evaluate(new EvalContext(), null));

        it = new HashSet<Object>().iterator();
        assertNull(new CoreOperationCompare(it, null).evaluate(new EvalContext(), null));
    }
}