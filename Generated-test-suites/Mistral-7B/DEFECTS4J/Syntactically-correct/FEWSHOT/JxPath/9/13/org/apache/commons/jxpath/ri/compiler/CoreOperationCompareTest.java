package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new Expression() {}; // Replace this with a valid Expression instance
        Expression arg2 = new Expression() {}; // Replace this with a valid Expression instance
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertSame(arg1, coreOperationCompare.getExpressions()[0]);
        assertSame(arg2, coreOperationCompare.getExpressions()[1]);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression() {}, new Expression() {});
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression() {}, new Expression() {});
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual() {
        Object l = new Object();
        Object r = new Object();
        EvalContext context = new EvalContext();

        assertEquals(true, coreOperationCompare.equal(context, new Expression() {}, new Expression() {}).booleanValue());
        assertEquals(true, coreOperationCompare.equal(context, new Expression() {}.compute(context), new Expression() {}.compute(context)).booleanValue());

        assertEquals(false, coreOperationCompare.equal(context, null, r).booleanValue());
        assertEquals(false, coreOperationCompare.equal(context, l, null).booleanValue());

        assertEquals(false, coreOperationCompare.equal(context, new InitialContext(), new SelfContext()).booleanValue());
        assertEquals(false, coreOperationCompare.equal(context, new SelfContext(), new InitialContext()).booleanValue());

        assertEquals(true, coreOperationCompare.equal(context, new Boolean(true), new Boolean(true)).booleanValue());

        assertEquals(false, coreOperationCompare.equal(context, new Double(Double.NaN), new Double(Double.NaN)).booleanValue());
        assertEquals(false, coreOperationCompare.equal(context, new Double(Double.NaN), new Double(Double.NAN)).booleanValue());

        assertEquals(true, coreOperationCompare.equal(context, new String("foo"), new String("foo")).booleanValue());

        Pointer lPointer = new Pointer(new Object());
        Pointer rPointer = new Pointer(new Object());
        assertEquals(true, coreOperationCompare.equal(context, lPointer, rPointer).booleanValue());
        assertEquals(true, coreOperationCompare.equal(context, rPointer, lPointer).booleanValue());

        Collection lCollection = new ArrayList();
        Collection rCollection = new ArrayList();
        Iterator lIterator = lCollection.iterator();
        Iterator rIterator = rCollection.iterator();
        lCollection.add(new Object());
        rCollection.add(new Object());

        assertEquals(true, coreOperationCompare.equal(context, lCollection, rCollection).booleanValue());
        assertEquals(true, coreOperationCompare.equal(context, rCollection, lCollection).booleanValue());

        assertEquals(false, coreOperationCompare.equal(context, lCollection, null).booleanValue());
        assertEquals(false, coreOperationCompare.equal(context, null, lCollection).booleanValue());

        assertEquals(false, coreOperationCompare.equal(context, lIterator, rCollection).booleanValue());
        assertEquals(false, coreOperationCompare.equal(context, rCollection, lIterator).booleanValue());
    }

    @Test
    public void testContains() {
        Collection collection = new ArrayList();
        Iterator iterator = collection.iterator();

        collection.add(new Object());
        collection.add(new Object());

        assertTrue(coreOperationCompare.contains(iterator, new Object()));
        assertFalse(coreOperationCompare.contains(iterator, null));

        Object value = new Object();
        assertTrue(coreOperationCompare.contains(iterator, value));
        assertFalse(coreOperationCompare.contains(iterator, new Object()));
    }

    @Test
    public void testFindMatch() {
        Collection leftCollection = new ArrayList();
        Collection rightCollection = new ArrayList();
        Iterator leftIterator = leftCollection.iterator();
        Iterator rightIterator = rightCollection.iterator();

        leftCollection.add(new Object());
        leftCollection.add(new Object());
        rightCollection.add(new Object());
        rightCollection.add(new Object());

        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
        assertFalse(coreOperationCompare.findMatch(leftIterator, null));
        assertFalse(coreOperationCompare.findMatch(null, rightIterator));
        assertFalse(coreOperationCompare.findMatch(leftIterator, new Object()));
    }
}