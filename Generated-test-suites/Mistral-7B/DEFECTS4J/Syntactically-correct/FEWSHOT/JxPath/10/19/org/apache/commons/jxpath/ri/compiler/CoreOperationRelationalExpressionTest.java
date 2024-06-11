package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionNumber("3"));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("3"), new CoreExpressionNumber("5"));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));
        boolean result = expr.evaluateCompare(-1);
        assertFalse(result);

        result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(1);
        assertTrue(result);
    }

    @Test
    public void testComputeWithInitialContext() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionString("3"));
        EvalContext context = new EvalContext();
        context.setNode(new InitialContext());
        Object result = expr.computeValue(context);
        assertNull(result);

        context = new EvalContext();
        context.setNode(new SelfContext("5"));
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext();
        context.setNode(new SelfContext("6"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeWithCollectionIterators() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        Collection<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new CoreExpressionNumber("5"));
        leftCollection.add(new CoreExpressionNumber("6"));

        Collection<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new CoreExpressionNumber("3"));
        rightCollection.add(new CoreExpressionNumber("5"));

        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Iterators.iterate(leftCollection.iterator());
        Iterator<Object> rightIterator = Iterators.iterate(rightCollection.iterator());
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftCollection.add(new CoreExpressionNumber("7"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeWithSingleIterator() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Iterators.iterate(new CoreExpressionNumber("5").iterator());
        Iterator<Object> rightIterator = Iterators.iterate(new CoreExpressionNumber("3").iterator());
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = Iterators.iterate(new CoreExpressionNumber("6").iterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeWithTwoCollections() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        Collection<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new CoreExpressionNumber("5"));
        leftCollection.add(new CoreExpressionNumber("6"));

        Collection<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new CoreExpressionNumber("3"));
        rightCollection.add(new CoreExpressionNumber("5"));

        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Iterators.iterate(leftCollection.iterator());
        Iterator<Object> rightIterator = Iterators.iterate(rightCollection.iterator());
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftCollection.add(new CoreExpressionNumber("7"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeWithTwoIntegers() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        EvalContext context = new EvalContext();
        double leftValue = InfoSetUtil.doubleValue(new CoreExpressionNumber("5").computeValue(context));
        double rightValue = InfoSetUtil.doubleValue(new CoreExpressionNumber("3").computeValue(context));
        Object result
                = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result, "The result should be true.");
    }

    @Test
    public void testComputeWithTwoDoubles() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5.5"), new CoreExpressionNumber("3.3"));

        EvalContext context = new EvalContext();
        double leftValue = InfoSetUtil.doubleValue(new CoreExpressionNumber("5.5").computeValue(context));
        double rightValue = InfoSetUtil.doubleValue(new CoreExpressionNumber("3.3").computeValue(context));
        Object result
                = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result, "The result should be true.");
    }

    @Test
    public void testComputeWithTwoStrings() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionString("3"));

        EvalContext context = new EvalContext();
        Object leftValue = new CoreExpressionString("5").computeValue(context);
        Object rightValue = new CoreExpressionString("3").computeValue(context);
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeWithTwoStringsWithCompareFunction() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionString("3"));

        EvalContext context = new EvalContext();
        Object leftValue = new CoreExpressionString("5").computeValue(context);
        Object rightValue = new CoreExpressionString("3").computeValue(context);
        boolean result = expr.evaluateCompare(compareStrings(leftValue, rightValue));
        assertFalse(result);
    }

    @Test
    public void testComputeWithTwoStringsWithCompareFunctionAndSameValues() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionString("5"));

        EvalContext context = new EvalContext();
        Object leftValue = new CoreExpressionString("5").computeValue(context);
        Object rightValue = new CoreExpressionString("5").computeValue(context);
        boolean result = expr.evaluateCompare(compareStrings(leftValue, rightValue));
        assertFalse(result);
    }

    @Test
    public void testComputeWithTwoStringsWithCompareFunctionAndDifferentValues() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionString("5"), new CoreExpressionString("3"));

        EvalContext context = new EvalContext();
        Object leftValue = new CoreExpressionString("5").computeValue(context);
        Object rightValue = new CoreExpressionString("3").computeValue(context);
        boolean result = expr.evaluateCompare(compareStrings(leftValue, rightValue));
        assertFalse(result);
    }

    @Test
    public void testComputeWithTwoCollectionsWithContainMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        Collection<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new CoreExpressionNumber("5"));
        leftCollection.add(new CoreExpressionNumber("6"));

        Collection<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new CoreExpressionNumber("3"));
        rightCollection.add(new CoreExpressionNumber("5"));

        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Iterators.iterate(leftCollection.iterator());
        Iterator<Object> rightIterator = Iterators.iterate(rightCollection.iterator());
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftCollection.add(new CoreExpressionNumber("7"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeWithTwoCollectionsWithFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber("5"), new CoreExpressionNumber("3"));

        Collection<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new CoreExpressionNumber("5"));
        leftCollection.add(new CoreExpressionNumber("6"));

        Collection<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new CoreExpressionNumber("3"));
        rightCollection.add(new CoreExpressionNumber("5"));

        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = Iterators.iterate(leftCollection.iterator());
        Iterator<Object> rightIterator = Iterators.iterate(rightCollection.iterator());
        Object result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftCollection.add(new CoreExpressionNumber("7"));
        HashSet<Object> leftSet = new HashSet<>(leftCollection);
        rightCollection.add(new CoreExpressionNumber("6"));
        Iterator<Object> rightIterator2 = Iterators.iterate(rightCollection.iterator());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    private int compareStrings(Object o1, Object o2) {
        String str1 = o1.toString();
        String str2 = o2.toString();
        return str1.compareTo(str2);
    }

}