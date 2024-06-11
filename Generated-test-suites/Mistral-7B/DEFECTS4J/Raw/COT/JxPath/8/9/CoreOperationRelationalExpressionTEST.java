package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.util.Iterators;
import org.apache.commons.jxpath.ri.util.TestUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_booleanTrueAndBooleanTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(true), new CoreConstantExpression(true));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_booleanFalseAndBooleanFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(false), new CoreConstantExpression(false));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_booleanTrueAndBooleanFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(true), new CoreConstantExpression(false));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_booleanFalseAndBooleanTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(false), new CoreConstantExpression(true));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_doubleGreaterThanDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(2.5), new CoreConstantExpression(1.5));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_doubleLessThanDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationLt(
                new CoreConstantExpression(1.5), new CoreConstantExpression(2.5));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_doubleGreaterThanOrEqualDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationGe(
                new CoreConstantExpression(2.5), new CoreConstantExpression(1.5));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_doubleLessThanOrEqualDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationLe(
                new CoreConstantExpression(1.5), new CoreConstantExpression(2.5));
        Object result = expression.computeValue(new EvalContext(new InfoSet()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_invalidArgumentType() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(2.5));
        expression.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testFindMatch_iteratorsWithMatchingValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertTrue(result);
    }

    @Test
    public void testFindMatch_iteratorsWithNoMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(2));
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(2, 3, 4).iterator();
        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test
    public void testFindMatch_iteratorsWithOneMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        Iterator leftIterator = Arrays.asList(1, 2, 3, 1).iterator();
        Iterator rightIterator = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertTrue(result);
    }

    @Test
    public void testFindMatch_iteratorsWithMismatchedSize() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(2));
        Iterator leftIterator = Arrays.asList(1, 2, 3).iterator();
        Iterator rightIterator = Arrays.asList(2).iterator();
        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test
    public void testContainsMatch_iteratorWithMatchingValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        Iterator iter = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.containsMatch(iter, 1);
        assertTrue(result);
    }

    @Test
    public void testContainsMatch_iteratorWithNoMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(2));
        Iterator iter = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.containsMatch(iter, 2);
        assertFalse(result);
    }

    @Test
    public void testContainsMatch_iteratorWithEmptyList() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        Iterator iter = Iterators.emptyIterator();
        boolean result = expression.containsMatch(iter, 1);
        assertFalse(result);
    }

    @Test
    public void testContainsMatch_iteratorWithNullValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        Iterator iter = Iterators.emptyIterator();
        boolean result = expression.containsMatch(iter, null);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_equal() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(0, compareResult);
    }

    @Test
    public void testEvaluateCompare_greater() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(-1, compareResult);
    }

    @Test
    public void testEvaluateCompare_lesser() {
        CoreOperationRelationalExpression expression = new CoreOperationLt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(1, compareResult);
    }

    @Test
    public void testEvaluateCompare_equalWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(0, compareResult);
    }

    @Test
    public void testEvaluateCompare_greaterWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(1.0));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(-1, compareResult);
    }

    @Test
    public void testEvaluateCompare_lesserWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(Double.NaN));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(1, compareResult);
    }

    @Test
    public void testEvaluateCompare_greaterThanZero() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(1.1));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(-1, compareResult);
    }

    @Test
    public void testEvaluateCompare_lessThanZero() {
        CoreOperationRelationalExpression expression = new CoreOperationLt(
                new CoreConstantExpression(1.0), new CoreConstantExpression(1.1));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(1, compareResult);
    }

    @Test
    public void testEvaluateCompare_equalToZero() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(
                new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
        int compareResult = expression.evaluateCompare(0);
        assertEquals(0, compareResult);
    }
}