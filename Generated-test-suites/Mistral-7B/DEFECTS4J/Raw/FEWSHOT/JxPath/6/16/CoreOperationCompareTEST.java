package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        assertTrue(CoreOperationCompare.equal(null, l, r));
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(l, l));
        assertTrue(CoreOperationCompare.equal(r, r));
    }

    @Test
    public void testEqual_boolean() {
        Object l = Boolean.TRUE;
        Object r = Boolean.TRUE;
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));
    }

    @Test
    public void testEqual_string() {
        Object l = "foo";
        Object r = "foo";
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));
    }

    @Test
    public void testEqual_null() {
        Object l = null;
        Object r = null;
        assertTrue(CoreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_differentTypes() {
        Object l = new Integer(1);
        Object r = Boolean.TRUE;
        assertFalse(CoreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_pointers() {
        Pointer l = new Pointer("x[0]", new InitialContext());
        Pointer r = new Pointer("x[0]", new InitialContext());
        assertTrue(CoreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_pointersDifferent() {
        Pointer l = new Pointer("x[0]", new InitialContext());
        Pointer r = new Pointer("x[1]", new InitialContext());
        assertFalse(CoreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_collectionIterator() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);

        Iterator<Integer> lit = l.iterator();
        Object r = new Integer(1);

        assertFalse(CoreOperationCompare.equal(lit, r));
        assertFalse(CoreOperationCompare.equal(r, lit));

        List<Integer> m = new ArrayList<>();
        m.add(1);
        m.add(1);

        Iterator<Integer> mit = m.iterator();
        assertTrue(CoreOperationCompare.equal(mit, mit));
    }

    @Test
    public void testFindMatch() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);

        Iterator<Integer> lit = l.iterator();
        List<Integer> r = new ArrayList<>();
        r.add(1);
        r.add(2);
        r.add(3);

        Iterator<Integer> rit = r.iterator();
        assertTrue(CoreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testFindMatch_noMatch() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);

        Iterator<Integer> lit = l.iterator();
        List<Integer> r = new ArrayList<>();
        r.add(4);
        r.add(2);
        r.add(3);

        Iterator<Integer> rit = r.iterator();
        assertFalse(CoreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testContains() {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);

        Iterator<Integer> lit = l.iterator();
        Object value = new Integer(1);

        assertTrue(CoreOperationCompare.contains(lit, value));
        assertTrue(CoreOperationCompare.contains(lit, new Integer(1)));
        assertFalse(CoreOperationCompare.contains(lit, new Integer(3)));
    }

    @Test
    public void testContains_emptyList() {
        List<Integer> l = Collections.emptyList();
        Object value = new Integer(1);
        assertFalse(CoreOperationCompare.contains(l.iterator(), value));
    }
}