package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.ListIterator;
import org.apache.commons.jxpath.ri.iter.SingletonListIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        EvalContext context = new EvalContext();
        assertTrue(new CoreOperationCompare(new Constant(l), new Constant(r)).evaluateToBoolean(context));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(new CoreOperationCompare(new Constant(l), new Constant(r)).evaluateToBoolean(context));

        l = new Boolean(true);
        r = new Boolean(true);
        assertTrue(new CoreOperationCompare(new Constant(l), new Constant(r)).evaluateToBoolean(context));

        l = new String("foo");
        r = new String("foo");
        assertTrue(new CoreOperationCompare(new Constant(l), new Constant(r)).evaluateToBoolean(context));

        l = new Pointer("foo");
        r = new Pointer("foo");
        assertTrue(new CoreOperationCompare(new Constant(l), new Constant(r)).evaluateToBoolean(context));

        l = new Integer(1);
        r = new Integer(1);
        EvalContext context1 = new EvalContext();
        assertTrue(new CoreOperationCompare(new Variable("x"), new Constant(l)).evaluateToBoolean(context1, context1.set("x", l)));

        l = new Integer(1);
        r = new Integer(1);
        EvalContext context2 = new EvalContext();
        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(context2, context2.set("x", l).set("y", r)));

        l = new String("foo");
        r = new String("foo");
        EvalContext context3 = new EvalContext();
        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(context3, context3.set("x", l).set("y", r)));

        l = new Integer(1);
        r = new Double(1.0);
        EvalContext context4 = new EvalContext();
        assertFalse(new CoreOperationCompare(new Variable("x"), new Constant(r)).evaluateToBoolean(context4, context4.set("x", l)));

        l = new Double(1.0);
        r = new Integer(1);
        EvalContext context5 = new EvalContext();
        assertFalse(new CoreOperationCompare(new Constant(l), new Variable("y")).evaluateToBoolean(context5, context5.set("y", r)));

        l = new String("foo");
        r = new String("bar");
        EvalContext context6 = new EvalContext();
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(context6, context6.set("x", l).set("y", r)));

        l = new Integer(1);
        r = null;
        EvalContext context7 = new EvalContext();
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(context7, context7.set("x", l).set("y", r)));

        l = null;
        r = new Integer(1);
        EvalContext context8 = new EvalContext();
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(context8, context8.set("x", l).set("y", r)));
    }

    @Test
    public void testContains() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        Iterator iter = list.iterator();

        assertTrue(new CoreOperationCompare(new Variable("x"), new Constant(1)).evaluateToBoolean(new EvalContext(), new InitialContext(new SingletonListIterator(iter)), "x"));

        assertFalse(new CoreOperationCompare(new Variable("x"), new Constant(3)).evaluateToBoolean(new EvalContext(), new InitialContext(new SingletonListIterator(iter)), "x"));

        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(new EvalContext(), new InitialContext(new SingletonListIterator(iter)), "x, y"));

        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("z")).evaluateToBoolean(new EvalContext(), new InitialContext(new SingletonListIterator(iter)), "x, z"));
    }

    @Test
    public void testFindMatch() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list1.add(new Integer(1));

        List list2 = new ArrayList();
        list2.add(new Integer(2));
        list2.add(new Integer(1));

        List list3 = new ArrayList();
        list3.add(new Integer(1));
        list3.add(new Integer(1));
        list3.add(new Integer(2));

        HashSet set1 = new HashSet();
        set1.add(new Integer(1));
        set1.add(new Integer(2));

        HashSet set2 = new HashSet();
        set2.add(new Integer(2));
        set2.add(new Integer(1));

        HashSet set3 = new HashSet();
        set3.add(new Integer(1));
        set3.add(new Integer(1));
        set3.add(new Integer(2));

        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y")).evaluateToBoolean(new EvalContext(), new InitialContext(new ListIterator(list1.iterator())), "x, y"));
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(new ListIterator(list1.iterator())), "x, y, z, w"));

        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(new ListIterator(list2.iterator())), "x, y, z, w"));
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(new ListIterator(list3.iterator())), "x, y, z, w"));

        assertTrue(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(set1.iterator()), "x, y, z, w"));
        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(set2.iterator()), "x, y, z, w"));

        assertFalse(new CoreOperationCompare(new Variable("x"), new Variable("y"), new CoreOperationCompare.CoreOperationCompare(new Variable("z"), new Variable("w"))).evaluateToBoolean(new EvalContext(), new InitialContext(set3.iterator()), "x, y, z, w"));
    }

}