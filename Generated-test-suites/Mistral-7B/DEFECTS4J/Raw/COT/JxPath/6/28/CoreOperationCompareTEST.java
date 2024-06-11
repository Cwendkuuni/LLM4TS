package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.InfoSetUtil;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression expr1 = new Expression(1);
        Expression expr2 = new Expression(2);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        assertEquals(2, coreOperationCompare.getArity());
        assertNotNull(coreOperationCompare.getLeftExpression());
        assertNotNull(coreOperationCompare.getRightExpression());
    }

    @Test
    public void testEqualMethodNullContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        assertFalse(coreOperationCompare.equal(null, null, null));
    }

    @Test
    public void testEqualMethodNullLeft() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, new Expression(1)); ;
        assertFalse(coreOperationCompare.equal(null, null, new Expression(1)));
    }

    @Test
    public void testEqualMethodNullRight() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(1), null);
        assertFalse(coreOperationCompare.equal(new Expression(1), null, null));
    }

    @Test
    public void testEqualMethodNullLeftAndRight() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        assertFalse(coreOperationCompare.equal(null, null, null));
    }

    @Test
    public void testEqualMethodBooleanValuesTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(true), new Expression(true));
        assertTrue(coreOperationCompare.equal(null, new Expression(true), new Expression(true)));
    }

    @Test
    public void testEqualMethodBooleanValuesFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(true), new Expression(false));
        assertFalse(coreOperationCompare.equal(null, new Expression(true), new Expression(false)));
    }

    @Test
    public void testEqualMethodNumberValuesEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(1.0), new Expression(1.0));
        assertTrue(coreOperationCompare.equal(null, new Expression(1.0), new Expression(1.0)));
    }

    @Test
    public void testEqualMethodNumberValuesNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(1.0), new Expression(2.0));
        assertFalse(coreOperationCompare.equal(null, new Expression(1.0), new Expression(2.0)));
    }

    @Test
    public void testEqualMethodStringValuesEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression("test"), new Expression("test"));
        assertTrue(coreOperationCompare.equal(null, new Expression("test"), new Expression("test")));
    }

    @Test
    public void testEqualMethodStringValuesNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression("test"), new Expression("test1"));
        assertFalse(coreOperationCompare.equal(null, new Expression("test"), new Expression("test1")));
    }

    @Test
    public void testEqualMethodObjectsEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object()), new Expression(new Object()));
        assertTrue(coreOperationCompare.equal(null, new Expression(new Object()), new Expression(new Object())));
    }

    @Test
    public void testEqualMethodObjectsNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object()), new Expression(new Object()[0]));
        assertFalse(coreOperationCompare.equal(null, new Expression(new Object()), new Expression(new Object()[0])));
    }

    @Test
    public void testEqualMethodPointersEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Pointer(1)), new Expression(new Pointer(1)));
        assertTrue(coreOperationCompare.equal(null, new Expression(new Pointer(1)), new Expression(new Pointer(1))));
    }

    @Test
    public void testEqualMethodPointersNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Pointer(1)), new Expression(new Pointer(2)));
        assertFalse(coreOperationCompare.equal(null, new Expression(new Pointer(1)), new Expression(new Pointer(2))));
    }

    @Test
    public void testEqualMethodIteratorAndValue() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection.iterator()), new Expression(1));
        assertFalse(coreOperationCompare.equal(null, new Expression(collection.iterator()), new Expression(1)));
    }

    @Test
    public void testEqualMethodValueAndIterator() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(1), new Expression(collection.iterator()));
        assertFalse(coreOperationCompare.equal(null, new Expression(1), new Expression(collection.iterator())));
    }

    @Test
    public void testEqualMethodIteratorsEqual() {
        Collection collection1 = new ArrayList();
        collection1.add(new Integer(1));
        Collection collection2 = new ArrayList();
        collection2.add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection1.iterator()), new Expression(collection2.iterator()));
        assertTrue(coreOperationCompare.equal(null, new Expression(collection1.iterator()), new Expression(collection2.iterator())));
    }

    @Test(expected = IllegalStateException.class)
    public void testEqualMethodThrowsExceptionWhenLeftAndRightAreDifferentTypes() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(1), new Expression("test"));
        coreOperationCompare.equal(null, new Expression(1), new Expression("test"));
    }

    @Test
    public void testContainsMethodNullIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, new Expression(new Integer(1)));
        assertFalse(coreOperationCompare.contains(null, new Expression(new Integer(1))));
    }

    @Test
    public void testContainsMethodEmptyIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new ArrayList().iterator()), new Expression(new Integer(1)));
        assertFalse(coreOperationCompare.contains(null, new Expression(new Integer(1))));
    }

    @Test
    public void testContainsMethodValueFound() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection.iterator()), new Expression(new Integer(1)));
        assertTrue(coreOperationCompare.contains(null, new Expression(new Integer(1))));
    }

    @Test
    public void testContainsMethodValueNotFound() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection.iterator()), new Expression(new Integer(3)));
        assertFalse(coreOperationCompare.contains(null, new Expression(new Integer(3))));
    }

    @Test
    public void testFindMatchMethodNullLeftIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, new Expression(new ArrayList().iterator()));
        assertFalse(coreOperationCompare.findMatch(null, new Expression(new ArrayList().iterator())));
    }

    @Test
    public void testFindMatchMethodNullRightIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new ArrayList().iterator()), null);
        assertFalse(coreOperationCompare.findMatch(new Expression(new ArrayList().iterator()), null));
    }

    @Test
    public void testFindMatchMethodMatchesFound() {
        Collection collection1 = new ArrayList();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        Collection collection2 = new ArrayList();
        collection2.add(new Integer(2));
        collection2.add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection1.iterator()), new Expression(collection2.iterator()));
        assertTrue(coreOperationCompare.findMatch(new Expression(collection1.iterator()), new Expression(collection2.iterator())));
    }

    @Test
    public void testFindMatchMethodMatchesNotFound() {
        Collection collection1 = new ArrayList();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        Collection collection2 = new ArrayList();
        collection2.add(new Integer(3));
        collection2.add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection1.iterator()), new Expression(collection2.iterator()));
        assertFalse(coreOperationCompare.findMatch(new Expression(collection1.iterator()), new Expression(collection2.iterator())));
    }

}