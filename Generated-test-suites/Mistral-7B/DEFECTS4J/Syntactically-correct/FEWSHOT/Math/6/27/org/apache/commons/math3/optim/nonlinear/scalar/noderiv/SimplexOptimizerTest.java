package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.initializer.SimplexInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizationMethod;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();
        final Simplex simplex = new Simplex(3, new SimplexInitializer(3));
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeWithDefaultChecker() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();

        final MultivariateFunction f = new ObjectiveFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;

        final PointValuePair result = optimizer.optimize(100, f, goalType, new double[] {1.0, 1.0});
        assertEquals(GoalType.MINIMIZE, goalType);
        assertEquals(1.0, result.getPoint()[0], 0.001);
        assertEquals(1.0, result.getPoint()[1], 0.001);
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithCustomChecker() {
        final double relTol = 0.001;
        final double absTol = 0.0;
        final SimpleValueChecker checker = new SimpleValueChecker(relTol, absTol);
        final SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        final MultivariateFunction f = new ObjectiveFunction() {
            public double value(double[] x) {
                return FunctionUtils.square(x[0]) + FunctionUtils.square(x[1]);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;

        final PointValuePair result = optimizer.optimize(100, f, goalType, new double[] {1.0, 1.0});
        assertEquals(GoalType.MINIMIZE, goalType);
        assertEquals(1.0, result.getPoint()[0], 0.001);
        assertEquals(1.0, result.getPoint()[1], 0.001);
        assertEquals(2.0, result.getValue(), 0.001);

        // Check the convergence.
        final PointValuePair[] previous = optimizer.getPreviousPoints();
        assertArrayEquals(new double[] {1.0, 1.0}, previous[0], 0.001);
        assertArrayEquals(new double[] {1.0, 1.0}, previous[1], 0.001);
        assertArrayEquals(new double[] {1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(2.0, result.getValue(), 0.001);
        assertEquals(true, optimizer.isConverged());
    }

    @Test
    public void testOptimizeWithEmptyData() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();

        assertThrows(NullArgumentException.class, () -> optimizer.optimize(100, null, GoalType.MINIMIZE, new double[0][]));
    }

    @Test
    public void testOptimizeWithNullData() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();

        assertThrows(NullPointerException.class, () -> optimizer.optimize(100, null, GoalType.MINIMIZE, (OptimizationData[]) null));
    }

    @Test
    public void testOptimizeWithWrongData() {
        final SimplexOptimizer optimizer = new SimplexOptimizer();

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(100, new ObjectiveFunction() {}, GoalType.MINIMIZE, new int[][] {{}}));
    }

    @Test
    public void testOptimizeWithSimplexInitializer() {
        final double relTol = 0.001;
        final double absTol = 0.0;
        final SimpleValueChecker checker = new SimpleValueChecker(relTol, absTol);
        final SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        final int n = 2;
        final Simplex simplex = new Simplex(n, new SimplexInitializer(n));

        optimizer.setSimplex(simplex);

        final MultivariateFunction f = new ObjectiveFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;

        final PointValuePair result = optimizer.optimize(100, f, goalType, new double[] {1.0, 1.0});
        assertEquals(GoalType.MINIMIZE, goalType);
        assertEquals(0.0, result.getPoint()[0], 0.001);
        assertEquals(0.0, result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals(true, optimizer.isConverged());
    }

    @Test
    public void testOptimizeWithMaxEvalException() {
        final double relTol = 0.001;
        final double absTol = 0.0;
        final SimpleValueChecker checker = new SimpleValueChecker(relTol, absTol);
        final SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        final MultivariateFunction f = new ObjectiveFunction() {
            public double value(double[] x) {
                throw new MaxEvalException("Maximum number of evaluations reached.");
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;

        assertThrows(MaxEvalException.class, () -> optimizer.optimize(1, f, goalType, new double[] {1.0, 1.0}));
    }
}