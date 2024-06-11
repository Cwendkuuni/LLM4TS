package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.test.TestSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for CoreOperationRelationalExpression class.
 *
 * @since JXPath 1.3
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpressionFactory factory;

    public CoreOperationRelationalExpressionTest() {
        factory = new CoreOperationRelationalExpressionFactory();
    }

    @org.junit.Test
    public void testGreaterThan() {
        Expression left = factory.createNumber(2.0);
        Expression right = factory.createNumber(1.0);
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        Expression left = factory.createNumber(2.0);
        Expression right = factory.createNumber(1.0);
        CoreOperationRelationalExpression expression = factory.createGreaterThanOrEqual(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(1.0);
        right = factory.createNumber(1.0);
        expression = factory.createGreaterThanOrEqual(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThan() {
        Expression left = factory.createNumber(1.0);
        Expression right = factory.createNumber(2.0);
        CoreOperationRelationalExpression expression = factory.createLessThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = factory.createNumber(2.0);
        right = factory.createNumber(1.0);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        Expression left = factory.createNumber(1.0);
        Expression right = factory.createNumber(1.0);
        CoreOperationRelationalExpression expression = factory.createLessThanOrEqual(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(2.0);
        right = factory.createNumber(1.0);
        expression = factory.createLessThanOrEqual(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testDifferentTypes() {
        Expression left = factory.createNumber(2.0);
        Expression right = factory.createString("2");
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testNullLeft() {
        Expression left = factory.createNull();
        Expression right = factory.createNumber(1.0);
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testNullRight() {
        Expression left = factory.createNumber(1.0);
        Expression right = factory.createNull();
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testSameInstance() {
        Expression left = factory.createNumber(1.0);
        Expression right = left;
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testSelfContextComparison() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        context.setNodePointer(left);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCollectionComparison() {
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(1, 2, 3).iterator();
        CoreOperationRelationalExpression expression = factory.createGreaterThan(leftIterator, rightIterator);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftIterator = Arrays.asList(1, 2, 3).iterator();
        rightIterator = Arrays.asList(1, 2).iterator();
        expression = factory.createGreaterThan(leftIterator, rightIterator);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftIterator = Arrays.asList(1, 2, 3).iterator();
        rightIterator = Arrays.asList(1, 2, 3, 4).iterator();
        expression = factory.createLessThan(leftIterator, rightIterator);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testDoubleComparison() {
        Expression left = factory.createNumber(1.0);
        Expression right = factory.createNumber(2.0);
        CoreOperationRelationalExpression expression = factory.createGreaterThan(left, right);
        EvalContext context = TestSupport.newContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = factory.createNumber(2.0);
        right = factory.createNumber(1.0);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(1.0);
        right = factory.createNumber(1.0);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = factory.createNumber(1.0);
        right = factory.createNumber(1.0);
        expression = factory.createGreaterThanOrEqual(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(1.0);
        right = factory.createNumber(1.0);
        expression = factory.createLessThanOrEqual(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(Double.NEGATIVE_INFINITY);
        right = factory.createNumber(Double.NEGATIVE_INFINITY);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = factory.createNumber(Double.NaN);
        right = factory.createNumber(Double.NaN);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = factory.createNumber(Double.NaN);
        right = factory.createNumber(Double.NaN);
        expression = factory.createGreaterThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = factory.createNumber(Double.NEGATIVE_INFINITY);
        right = factory.createNumber(Double.POSITIVE_INFINITY);
        expression = factory.createLessThan(left, right);
        context = TestSupport.newContext();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

}