package org.apache.commons.jxpath.ri.compiler;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(1)));
        assertTrue(op.isSymmetric());
        assertEquals(2, op.getPrecedence());
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(1)));

        Object l = op.equal(context, new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(1)));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(2)));
        assertTrue(l instanceof Boolean);
        assertFalse((Boolean) l);

        l = op.equal(context, new ConstantExpression("foo"),
                new ConstantExpression("foo"));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Integer(1)),
                null);
        assertNull(l);

        l = op.equal(context, null, new ConstantExpression("foo"));
        assertNull(l);

        l = op.equal(context, new ConstantExpression("foo"), null);
        assertNull(l);

        l = op.equal(context, new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(1)));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer[0]));
        assertNull(l);

        l = op.equal(context, new ConstantExpression(new Integer[0]),
                new ConstantExpression(new Integer(1)));
        assertNull(l);

        l = op.equal(context, new ConstantExpression(new Integer(1)),
                new ConstantExpression(new Integer(1).getClass()));
        assertNull(l);

        l = op.equal(context, new ConstantExpression(new Integer(1).getClass()),
                new ConstantExpression(new Integer(1)));
        assertNull(l);

        l = op.equal(context, new ConstantExpression(new SelfContext()),
                new ConstantExpression(new SelfContext()));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new InitialContext()),
                new ConstantExpression(new InitialContext()));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new InitialContext()),
                new ConstantExpression(new SelfContext()));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new SelfContext()),
                new ConstantExpression(new InitialContext()));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new Pointer(new Integer(1))),
                new ConstantExpression(new Pointer(new Integer(1))));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Pointer(new Integer(1))),
                new ConstantExpression(new Pointer(new Integer(2))));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new Pointer(new Integer(1))),
                new ConstantExpression(new Pointer()));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new Pointer()),
                new ConstantExpression(new Pointer(new Integer(1))));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new Pointer()),
                new ConstantExpression(new Pointer()));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Pointer("foo")),
                new ConstantExpression(new Pointer("foo")));
        assertTrue(l instanceof Boolean);
        assertTrue((Boolean) l);

        l = op.equal(context, new ConstantExpression(new Pointer("foo")),
                new ConstantExpression(new Pointer("bar")));
        assertFalse(l instanceof Boolean);

        l = op.equal(context, new ConstantExpression(new Pointer("foo")),
                new ConstantExpression(new Pointer()));
        assertFalse(l instanceof Boolean);
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext();
        List list1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        List list2 = Arrays.asList(new Integer(3), new Integer(2), new Integer(1));
        CoreOperationCompare op = new CoreOperationCompare(
                new ListExpression(new ConstantExpression("."), list1),
                new ListExpression(new ConstantExpression("."), list2));

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        assertTrue(op.findMatch(it1, it2));

        it1 = Collections.singletonList(new Integer(3)).iterator();
        it2 = list2.iterator();
        assertFalse(op.findMatch(it1, it2));

        it1 = list1.iterator();
        it2 = Collections.singletonList(new Integer(3)).iterator();
        assertFalse(op.findMatch(it1, it2));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext();
        List list = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        CoreOperationCompare op = new CoreOperationCompare(
                new ListExpression(new ConstantExpression("."), list),
                new ConstantExpression(new Integer(3)));

        Iterator it = list.iterator();
        assertTrue(op.contains(it, new Integer(3)));
        assertFalse(op.contains(it, new Integer(4)));

        op = new CoreOperationCompare(
                new ListExpression(new ConstantExpression("."), Collections.emptyList()),
                new ConstantExpression(new Integer(3)));
        assertFalse(op.contains(Collections.emptyIterator(), new Integer(3)));

        op = new CoreOperationCompare(
                new ConstantExpression(new Integer(3)),
                new ListExpression(new ConstantExpression("."), list));
        assertTrue(op.contains(list.iterator(), new Integer(3)));
        assertFalse(op.contains(list.iterator(), new Integer(4)));

        op = new CoreOperationCompare(
                new ListExpression(new ConstantExpression("."), list),
                new ListExpression(new ConstantExpression("."), list));
        assertTrue(op.findMatch(it, it));
    }

    @Test
    public void testEqualString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression("foo"), new ConstantExpression("bar"));
        assertFalse(op.equal(context, null, null));
        assertFalse(op.equal(context, null, new ConstantExpression("foo")));
        assertFalse(op.equal(context, new ConstantExpression("foo"), null));
        assertFalse(op.equal(context, new ConstantExpression("foo"),
                new ConstantExpression("bar")));
        assertTrue(op.equal(context, new ConstantExpression("foo"),
                new ConstantExpression("foo")));
        assertTrue(op.equal(context, new ConstantExpression("foo"),
                new ConstantExpression("foo")));
        assertTrue(op.equal(context, new ConstantExpression("foo"),
                new ConstantExpression(InfoSetUtil.stringValue("foo"))));
    }

    @Test
    public void testEqualNumber() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(1.6)));
        assertFalse(op.equal(context, null, null));
        assertFalse(op.equal(context, null, new ConstantExpression(new Double(1.5))));
        assertFalse(op.equal(context, new ConstantExpression(new Double(1.5)),
                null));
        assertFalse(op.equal(context, new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(1.6))));
        assertTrue(op.equal(context, new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(1.5))));
        assertTrue(op.equal(context, new ConstantExpression(new Double(1.5)),
                new ConstantExpression(new Double(1.5))));
    }

    @Test
    public void testEqualBoolean() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.FALSE));
        assertFalse(op.equal(context, null, null));
        assertFalse(op.equal(context, null, new ConstantExpression(Boolean.TRUE)));
        assertFalse(op.equal(context, new ConstantExpression(Boolean.TRUE),
                null));
        assertFalse(op.equal(context, new ConstantExpression(Boolean.TRUE),
                new ConstantExpression(Boolean.FALSE)));
        assertFalse(op.equal(context, new ConstantExpression(Boolean.FALSE),
                new ConstantExpression(Boolean.TRUE)));
        assertTrue(op.equal(context, new ConstantExpression(Boolean.TRUE),
                new ConstantExpression(Boolean.TRUE)));
        assertTrue(op.equal(context, new ConstantExpression(Boolean.TRUE),
                new ConstantExpression(Boolean.TRUE)));
        assertTrue(op.equal(context, new ConstantExpression(Boolean.FALSE),
                new ConstantExpression(Boolean.FALSE)));
        assertTrue(op.equal(context, new ConstantExpression(Boolean.FALSE),
                new ConstantExpression(Boolean.FALSE)));
    }

    @Test
    public void testEqualNull() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(null), new ConstantExpression(null));
        assertNull(op.equal(context, null, null));
        assertTrue(op.equal(context, null, new ConstantExpression(null)));
        assertTrue(op.equal(context, new ConstantExpression(null), null));
        assertTrue(op.equal(context, new ConstantExpression(null),
                new ConstantExpression(null)));
    }

    @Test
    public void testEqualPointer() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Pointer(new Integer(1))),
                new ConstantExpression(new Pointer(new Integer(1))));
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, null, new ConstantExpression(new Pointer(new Integer(1)))));
        assertTrue(op.equal(context, new ConstantExpression(new Pointer(new Integer(1))),
                null));
        assertTrue(op.equal(context, new ConstantExpression(new Pointer(new Integer(1))),
                new ConstantExpression(new Pointer(new Integer(1)))));
    }
}