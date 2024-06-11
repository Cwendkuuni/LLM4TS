package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        String[] numbers = {"1", "2", "3", "4"};
        EvalContext context = new EvalContext(new InitialContext(new Object[]{numbers}));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new ConstantExpression(3.0)
        );

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_false() {
        String[] numbers = {"1", "2", "3", "4"};
        EvalContext context = new EvalContext(new InitialContext(new Object[]{numbers}));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new ConstantExpression(3.0)
        );

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_null() {
        EvalContext context = new EvalContext(new SelfContext(""));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Object result = expression.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValue_collection() {
        String[] numbers = {"1", "2", "3", "4"};
        EvalContext context = new EvalContext(new InitialContext(new Object[]{numbers}));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        List list = Arrays.asList(numbers);
        Object result = expression.computeValue(new EvalContext(new InitialContext(list)));

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_collection_false() {
        String[] numbers = {"1", "2", "3", "4"};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        List list = Arrays.asList(numbers);
        Object result = expression.computeValue(new EvalContext(new InitialContext(list)));

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_double() {
        Double[] numbers = {1.5, 2.0, 3.5};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new ConstantExpression(3.0)
        );

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_double_true() {
        Double[] numbers = {1.5, 2.0, 3.5};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new ConstantExpression(2.0)
        );

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_iterator() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(iterator, null)));

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_iterator_false() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(iterator, null)));

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_iterator_collection() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator1 = Arrays.asList(numbers).iterator();
        Iterator iterator2 = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(iterator1, iterator2)));

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_iterator_collection_true() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator1 = Arrays.asList(numbers).iterator();
        Iterator iterator2 = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(iterator1, iterator2)));

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_null_iterator() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Object result = expression.computeValue(new EvalContext(new IteratorContext(null, null)));

        assertNull(result);
    }

    @Test
    public void testComputeValue_null_iterator_collection() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Object result = expression.computeValue(new EvalContext(new IteratorContext(null, new InitialContext(Collections.emptyList()))));

        assertNull(result);
    }

    @Test
    public void testComputeValue_collection_iterator() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator1 = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(new InitialContext(numbers), iterator1)));

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_collection_iterator_true() {
        Object[] numbers = {1, 2, 3, 4};
        EvalContext context = new EvalContext(new InitialContext(numbers));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        Iterator iterator1 = Arrays.asList(numbers).iterator();
        Object result = expression.computeValue(new EvalContext(new IteratorContext(new InitialContext(numbers), iterator1)));

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        int precedence = expression.getPrecedence();

        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new VariableExpression("$x"),
                new VariableExpression("$y")
        );

        assertTrue(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Object left = 3.0;
        Object right = 4.0;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new ConstantExpression(left),
                new ConstantExpression(right)
        );

        boolean result = expression.compute(left, right);

        assertFalse(result);

        left = 4.0;
        right = 3.0;

        result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testReduce() {
        Object left = new InitialContext(new Object[]{1, 2, 3, 4});
        Object right = new IteratorContext(Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator(), null);

        Object resultLeft = expression.reduce(left);
        Object resultRight = expression.reduce(right);

        assertEquals(Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator(), resultLeft);
        assertTrue(resultRight instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Object left = Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator();
        Object right = 3;

        boolean result = expression.containsMatch(left, right);

        assertFalse(result);

        left = Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator();
        right = 3.0;

        result = expression.containsMatch(left, right);

        assertFalse(result);

        left = Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator();
        right = 3;

        ((Iterator) left).next();

        result = expression.containsMatch(left, right);

        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Object left = Arrays.asList(new Integer[]{1, 2, 3, 4}).iterator();
        Object right = Arrays.asList(new Integer[]{3, 2, 1, 4}).iterator();

        boolean result = expression.findMatch(left, right);

        assertTrue(result);
    }
}