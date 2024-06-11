package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.initializer.AveragedInitialGuess;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.simple.SimpleXSimplex;
import org.apache.commons.math3.optim.simple.SimpleXSimplex.SimplexChange;
import org.apache.commons.math3.optim.simple.SimpleXSimplex.SimplexStatus;
import org.junit.Test;

public class SimplexOptimizerTest {

    private final static double EPSILON = 1e-6;

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new SimpleXSimplex());
        assertNotNull(optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeWithDefaults() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = f -> FunctionUtils.square(f);
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, null);
        assertEquals(-1.0, optimum.getValue(), EPSILON);
        assertArrayEquals(new double[] { 0.0 }, optimum.getPoint(), EPSILON);
    }

    @Test
    public void testOptimizeWithCustomGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = f -> FunctionUtils.pow(FunctionUtils.sum(f), 0.5);
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, optimum.getValue(), EPSILON);
        assertArrayEquals(new double[] { 1.0, 1.0, 1.0 }, optimum.getPoint(), EPSILON);
    }

    @Test
    public void testOptimizeWithCustomInitialGuess() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = f -> FunctionUtils.square(f[0]) + FunctionUtils.square(f[1]);
        InitialGuess initialGuess = new AveragedInitialGuess(2, 2, new double[] { 1.0, 1.0 });
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, initialGuess);
        assertEquals(1.0, optimum.getValue(), EPSILON);
        assertArrayEquals(new double[] { 1.0 }, optimum.getPoint(), EPSILON);
    }

    @Test
    public void testOptimizeWithCustomSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        SimpleXSimplex simplex = new SimpleXSimplex();
        optimizer.setSimplex(simplex);

        MultivariateFunction function = f -> FunctionUtils.square(f[0]) + FunctionUtils.square(f[1]);
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, null);
        assertEquals(1.0, optimum.getValue(), EPSILON);
        assertArrayEquals(new double[] { 1.0 }, optimum.getPoint(), EPSILON);

        PointValuePairList points = new PointValuePairList();
        simplex.iterate(function, points::add);
        SimpleXSimplex simplexAfterIteration = (SimpleXSimplex) optimizer.getSimplex();
        assertEquals(SimplexStatus.OPTIMAL, simplexAfterIteration.getStatus());
        assertEquals(SimplexChange.NO_IMPROVEMENT, simplexAfterIteration.getChange());
    }

    @Test(expected = NullPointerException.class)
    public void testOptimizeWithNullMultivariateFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, null, GoalType.MINIMIZE, null);
    }
}