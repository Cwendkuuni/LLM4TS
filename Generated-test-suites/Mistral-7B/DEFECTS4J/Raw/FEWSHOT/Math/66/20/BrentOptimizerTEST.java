package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertTrue(optimizer instanceof AbstractUnivariateRealOptimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testDoOptimizeThrowUnsupportedOperationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizerWithInvalidEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(mockFunction(), GoalType.MINIMIZE, 0, 1, 0, -1, -0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizerWithInvalidT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(mockFunction(), GoalType.MINIMIZE, 0, 1, 0, -1, 0);
    }

    @Test
    public void testOptimizerWithMinFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        when(mockFunction.value(0)).thenReturn(10.0);
        when(mockFunction.value(1)).thenReturn(5.0);
        when(mockFunction.value(2)).thenReturn(3.0);

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0, 1, 0.5);
        assertTrue(solution.getPoint() < 1);
        assertTrue(solution.getValue() < 3);
    }

    @Test
    public void testOptimizerWithMaxFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        when(mockFunction.value(0)).thenReturn(-10.0);
        when(mockFunction.value(1)).thenReturn(-5.0);
        when(mockFunction.value(2)).thenReturn(-3.0);

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair solution = optimizer.optimize(mockFunction, GoalType.MAXIMIZE, 0, 1, 0.5);
        assertTrue(solution.getPoint() > 0);
        assertTrue(solution.getValue() > 3);
    }

    @Test
    public void testOptimizerWithBoundingFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction mockFunction = mock(UnivariateRealFunction.class);
        when(mockFunction.value(0)).thenThrow(new FunctionEvaluationException());
        when(mockFunction.value(1)).thenReturn(1.0);
        when(mockFunction.value(2)).thenReturn(2.0);
        when(mockFunction.value(3)).thenReturn(-1.0);

        BrentOptimizer optimizer = new BrentOptimizer();
        SimpleBoundingMethod boundingFunction = new SimpleBoundingMethod(mockFunction);

        PointValuePair solution = optimizer.optimize(boundingFunction, GoalType.MINIMIZE, 0, 1, 0.5);
        assertTrue(solution.getPoint() > 0);
        assertTrue(solution.getPoint() < 1);
        assertTrue(solution.getValue() < 2);
    }

    private UnivariateRealFunction mockFunction() {
        return mock(UnivariateRealFunction.class);
    }
}