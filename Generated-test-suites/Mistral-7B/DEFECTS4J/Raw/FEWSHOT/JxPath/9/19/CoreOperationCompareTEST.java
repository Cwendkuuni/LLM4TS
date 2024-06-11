package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    @Test
    public void testEquals() {
        Expression left = new ConstantExpression(new Integer(1));
        Expression right = new ConstantExpression(new Integer(1));

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        assertTrue(op.equals(op));
        assertTrue(op.equals(new CoreOperationCompare(left, right)));
    }

    @Test
    public void testUnequalOperands() {
        Expression left = new ConstantExpression(new Integer(1));
        Expression right = new ConstantExpression(new Integer(2));

        CoreOperationCompare op = new CoreOperationCompare(left, right);

        assertFalse(op.equals(null));
        assertFalse(op.equals(""));
        assertFalse(op.equals(new Object()));
        assertFalse(op.equals(new CoreOperationAdd(left, right)));

        assertFalse(op.equals(left));
        assertFalse(op.equals(right));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));

        int precedence = op.getPrecedence();

        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1)));

        assertTrue(op.isSymmetric());

        op = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));

        assertFalse(op.isSymmetric());
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));

        assertTrue(op.equal(null, null, null));
    }

    @Test
    public void testEqualWithBooleanValues() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.TRUE));

        assertTrue(op.equal(Boolean.TRUE, Boolean.TRUE, null));
        assertTrue(op.equal(Boolean.FALSE, Boolean.FALSE, null));
        assertFalse(op.equal(Boolean.TRUE, Boolean.FALSE, null));
        assertFalse(op.equal(Boolean.FALSE, Boolean.TRUE, null));
    }

    @Test
    public void testEqualWithNumbers() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(1.0)));

        assertTrue(op.equal(1.0, 1.0, null));

        op = new CoreOperationCompare(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(2.0)));

        assertFalse(op.equal(1.0, 2.0, null));
    }

    @Test
    public void testEqualWithStrings() {
        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression("foo"), new ConstantExpression("foo"));

        assertTrue(op.equal("foo", "foo", null));

        op = new CoreOperationCompare(new ConstantExpression("foo"), new ConstantExpression("bar"));

        assertFalse(op.equal("foo", "bar", null));
    }

    @Test
    public void testEqualWithPointers() {
        Pointer leftPointer = new Pointer("foo");
        Pointer rightPointer = new Pointer("foo");

        CoreOperationCompare op = new CoreOperationCompare(new ConstantExpression(leftPointer), new ConstantExpression(rightPointer));

        assertTrue(op.equal(leftPointer, rightPointer, null));
    }

    @Test
    public void testEqualWithIteratorsAndNullValues() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { null }),
                new ConstantExpression(new Iterator[] { null }));

        assertTrue(op.equal(null, null, null));
    }

    @Test
    public void testEqualWithIteratorsAndSameValues() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { new ConstantExpression(1), new ConstantExpression(2) }),
                new ConstantExpression(new Iterator[] { new ConstantExpression(1), new ConstantExpression(2) }));

        assertTrue(op.equal(1, 1, null));
        assertTrue(op.equal(2, 2, null));
    }

    @Test
    public void testEqualWithIteratorsAndDifferentValues() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { new ConstantExpression(1), new ConstantExpression(2) }),
                new ConstantExpression(new Iterator[] { new ConstantExpression(2), new ConstantExpression(1) }));

        assertFalse(op.equal(1, 2, null));
        assertFalse(op.equal(2, 1, null));
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(2, 1, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { leftIterator }),
                new ConstantExpression(new Iterator[] { rightIterator }));

        assertTrue(op.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatchWithNullValues() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { null }),
                new ConstantExpression(new Iterator[] { null }));

        assertTrue(op.findMatch(null, null));
    }

    @Test
    public void testFindMatchWithNoMatch() {
        Iterator leftIterator = Arrays.asList(1, 2).iterator();
        Iterator rightIterator = Arrays.asList(3, 4).iterator();

        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { leftIterator }),
                new ConstantExpression(new Iterator[] { rightIterator }));

        assertFalse(op.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testContains() {
        Iterator iter = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(iter, new ConstantExpression(2));

        assertTrue(op.contains(iter, 2));
    }

    @Test
    public void testContainsWithNullValue() {
        Iterator iter = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(iter, null);

        assertFalse(op.contains(iter, null));
    }

    @Test
    public void testContainsWithNotMatchingValue() {
        Iterator iter = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(iter, new ConstantExpression(4));

        assertFalse(op.contains(iter, 4));
    }

    @Test
    public void testContainsWithMatchingValue() {
        Iterator iter = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(iter, new ConstantExpression(2));

        assertTrue(op.contains(iter, 2));
    }

    @Test
    public void testContainsWithEmptyCollection() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] {}),
                new ConstantExpression(new Integer(1)));

        assertFalse(op.contains(null, new Integer(1)));
    }

    @Test
    public void testContainsWithNullCollection() {
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(null),
                new ConstantExpression(new Integer(1)));

        assertFalse(op.contains(null, new Integer(1)));
    }

    @Test
    public void testContainsWithEmptyValue() {
        Iterator iter = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare op = new CoreOperationCompare(iter, new ConstantExpression(""));

        assertFalse(op.contains(iter, ""));
    }

    @Test
    public void testContainsWithValueContainedInCollection() {
        Collection collection = new HashSet();
        collection.add(new Integer(1));
        collection.add(new Integer(2));

        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new Iterator[] { collection.iterator() }),
                new ConstantExpression(new Integer(1)));

        assertTrue(op.contains(null, new Integer(1)));
    }
}