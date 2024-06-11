package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = "foo";
        r = "foo";
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Boolean(true);
        r = new Boolean(true);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Pointer("foo");
        r = new Pointer("foo");
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new ArrayList<>();
        r = new ArrayList<>();
        Iterator lit = l.iterator();
        lit.next();
        assertTrue(new CoreOperationCompare(lit, r).evaluate(new EvalContext(), new InitialContext()));

        l = new Integer(1);
        r = new Integer(2);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Double(1.0);
        r = new Double(2.0);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = "foo";
        r = "bar";
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Boolean(true);
        r = new Boolean(false);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new Pointer("foo");
        r = new Pointer("bar");
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(new EvalContext(), new InitialContext()));

        l = new ArrayList<>();
        r = new ArrayList<>();
        assertFalse(new CoreOperationCompare(Collections.singletonList(new Integer(1)).iterator(), r).evaluate(new EvalContext(), new InitialContext()));
    }

    @Test
    public void testContains() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));

        Iterator it = list.iterator();
        assertTrue(new CoreOperationCompare(it, new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), new InitialContext()));
        assertFalse(new CoreOperationCompare(it, new ConstantExpression(new Integer(3))).evaluate(new EvalContext(), new InitialContext()));

        list = new ArrayList<>();
        assertTrue(new CoreOperationCompare(Collections.singletonList(new Integer(1)).iterator(), list.iterator()).evaluate(new EvalContext(), new InitialContext()));
        assertFalse(new CoreOperationCompare(Collections.singletonList(new Integer(2)).iterator(), list.iterator()).evaluate(new EvalContext(), new InitialContext()));
    }

    @Test
    public void testFindMatch() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list1.add(new Integer(3));

        List list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(1));

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        assertTrue(new CoreOperationCompare(it1, it2).evaluate(new EvalContext(), new InitialContext()));

        List list3 = new ArrayList();
        list3.add(new Integer(1));
        list3.add(new Integer(2));
        list3.add(new Integer(3));

        Iterator it3 = list3.iterator();
        assertFalse(new CoreOperationCompare(it1, it3).evaluate(new EvalContext(), new InitialContext()));

        List list4 = new ArrayList();
        assertNull(new CoreOperationCompare(Collections.emptyList().iterator(), list4.iterator()).evaluate(new EvalContext(), new InitialContext()));
    }

    @Test
    public void testNull() {
        assertNull(new CoreOperationCompare(null, new ConstantExpression(new Integer(1))).evaluate(new EvalContext(), new InitialContext()));
        assertNull(new CoreOperationCompare(new ConstantExpression(new Integer(1)), null).evaluate(new EvalContext(), new InitialContext()));
    }
}