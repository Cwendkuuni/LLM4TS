package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1)));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new String("foo")), new ConstantExpression(new String("foo")));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new String("foo")), new ConstantExpression(new String("bar")));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Boolean(true)), new ConstantExpression(new Boolean(true)));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Boolean(true)), new ConstantExpression(new Boolean(false)));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(1.0)));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(2.0)));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ListExpression(new ArrayList<>()));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ListExpression(new ArrayList<>()), new ConstantExpression(new Integer(1)));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ListExpression(new ArrayList<>()), new ListExpression(new ArrayList<>()));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new InitialContext()), new ConstantExpression(new InitialContext()));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new InitialContext()), new ConstantExpression(new SelfContext()));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new SelfContext()), new ConstantExpression(new InitialContext()));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new SelfContext()), new ConstantExpression(new SelfContext()));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new SelfContext()), new ConstantExpression(new Pointer(new Object(), new Object())));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Pointer(new Object(), new Object())), new ConstantExpression(new SelfContext()));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Pointer(new Object(), new Object())), new ConstantExpression(new Pointer(new Object(), new Object())));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new Pointer(new Object(), new Object())), new ConstantExpression(new String("foo")));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ConstantExpression(new String("foo")), new ConstantExpression(new Pointer(new Object(), new Object())));
        assertFalse(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ListExpression(new ArrayList<>()), new ListExpression(new ArrayList<>()));
        assertTrue(op.equal(new EvalContext(), op.getArguments()[0], op.getArguments()[1]));

        op = new CoreOperationCompare(new ListExpression(new ArrayList<>()), new ListExpression(new ArrayList<>()));
        assertTrue(op.isSymmetric());

        op = new CoreOperationCompare(new ListExpression(new ArrayList<>()), new ListExpression(new ArrayList<>()));
        assertEquals(2, op.getPrecedence());
    }

    @Test
    public void testFindMatch() {
        HashSet set = new HashSet();
        set.add(new Integer(1));
        set.add(new Integer(2));
        set.add(new Integer(3));

        Iterator it = set.iterator();

        Iterator rit = new ArrayList(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();

        assertTrue(CoreOperationCompare.findMatch(it, rit));

        Iterator lit = new ArrayList(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
        Iterator rit1 = set.iterator();

        assertTrue(CoreOperationCompare.findMatch(lit, rit1));

        Iterator it1 = new ArrayList(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
        Iterator rit2 = new ArrayList(List.of(new Integer(2), new Integer(3), new Integer(1))).iterator();

        assertFalse(CoreOperationCompare.findMatch(it1, rit2));

        Iterator lit1 = new ArrayList(List.of(new Integer(1), new Integer(2), new Integer(3))).iterator();
        Iterator rit3 = new ArrayList(List.of(new Integer(4), new Integer(5), new Integer(6))).iterator();

        assertFalse(CoreOperationCompare.findMatch(lit1, rit3));
    }

    @Test
    public void testContains() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        list.add(new Integer(3));

        Iterator it = list.iterator();

        assertTrue(CoreOperationCompare.contains(it, new Integer(1)));

        assertFalse(CoreOperationCompare.contains(it, new Integer(4)));

        HashSet set = new HashSet();
        set.add(new Integer(1));
        set.add(new Integer(2));
        set.add(new Integer(3));

        Iterator rit = set.iterator();

        assertTrue(CoreOperationCompare.contains(rit, new Integer(1)));

        assertFalse(CoreOperationCompare.contains(rit, new Integer(4)));
    }
}