package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNumbers_thenReturnCorrectBooleanValue() {
        // Given
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(1.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = new CoreNumberExpression(2.0);
        arg2 = new CoreNumberExpression(1.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(1.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_whenOneArgumentIsNotANumber_thenThrowException() {
        // Given
        Expression arg1 = new CoreStringExpression("1");
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When & Then
        op.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNotNumbers_thenThrowException() {
        // Given
        Expression arg1 = new CoreStringExpression("1");
        Expression arg2 = new CoreStringExpression("2");
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When & Then
        op.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenOneArgumentIsNaN_thenReturnCorrectBooleanValue() {
        // Given
        Expression arg1 = new CoreNumberExpression(Double.NaN);
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(Double.NaN);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = new CoreNumberExpression(Double.NaN);
        arg2 = new CoreNumberExpression(Double.NaN);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenBothArgumentsAreNegativeNumbers_thenReturnCorrectBooleanValue() {
        // Given
        Expression arg1 = new CoreNumberExpression(-2.0);
        Expression arg2 = new CoreNumberExpression(-1.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = new CoreNumberExpression(-2.0);
        arg2 = new CoreNumberExpression(-2.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = new CoreNumberExpression(-1.0);
        arg2 = new CoreNumberExpression(-2.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_whenOneArgumentIsInfinite_thenReturnCorrectBooleanValue() {
        // Given
        Expression arg1 = new CoreNumberExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(Double.POSITIVE_INFINITY);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = new CoreNumberExpression(Double.NEGATIVE_INFINITY);
        arg2 = new CoreNumberExpression(1.0);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(Double.NEGATIVE_INFINITY);

        // When
        result = op.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}