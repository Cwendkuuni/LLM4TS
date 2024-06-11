package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * JUnit test cases for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertEquals("Unexpected number of arguments", 2, coreOperationCompare.getArguments().length);
    }

    /**
     * Test equals method with null arguments
     */
    @Test
    public void testEqualsWithNullArguments() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        assertTrue("equals should return true with null arguments", coreOperationCompare.equals(null));
    }

    /**
     * Test equals method with same instance
     */
    @Test
    public void testEqualsWithSameInstance() {
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(new Expression(null), new Expression(null));
        CoreOperationCompare coreOperationCompare2 = coreOperationCompare1;
        assertTrue("equals should return true with same instance", coreOperationCompare1.equals(coreOperationCompare2));
    }

    /**
     * Test equals method with different instances
     */
    @Test
    public void testEqualsWithDifferentInstances() {
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(new Expression(null), new Expression(null));
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(new Expression(null), new Expression(null));
        assertFalse("equals should return false with different instances", coreOperationCompare1.equals(coreOperationCompare2));
    }

    /**
     * Test equals method with different types
     */
    @Test
    public void testEqualsWithDifferentTypes() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(null), new Expression(null));
        assertFalse("equals should return false with different types", coreOperationCompare.equals("Test String"));
    }

    /**
     * Test equals method with same values
     */
    @Test
    public void testEqualsWithSameValues() {
        Expression leftExpression = new Expression(new Integer(1));
        Expression rightExpression = new Expression(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(rightExpression, leftExpression);

        assertTrue("equals should return true with equal values", coreOperationCompare.equals(coreOperationCompare2));
    }

    /**
     * Test equals method with different values
     */
    @Test
    public void testEqualsWithDifferentValues() {
        Expression leftExpression = new Expression(new Integer(1));
        Expression rightExpression = new Expression(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);
        assertFalse("equals should return false with different values", coreOperationCompare.equals(new CoreOperationCompare(rightExpression, leftExpression)));
    }

    /**
     * Test equal method with null values
     */
    @Test
    public void testEqualWithNullValues() {
        Expression leftExpression = new Expression(null);
        Expression rightExpression = new Expression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);
        assertTrue("equal should return true with null values", coreOperationCompare.equal(leftExpression, rightExpression));
    }

    /**
     * Test equal method with non-null and non-equal values
     */
    @Test
    public void testEqualWithNonEqualValues() {
        Expression leftExpression = new Expression(new Integer(1));
        Expression rightExpression = new Expression(new Integer(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);
        assertFalse("equal should return false with non-equal values", coreOperationCompare.equal(leftExpression, rightExpression));
    }

    /**
     * Test equal method with pointers
     */
    @Test
    public void testEqualWithPointers() {
        Pointer leftPointer = new Pointer();
        Pointer rightPointer = new Pointer();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(leftPointer), new Expression(rightPointer));
        assertFalse("equal should return false with different pointers", coreOperationCompare.equal(leftPointer, rightPointer));
        leftPointer.setValue(new Integer(1));
        rightPointer.setValue(new Integer(1));
        assertTrue("equal should return true with equal pointers and values", coreOperationCompare.equal(leftPointer, rightPointer));
    }

    /**
     * Test equal method with iterators and Collection
     */
    @Test
    public void testEqualWithIteratorsAndCollection() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        Iterator iterator = collection.iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(iterator), new Expression(new Expression(collection)));
        assertFalse("equal should return false with different iterators and Collection", coreOperationCompare.equal(iterator, collection));
    }

    /**
     * Test contains method with empty iterator and value
     */
    @Test
    public void testContainsWithEmptyIteratorAndValue() {
        Iterator emptyIterator = new ArrayList().iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(emptyIterator), new Expression(new Integer(1)));
        assertFalse("contains should return false with empty iterator and value", coreOperationCompare.contains(emptyIterator, new Integer(1)));
    }

    /**
     * Test contains method with non-empty iterator and value
     */
    @Test
    public void testContainsWithNonEmptyIteratorAndValue() {
        Collection collection = new ArrayList();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        Iterator iterator = collection.iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(iterator), new Expression(new Integer(1)));
        assertTrue("contains should return true with non-empty iterator and value", coreOperationCompare.contains(iterator, new Integer(1)));
    }

    /**
     * Test findMatch method with empty iterators
     */
    @Test
    public void testFindMatchWithEmptyIterators() {
        Iterator leftIterator = new ArrayList().iterator();
        Iterator rightIterator = new ArrayList().iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(leftIterator), new Expression(rightIterator));
        assertFalse("findMatch should return false with empty iterators", coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    /**
     * Test findMatch method with non-empty iterators
     */
    @Test
    public void testFindMatchWithNonEmptyIterators() {
        Collection leftCollection = new ArrayList();
        leftCollection.add(new Integer(1));
        leftCollection.add(new Integer(2));
        Iterator leftIterator = leftCollection.iterator();

        Collection rightCollection = new ArrayList();
        rightCollection.add(new Integer(2));
        rightCollection.add(new Integer(1));
        Iterator rightIterator = rightCollection.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(leftIterator), new Expression(rightIterator));
        assertTrue("findMatch should return true with non-empty iterators", coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

}