package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare
 */
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new Expression(new LiteralExpression(1));
        Expression arg2 = new Expression(new LiteralExpression(1));

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(1)),
            new Expression(new LiteralExpression(2))
        );
        int precedence = coreOperationCompare.getPrecedence();
        assertTrue(precedence == 2);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(1)),
            new Expression(new LiteralExpression(2))
        );
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqualNullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(null)),
            new Expression(new LiteralExpression(null))
        );
        boolean result = coreOperationCompare.equal(context, null, null);
        assertTrue(result);
    }

    @Test
    public void testEqualNullLeft() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(null)),
            new Expression(new LiteralExpression(1))
        );
        boolean result = coreOperationCompare.equal(context, null, new LiteralExpression(1));
        assertFalse(result);
    }

    @Test
    public void testEqualNullRight() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(1)),
            new Expression(new LiteralExpression(null))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(1), null);
        assertFalse(result);
    }

    @Test
    public void testEqualBoolean() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(true)),
            new Expression(new LiteralExpression(true))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(true), new LiteralExpression(true));
        assertTrue(result);
    }

    @Test
    public void testEqualBooleanFalse() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(true)),
            new Expression(new LiteralExpression(false))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(true), new LiteralExpression(false));
        assertFalse(result);
    }

    @Test
    public void testEqualNumber() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(1.0)),
            new Expression(new LiteralExpression(1.0))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(1.0), new LiteralExpression(1.0));
        assertTrue(result);
    }

    @Test
    public void testEqualNumberDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(1.0)),
            new Expression(new LiteralExpression(2.0))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(1.0), new LiteralExpression(2.0));
        assertFalse(result);
    }

    @Test
    public void testEqualString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression("abc")),
            new Expression(new LiteralExpression("abc"))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression("abc"), new LiteralExpression("abc"));
        assertTrue(result);
    }

    @Test
    public void testEqualStringDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression("abc")),
            new Expression(new LiteralExpression("def"))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression("abc"), new LiteralExpression("def"));
        assertFalse(result);
    }

    @Test
    public void testEqualObject() {
        EvalContext context = new EvalContext();
        Object left = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new LiteralExpression(left))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(left), new LiteralExpression(left));
        assertTrue(result);
    }

    @Test
    public void testEqualObjectDifferent() {
        EvalContext context = new EvalContext();
        Object left = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new LiteralExpression(new Object()))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(left), new LiteralExpression(new Object()));
        assertFalse(result);
    }

    @Test
    public void testContainsIteratorContainsValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(Arrays.asList(1, 2, 3).iterator())),
            new Expression(new LiteralExpression(2))
        );
        boolean result = coreOperationCompare.contains(context, Arrays.asList(1, 2, 3).iterator(), new LiteralExpression(2));
        assertTrue(result);
    }

    @Test
    public void testContainsIteratorDoesNotContainValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(Arrays.asList(1, 2, 3).iterator())),
            new Expression(new LiteralExpression(4))
        );
        boolean result = coreOperationCompare.contains(context, Arrays.asList(1, 2, 3).iterator(), new LiteralExpression(4));
        assertFalse(result);
    }

    @Test
    public void testFindMatchIteratorMatchesValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(Arrays.asList(1, 2, 3).iterator())),
            new Expression(new IteratorExpression(Arrays.asList(1, 2, 3).iterator()))
        );
        boolean result = coreOperationCompare.findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(1, 2, 3).iterator());
        assertTrue(result);
    }

    @Test
    public void testFindMatchIteratorDoesNotMatchValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(Arrays.asList(1, 2, 3).iterator())),
            new Expression(new IteratorExpression(Arrays.asList(4, 5, 6).iterator()))
        );
        boolean result = coreOperationCompare.findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(4, 5, 6).iterator());
        assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindMatchCollectionIsNull() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(null)),
            new Expression(new LiteralExpression(null))
        );
        coreOperationCompare.findMatch(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindMatchCollectionEmpty() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(Collections.emptyList())),
            new Expression(new LiteralExpression(Collections.emptyList()))
        );
        coreOperationCompare.findMatch(Collections.emptyList().iterator(), Collections.emptyList().iterator());
    }

    @Test
    public void testFindMatchCollectionMatchesValue() {
        EvalContext context = new EvalContext();
        List<Object> list1 = Arrays.asList(1, 2, 3);
        List<Object> list2 = Arrays.asList(1, 2, 3, 1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(list1)),
            new Expression(new LiteralExpression(list2))
        );
        boolean result = coreOperationCompare.findMatch(list1.iterator(), list2.iterator());
        assertTrue(result);
    }

    @Test
    public void testFindMatchCollectionDoesNotMatchValue() {
        EvalContext context = new EvalContext();
        List<Object> list1 = Arrays.asList(1, 2, 3);
        List<Object> list2 = Arrays.asList(1, 2, 4);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(list1)),
            new Expression(new LiteralExpression(list2))
        );
        boolean result = coreOperationCompare.findMatch(list1.iterator(), list2.iterator());
        assertFalse(result);
    }

    @Test
    public void testEqualPointer() {
        EvalContext context = new EvalContext();
        Pointer left = new Pointer("a/b/c");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new LiteralExpression(left))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(left), new LiteralExpression(left));
        assertTrue(result);
    }

    @Test
    public void testEqualPointerDifferent() {
        EvalContext context = new EvalContext();
        Pointer left = new Pointer("a/b/c");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new LiteralExpression(new Pointer("a/b/d")))
        );
        boolean result = coreOperationCompare.equal(context, new LiteralExpression(left), new LiteralExpression(new Pointer("a/b/d")));
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullLeftAndRight() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(null)),
            new Expression(new LiteralExpression(null))
        );
        coreOperationCompare.equal(context, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullLeftAndRightValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(null)),
            new Expression(new LiteralExpression(null))
        );
        coreOperationCompare.equal(context, null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testEqualLeftIsPointerRightIsIterable() {
        EvalContext context = new EvalContext();
        Pointer left = new Pointer("a/b/c");
        Iterator<Object> rightIterator = Arrays.asList(1, 2, 3).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new IteratorExpression(rightIterator))
        );
        coreOperationCompare.equal(context, new LiteralExpression(left), new IteratorExpression(rightIterator));
    }

    @Test(expected = ClassCastException.class)
    public void testEqualLeftIsIterableRightIsPointer() {
        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Arrays.asList(1, 2, 3).iterator();
        Pointer right = new Pointer("a/b/c");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(leftIterator)),
            new Expression(new LiteralExpression(right))
        );
        coreOperationCompare.equal(context, new IteratorExpression(leftIterator), new LiteralExpression(right));
    }

    @Test(expected = ClassCastException.class)
    public void testContainsLeftIsPointerRightIsIterable() {
        EvalContext context = new EvalContext();
        Pointer left = new Pointer("a/b/c");
        Iterator<Object> rightIterator = Arrays.asList(1, 2, 3).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new LiteralExpression(left)),
            new Expression(new IteratorExpression(rightIterator))
        );
        coreOperationCompare.contains(context, left, rightIterator);
    }

    @Test(expected = ClassCastException.class)
    public void testContainsLeftIsIterableRightIsPointer() {
        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Arrays.asList(1, 2, 3).iterator();
        Pointer right = new Pointer("a/b/c");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new Expression(new IteratorExpression(leftIterator)),
            new Expression(new LiteralExpression(right))
        );
        coreOperationCompare.contains(context, leftIterator, right);
    }

}