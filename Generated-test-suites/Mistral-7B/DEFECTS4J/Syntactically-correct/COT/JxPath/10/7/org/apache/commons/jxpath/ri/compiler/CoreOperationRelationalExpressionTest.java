package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodeLocator;
import org.apache.commons.jxpath.ri.util.Iterators;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}
     */
    @org.junit.Test
    public void testComputeValue() {
        // Given
        CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
        CoreOperationRelationalExpression expr = factory.newInstance(CoreOperationRelationalExpressionType.GT,
                new Expression[] { new ExpressionFactory().newInstance(NodeLocator.class),
                        new ExpressionFactory().newInstance(NodeLocator.class) });

        EvalContext context = new EvalContext();
        NodeLocator left = new NodeLocator(new Object(), context);
        NodeLocator right = new NodeLocator(new Object(), context);

        // When
        Object result = expr.computeValue(context);

        // Then
        assertFalse("Expected false as result", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with true result
     */
    @org.junit.Test
    public void testComputeValueTrue() {
        // Given
        CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
        CoreOperationRelationalExpression expr = factory.newInstance(CoreOperationRelationalExpressionType.GT,
                new Expression[] { new ExpressionFactory().newInstance(NodeLocator.class, 1L),
                        new ExpressionFactory().newInstance(NodeLocator.class, 2L) });

        EvalContext context = new EvalContext();
        NodeLocator left = new NodeLocator(1L, context);
        NodeLocator right = new NodeLocator(2L, context);

        // When
        Object result = expr.computeValue(context);

        // Then
        assertTrue("Expected true as result", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with false result
     */
    @org.junit.Test
    public void testComputeValueFalse() {
        // Given
        CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
        CoreOperationRelationalExpression expr = factory.newInstance(CoreOperationRelationalExpressionType.LT,
                new Expression[] { new ExpressionFactory().newInstance(NodeLocator.class, 2L),
                        new ExpressionFactory().newInstance(NodeLocator.class, 1L) });

        EvalContext context = new EvalContext();
        NodeLocator left = new NodeLocator(2L, context);
        NodeLocator right = new NodeLocator(1L, context);

        // When
        Object result = expr.computeValue(context);

        // Then
        assertFalse("Expected false as result", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with NaN values
     */
    @org.junit.Test
    public void testComputeValueNaN() {
        // Given
        CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
        CoreOperationRelationalExpression expr = factory.newInstance(CoreOperationRelationalExpressionType.EQ,
                new Expression[] { new ExpressionFactory().newInstance(NodeLocator.class),
                        new ExpressionFactory().newInstance(NodeLocator.class) });

        EvalContext context = new EvalContext();
        NodeLocator left = new NodeLocator(Double.NaN, context);
        NodeLocator right = new NodeLocator(Double.NaN, context);

        // When
        Object result = expr.computeValue(context);

        // Then
        assertFalse("Expected false as result", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with different data types
     */
    @org.junit.Test
    public void testComputeValueDifferentDataTypes() {
        // Given
        CoreOperationRelationalExpressionFactory factory = new CoreOperationRelationalExpressionFactory();
        CoreOperationRelationalExpression expr = factory.newInstance(CoreOperationRelationalExpressionType.EQ,
                new Expression[] { new ExpressionFactory().newInstance(NodeLocator.class),
                        new ExpressionFactory().newInstance(NodeLocator.class) });

        EvalContext context = new EvalContext();
        NodeLocator left = new NodeLocator("1", context);
        NodeLocator right = new NodeLocator(1, context);

        // When
        Object result = expr.computeValue(context);

        // Then
        assertTrue("Expected true as result", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}
     */
    @org.junit.Test
    public void testGetPrecedence() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();

        // When
        int precedence = expr.getPrecedence();

        // Then
        assertEquals("Expected precedence 3", 3, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}
     */
    @org.junit.Test
    public void testIsSymmetric() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();

        // When
        boolean symmetric = expr.isSymmetric();

        // Then
        assertFalse("Expected false as symmetric", symmetric);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with compare = 0
     */
    @org.junit.Test
    public void testEvaluateCompareZero() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();

        // When
        boolean result = expr.evaluateCompare(0);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with compare = -1
     */
    @org.junit.Test
    public void testEvaluateCompareMinusOne() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();

        // When
        boolean result = expr.evaluateCompare(-1);

        // Then
        assertTrue("Expected true as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} with compare = 1
     */
    @org.junit.Test
    public void testEvaluateCompareOne() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();

        // When
        boolean result = expr.evaluateCompare(1);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two InitialContexts
     */
    @org.junit.Test
    public void testComputeInitialContexts() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        InitialContext leftInitialContext = new InitialContext();
        InitialContext rightInitialContext = new InitialContext();
        NodeLocator left = new NodeLocator(new Object(), leftInitialContext);
        NodeLocator right = new NodeLocator(new Object(), rightInitialContext);

        // When
        boolean result = expr.compute(left.getValue(), right.getValue());

        // Then
        assertFalse("Expected false as result", result);

        // Reset InitialContexts
        leftInitialContext.reset();
        rightInitialContext.reset();
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two Iterators
     */
    @org.junit.Test
    public void testComputeIterators() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        NodeIterator leftIterator = Iterators.emptyIterator();
        NodeIterator rightIterator = Iterators.emptyIterator();

        // When
        boolean result = expr.compute(leftIterator, rightIterator);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with one Iterator and one value
     */
    @org.junit.Test
    public void testComputeIteratorAndValue() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        NodeIterator leftIterator = Iterators.emptyIterator();
        Object rightValue = 1;

        // When
        boolean result = expr.compute(leftIterator, rightValue);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two Iterators and match
     */
    @org.junit.Test
    public void testComputeIteratorsMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        List<Object> leftValues = Arrays.asList(1, 2);
        List<Object> rightValues = Arrays.asList(2, 3);
        NodeIterator leftIterator = Iterators.forList(leftValues.iterator());
        NodeIterator rightIterator = Iterators.forList(rightValues.iterator());

        // When
        boolean result = expr.compute(leftIterator, rightIterator);

        // Then
        assertTrue("Expected true as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two Iterators and no match
     */
    @org.junit.Test
    public void testComputeIteratorsNoMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        List<Object> leftValues = Arrays.asList(1, 2);
        List<Object> rightValues = Arrays.asList(2, 4);
        NodeIterator leftIterator = Iterators.forList(leftValues.iterator());
        NodeIterator rightIterator = Iterators.forList(rightValues.iterator());

        // When
        boolean result = expr.compute(leftIterator, rightIterator);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with one Iterator and contains match
     */
    @org.junit.Test
    public void testComputeIteratorContainsMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        List<Object> values = Arrays.asList(1, 2, 3);
        NodeIterator iterator = Iterators.forList(values.iterator());
        Object valueToSearch = 2;

        // When
        boolean result = expr.containsMatch(iterator, valueToSearch);

        // Then
        assertTrue("Expected true as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with one Iterator and no match
     */
    @org.junit.Test
    public void testComputeIteratorNoMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        List<Object> values = Arrays.asList(1, 2, 3);
        NodeIterator iterator = Iterators.forList(values.iterator());
        Object valueToSearch = 4;

        // When
        boolean result = expr.containsMatch(iterator, valueToSearch);

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two HashSets and match
     */
    @org.junit.Test
    public void testComputeHashSetsMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        HashSet<Object> leftSet = new HashSet<>();
        leftSet.add(1);
        leftSet.add(2);
        HashSet<Object> rightSet = new HashSet<>();
        rightSet.add(2);
        rightSet.add(3);

        // When
        boolean result = expr.findMatch(Iterators.forList(leftSet.iterator()), Iterators.forList(rightSet.iterator()));

        // Then
        assertTrue("Expected true as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} with two HashSets and no match
     */
    @org.junit.Test
    public void testComputeHashSetsNoMatch() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        HashSet<Object> leftSet = new HashSet<>();
        leftSet.add(1);
        leftSet.add(2);
        HashSet<Object> rightSet = new HashSet<>();
        rightSet.add(2);
        rightSet.add(4);

        // When
        boolean result = expr.findMatch(Iterators.forList(leftSet.iterator()), Iterators.forList(rightSet.iterator()));

        // Then
        assertFalse("Expected false as result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} with SelfContext
     */
    @org.junit.Test
    public void testReduceSelfContext() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        NodeLocator nodeLocator = new NodeLocator(new Object(), new SelfContext());

        // When
        Object result = expr.reduce(nodeLocator.getValue());

        // Then
        assertNotNull("Expected non-null result", result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} with Collection
     */
    @org.junit.Test
    public void testReduceCollection() {
        // Given
        CoreOperationRelationalExpression expr = new CoreOperationGtExpression();
        Collection<Object> collection = Collections.singletonList(new Object());

        // When
        Object result = expr.reduce(collection.iterator().next());

        // Then
        assertNotNull("Expected non-null result", result);
    }

}