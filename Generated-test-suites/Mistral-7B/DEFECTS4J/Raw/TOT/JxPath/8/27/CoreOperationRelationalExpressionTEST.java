package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test for greater than operation.
     */
    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(3));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(3);
        result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(7);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for greater than or equal operation.
     */
    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(3));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(3);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(5);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(7);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for less than operation.
     */
    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(3), new ConstantExpression(5));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(3);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(5);
        result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(7);
        result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test for less than or equal operation.
     */
    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(3);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(5);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(7);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for iterators.
     */
    @Test
    public void testIterators() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new IteratorExpression(list.iterator()),
                new ConstantExpression(3));

        EvalContext context = new EvalContext();
        context.setCurrentNode(Collections.EMPTY_LIST.iterator());

        Object result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(list.iterator());

        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for doubles.
     */
    @Test
    public void testDoubles() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(3.1),
                new ConstantExpression(2.5));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(2.0);
        result = expression.computeValue(context);

        assertFalse("Expected false, but was " + result, result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setCurrentNode(3.1);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(3.2);
        result = expression.computeValue(context);

        assertTrue("Expected true, but was " + result, result instanceof Boolean);
        assertTrue((Boolean) result);
    }

}