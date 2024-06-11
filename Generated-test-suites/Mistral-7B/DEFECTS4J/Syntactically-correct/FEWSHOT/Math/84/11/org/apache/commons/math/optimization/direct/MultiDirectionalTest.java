package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.01);
        assertEquals(0.5, optimizer.gamma, 0.01);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.khi, 0.01);
        assertEquals(0.7, optimizer.gamma, 0.01);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        Comparator<PointValuePair> comparator = mock(Comparator.class);
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false),
                                         new RealPointValuePair(new double[]{2.0, 1.0}, 3.0, false),
                                         new RealPointValuePair(new double[]{1.0, 2.0}, 4.0, false),
                                         new RealPointValuePair(new double[]{0.0, 0.0}, 5.0, false)};

        RealPointValuePair best = original[0];

        // Reflection step
        RealPointValuePair reflected = new RealPointValuePair(new double[]{1.5, 1.5}, Double.NaN, false);
        PointValuePair reflectionResult = new PointValuePair(new double[]{1.5, 1.5}, 1.0);
        when(comparator.compare(reflected, best)).thenReturn(-1);
        when(comparator.compare(reflected, reflected)).thenReturn(0);
        when(comparator.compare(best, reflected)).thenReturn(1);

        optimizer.iterateSimplex(comparator);

        verify(comparator).compare(reflected, best);
        verify(comparator).compare(reflected, reflected);
        verify(comparator).compare(best, reflected);

        // Expansion step
        RealPointValuePair expanded = new RealPointValuePair(new double[]{2.1, 2.1}, Double.NaN, false);
        PointValuePair expansionResult = new PointValuePair(new double[]{2.1, 2.1}, 0.5);
        when(comparator.compare(reflected, expanded)).thenReturn(1);
        when(comparator.compare(reflected, best)).thenReturn(-1);

        optimizer.iterateSimplex(comparator);

        verify(comparator).compare(reflected, expanded);
        verify(comparator).compare(reflected, best);

        // Contraction or convergence
        RealPointValuePair contracted = new RealPointValuePair(new double[]{1.2, 1.2}, Double.NaN, false);
        PointValuePair contractionResult = new PointValuePair(new double[]{1.2, 1.2}, 0.1);
        when(comparator.compare(contracted, best)).thenReturn(-1);

        optimizer.iterateSimplex(comparator);

        verify(comparator).compare(contracted, best);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false),
                                         new RealPointValuePair(new double[]{2.0, 1.0}, 3.0, false),
                                         new RealPointValuePair(new double[]{1.0, 2.0}, 4.0, false),
                                         new RealPointValuePair(new double[]{0.0, 0.0}, 5.0, false)};

        LinearFunction function = mock(LinearFunction.class);
        when(function.value(anyArray())).thenAnswer(i -> 0.0);

        optimizer.setFunction(function);

        double coeff = 1.0;
        RealPointValuePair result = optimizer.evaluateNewSimplex(original, coeff, comparator);

        verify(function).value(anyArray());
        assertEquals(original[0].getPointRef(), result.getPointRef());
    }

    @Test
    public void testIterateSimplexWithGoalType() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        Comparator<PointValuePair> comparator = (p1, p2) -> p1.getValue().compareTo(p2.getValue());
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false),
                                         new RealPointValuePair(new double[]{2.0, 1.0}, 3.0, false),
                                         new RealPointValuePair(new double[]{1.0, 2.0}, 4.0, false),
                                         new RealPointValuePair(new double[]{0.0, 0.0}, 5.0, false)};

        InitialGuess initialGuess = mock(InitialGuess.class);
        when(initialGuess.getPoint()).thenReturn(new double[]{0.0, 0.0});
        when(initialGuess.getGoalType()).thenReturn(SimpleGoalType.MINIMIZE);

        optimizer.setInitialGuess(initialGuess);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.iterateSimplex(comparator);

        // Check that the simplied has been updated
        RealPointValuePair[] simplex = optimizer.getSimpleX();
        assertArrayEquals(new double[][]{new double[]{1.5, 1.5},
                                              new double[]{2.1, 2.1},
                                              new double[]{1.2, 1.2},
                                              new double[]{0.0, 0.0}},
                         simplex, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullComparator() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexWithFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false),
                                         new RealPointValuePair(new double[]{2.0, 1.0}, 3.0, false),
                                         new RealPointValuePair(new double[]{1.0, 2.0}, 4.0, false),
                                         new RealPointValuePair(new double[]{0.0, 0.0}, 5.0, false)};

        LinearFunction function = mock(LinearFunction.class);
        when(function.value(anyArray())).thenThrow(new FunctionEvaluationException("Function evaluation error"));

        optimizer.setFunction(function);

        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexWithOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false),
                                         new RealPointValuePair(new double[]{2.0, 1.0}, 3.0, false),
                                         new RealPointValuePair(new double[]{1.0, 2.0}, 4.0, false),
                                         new RealPointValuePair(new double[]{0.0, 0.0}, 5.0, false)};

        optimizer.setMaxIterations(0);

        optimizer.iterateSimplex(comparator);
    }

}