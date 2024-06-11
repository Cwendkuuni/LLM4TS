package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationArithmetic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new CoreOperationArithmetic();
        Expression arg2 = new CoreOperationArithmetic();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(null, null, null);
        assertFalse(result);

        result = coreOperationCompare.equal(null, l, r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, null, r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, r, null);
        assertFalse(result);

        result = coreOperationCompare.equal(l, r, r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, l, r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, r, l);
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Integer(1), r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Double(1.0), r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, new String("1"), r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Boolean(true), r);
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Boolean(false), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Object(), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new ArrayList<>(), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new ArrayList<>(), new ArrayList<>());
        assertFalse(result);

        result = coreOperationCompare.equal(l, new HashSet<>(), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new HashSet<>(), new HashSet<>());
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new Pointer(null, 2));
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new Pointer(null, 1));
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new Pointer(null, 1, 1));
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1, 1), new Pointer(null, 1));
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1, 1), new Pointer(null, 1, 1));
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new Pointer(null, 1, 1, 1));
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new Pointer(null, 1, 1, 1, 1));
        assertTrue(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new InitialContext());
        assertFalse(result);

        result = coreOperationCompare.equal(l, new Pointer(null, 1), new SelfContext());
        assertFalse(result);

        result = coreOperationCompare.equal(l, new InitialContext(), r);
        assertFalse(result);

        result = coreOperationCompare.equal(l, new SelfContext(), r);
        assertFalse(result);
    }

    @Test
    public void testContains() {
        Iterator iterator = Arrays.asList(new Object[]{}).iterator();
        Object value = new Integer(1);
        boolean result = new CoreOperationCompare(null, null).contains(iterator, value);
        assertFalse(result);

        iterator = Arrays.asList(new Object[]{new Integer(1)}).iterator();
        result = new CoreOperationCompare(null, null).contains(iterator, value);
        assertFalse(result);

        result = new CoreOperationCompare(null, null).contains(iterator, new Integer(1));
        assertTrue(result);

        result = new CoreOperationCompare(null, null).contains(Arrays.asList(new Object[]{new Integer(1)}).iterator(), value);
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = Arrays.asList(new Object[]{new Integer(1), new Integer(2)}).iterator();
        Iterator rightIterator = Arrays.asList(new Object[]{new Integer(1), new Integer(2), new Integer(1)}).iterator();
        boolean result = new CoreOperationCompare(null, null).findMatch(leftIterator, rightIterator);
        assertTrue(result);

        result = new CoreOperationCompare(null, null).findMatch(leftIterator, Arrays.asList(new Object[]{new Integer(3)}).iterator());
        assertFalse(result);

        result = new CoreOperationCompare(null, null).findMatch(Arrays.asList(new Object[]{new Integer(1)}).iterator(), leftIterator);
        assertFalse(result);

        result = new CoreOperationCompare(null, null).findMatch(Arrays.asList(new Object[]{new Integer(1), new Integer(1)}).iterator(), rightIterator);
        assertTrue(result);

        result = new CoreOperationCompare(null, null).findMatch(Arrays.asList(new Object[]{new Integer(1), new Integer(1)}).iterator(), Arrays.asList(new Object[]{new Integer(1), new Integer(2)}).iterator());
        assertFalse(result);

        result = new CoreOperationCompare(null, null).findMatch(Arrays.asList(new Object[]{new Integer(1), new Integer(1)}).iterator(), Arrays.asList(new Object[]{new Integer(2), new Integer(1)}).iterator());
        assertFalse(result);

        result = new CoreOperationCompare(null, null).findMatch(Arrays.asList(new Object[]{new Integer(1), new Integer(1)}).iterator(), Arrays.asList(new Object[]{new Integer(1), new Integer(1), new Integer(1)}).iterator());
        assertTrue(result);
    }

}