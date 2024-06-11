package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Comparator;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSimple;
import org.apache.commons.math3.optim.nonlinear.scalar.InitialGuessRandom;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationDataSimple;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.VectorValueGoalType;
import org.apache.commons.math3.optim.nonlinear.vector.initialguess.VectorValueInitialGuess;
import org.apache.commons.math3.optim.nonlinear.vector.optimizationdata.VectorValueOptimizationData;
import org.apache.commons.math3.optim.nonlinear.vector.optimizationdata.VectorValueOptimizationDataSimple;
import org.apache.commons.math3.optim.nonlinear.vector.optimizer.SimplexOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.point.VectorValuePoint;
import org.apache.commons.math3.optim.nonlinear.vector.point.VectorValuePointValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.util.VectorFunctions;
import org.apache.commons.math3.special.Functions;
import org.junit.Test;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    private static final double EPSILON = 1e-6;
    private static final double ACCURACY = 1e-5;

    /**
     * Test constructor with SimpleValueChecker convergence checker.
     */
    @Test
    public void testConstructorWithSimpleValueChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with custom ConvergenceChecker.
     */
    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() <= previous.getValue() + ACCURACY;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(convergenceChecker);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with relative and absolute thresholds.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1e-3, 1e-4);
        assertNotNull(optimizer);
    }

    /**
     * Test setSimplex method.
     */
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new DuffinSchmitzSimplex();
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.simplex);
        assertSame(simplex, optimizer.simplex);
    }

    /**
     * Test optimize method with a simple objective function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Functions.SIN(Functions.PI * x[0]) + x[1] * Functions.SIN(Functions.PI * x[1]);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueInitialGuess initialGuess = new VectorValueInitialGuess(
                new double[]{0.1, 0.1});
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[]{initialGuess});
        PointValuePair result = optimizer.optimize(50, f, VectorValueGoalType.MINIMIZE, optimizationData);
        double[] expected = new double[]{0.314159265359, 0.314159265359};
        assertArrayEquals(expected, result.getPoint(), EPSILON);
        double actual = result.getValue();
        assertFalse(Double.isNaN(actual));
        assertEquals(-1.184694468268278, actual, EPSILON);
    }

    /**
     * Test optimize method with an objective function having multiple local
     * minima.
     */
    @Test
    public void testOptimizeFunctionWithMultipleLocalMinima() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Functions.SIN(Functions.PI * x[0]) * Functions.SIN(Functions.PI * x[1]);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueInitialGuess initialGuess = new VectorValueInitialGuess(
                new double[]{0.5, 0.5});
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[]{initialGuess});
        PointValuePair result = optimizer.optimize(50, f, VectorValueGoalType.MINIMIZE, optimizationData);
        double[] expected = new double[]{0.25, 0.25};
        assertArrayEquals(expected, result.getPoint(), EPSILON);
        double actual = result.getValue();
        assertFalse(Double.isNaN(actual));
        assertEquals(0.04082482904638632, actual, EPSILON);
    }

    /**
     * Test optimize method with an objective function having local maxima and
     * a global minimum.
     */
    @Test
    public void testOptimizeFunctionWithLocalMaximaAndGlobalMinimum() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] >= 1 || x[0] <= -1 || x[1] >= 1 || x[1] <= -1) {
                    return Double.POSITIVE_INFINITY;
                }
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueInitialGuess initialGuess = new VectorValueInitialGuess(
                new double[]{-0.5, -0.5});
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[]{initialGuess});
        PointValuePair result = optimizer.optimize(50, f, VectorValueGoalType.MINIMIZE, optimizationData);
        double[] expected = new double[]{-0.5, -0.5};
        assertArrayEquals(expected, result.getPoint(), EPSILON);
        double actual = result.getValue();
        assertFalse(Double.isNaN(actual));
        assertEquals(0.25, actual, EPSILON);
    }

    /**
     * Test optimize method with a rosenbrock function.
     */
    @Test
    public void testOptimizeRosenbrockFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    sum += 100.0 * (Functions.POW(x[i + 1] - Functions.POW(x[i], 2), 2)
                            + Functions.POW(x[i] - 1, 2));
                }
                return sum;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueInitialGuess initialGuess = new VectorValueInitialGuess(
                new double[]{3.0, 3.0});
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[]{initialGuess});
        PointValuePair result = optimizer.optimize(50, f, VectorValueGoalType.MINIMIZE, optimizationData);
        double[] expected = new double[]{1.0, 1.0};
        assertArrayEquals(expected, result.getPoint(), EPSILON);
        double actual = result.getValue();
        assertFalse(Double.isNaN(actual));
        assertEquals(0.0, actual, EPSILON);
    }

    /**
     * Test optimize method with an objective function that is constantly
     * increasing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionThatIsConstantlyIncreasing() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] + x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[0]);
        optimizer.optimize(1, f, VectorValueGoalType.MINIMIZE, optimizationData);
        fail("The expected IllegalArgumentException was not thrown.");
    }

    /**
     * Test optimize method with an objective function that is not defined for
     * given input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionNotDefinedForGivenInput() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueInitialGuess initialGuess = new VectorValueInitialGuess(
                new double[]{0.1, 0.1});
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[]{initialGuess});
        optimizer.optimize(50, f, VectorValueGoalType.MINIMIZE, optimizationData);
        fail("The expected IllegalArgumentException was not thrown.");
    }

    /**
     * Test optimize method with an objective function that is not multivariate.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionNotMultivariate() {
        FunctionUtils func = new FunctionUtils() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        VectorValueOptimizationData optimizationData = new VectorValueOptimizationDataSimple(
                new OptimizationData[0]);
        try {
            optimizer.optimize(50, func, VectorValueGoalType.MINIMIZE, optimizationData);
            fail("The expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
}