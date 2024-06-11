package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertInstanceOf(CoreOperationCompare.class, coreOperationCompare);
        assertEquals(new Expression[]{arg1, arg2}, coreOperationCompare.getExpressions());
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("5");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = new LiteralExpression("true");
        right = new LiteralExpression("true");
        coreOperationCompare = new CoreOperationCompare(left, right);
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = new LiteralExpression("5");
        right = new LiteralExpression("6");
        coreOperationCompare = new CoreOperationCompare(left, right);
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new LiteralExpression("true");
        right = new LiteralExpression("false");
        coreOperationCompare = new CoreOperationCompare(left, right);
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Collection collection = new ArrayList();
        collection.add("apple");
        collection.add("banana");
        Iterator iterator = collection.iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator, new LiteralExpression("apple"));
        assertTrue(coreOperationCompare.contains(iterator, "apple"));
        assertTrue(coreOperationCompare.contains(iterator, "banana"));
        assertFalse(coreOperationCompare.contains(iterator, "orange"));

        coreOperationCompare = new CoreOperationCompare(new LiteralExpression("apple"), iterator);
        assertFalse(coreOperationCompare.contains(iterator, "apple"));
        assertTrue(coreOperationCompare.contains(iterator, "banana"));
        assertFalse(coreOperationCompare.contains(iterator, "orange"));
    }

    @Test
    public void testFindMatch() {
        Collection leftCollection = new ArrayList();
        leftCollection.add("apple");
        leftCollection.add("banana");
        Iterator leftIterator = leftCollection.iterator();

        Collection rightCollection = new ArrayList();
        rightCollection.add("orange");
        rightCollection.add("apple");
        rightCollection.add("banana");
        Iterator rightIterator = rightCollection.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftIterator, rightIterator);
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualWithInvalidArguments() {
        EvalContext context = new EvalContext();
        Expression left = new CoreOperationCompare(null, new LiteralExpression("5"));
        Expression right = new LiteralExpression("5");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.equal(context, left, right);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsWithInvalidArguments() {
        Collection collection = new ArrayList();
        Iterator iterator = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator, new LiteralExpression("apple"));
        coreOperationCompare.contains(iterator, "apple");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatchWithInvalidArguments() {
        Iterator leftIterator = null;
        Iterator rightIterator = new ArrayList().iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftIterator, rightIterator);
        coreOperationCompare.findMatch(leftIterator, rightIterator);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testEqualWithInitialContexts() {
        EvalContext context = new EvalContext();
        InitialContext initialContext1 = new InitialContext();
        InitialContext initialContext2 = new InitialContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new LiteralExpression(" root"), initialContext1);
        Expression left = new LiteralExpression(" root");
        Expression right = initialContext2;
        assertEquals(coreOperationCompare.equal(context, left, right), true);
    }

    @Test
    public void testEqualWithSelfContexts() {
        EvalContext context = new EvalContext();
        SelfContext selfContext1 = new SelfContext();
        SelfContext selfContext2 = new SelfContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new LiteralExpression("."), selfContext1);
        Expression left = new LiteralExpression(".");
        Expression right = selfContext2;
        assertEquals(coreOperationCompare.equal(context, left, right), true);
    }
}