package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionWithGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.simplextools.AbstractSimplex;
import org.apache.commons.math3.optim.simplextools.SimplexPoint;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    private static final double EPSILON = 1.0e-10;

    private MultivariateFunction f;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        f = new RastriginFunction(10);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON));
    }

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testSimpleOptimization() {
        InitialGuess initialGuess = new DistanceInitialGuess(0.5);
        OptimizationData data = new OptimizationData(initialGuess);
        optimizer.optimize(5, f, GoalType.MINIMIZE, data);
        PointValuePair result = optimizer.getBestPoint();
        double expected = FunctionUtils.VALUE_MIN;
        assertEquals(expected, result.getValue(), EPSILON);
    }

    /**
     * Test optimization of a more complex function.
     */
    @Test
    public void testComplexOptimization() {
        InitialGuess initialGuess = new DistanceInitialGuess(0.5);
        OptimizationData data = new OptimizationData(initialGuess);
        optimizer.optimize(50, f, GoalType.MINIMIZE, data);
        PointValuePair result = optimizer.getBestPoint();
        double expected = -14.0467;
        assertEquals(expected, result.getValue(), EPSILON);
    }

    /**
     * Test optimization of a function with constraints.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testConstraintOptimization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                if (point[0] < 0) {
                    throw new IllegalArgumentException();
                }
                return Math.sin(point[0]) + Math.cos(point[1]);
            }
        };
        optimizer.optimize(50, f, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization of a function with non-finite values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonFiniteFunction() {
        MultivariateFunction f = new RastriginFunctionWithInfiniteValues(10);
        optimizer.optimize(50, f, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization of a function without a goal.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoGoalType() {
        optimizer.optimize(50, f, null, null);
    }

    /**
     * Test optimization with an unsupported goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedGoalType() {
        optimizer.optimize(50, f, GoalTypeWeightedSum.GOAL_TYPE_WEIGHTED_SUM, null);
    }

    /**
     * Test optimization with a null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        optimizer.optimize(50, null, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with a null simplic algorithm.
     */
    @Test(expected = NullPointerException.class)
    public void testNullSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(50, f, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with a custom simplex algorithm.
     */
    @Test
    public void testCustomSimplex() {
        MultivariateFunction f = new FibonacciSearchFunction(1);
        AbstractSimplex simplex = new SimplexPoint(new double[] { 0.0, 0.0 });
        optimizer.setSimplex(simplex);
        optimizer.optimize(50, f, GoalType.MINIMIZE, null);
        PointValuePair result = optimizer.getBestPoint();
        double expected = 1.0 / (1.0 + Math.sqrt(5.0));
        assertEquals(expected, result.getValue(), EPSILON);
    }

    /**
     * Rastrigin function.
     */
    private static class RastriginFunction implements MultivariateFunction {
        private final int dim;

        public RastriginFunction(int dim) {
            this.dim = dim;
        }

        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < dim; i++) {
                sum += Math.pow(x[i] * Math.pow(x[i], 2) - Math.cos(2.0 * Math.PI * x[i]), 2);
            }
            sum += dim * Math.PI * Math.sqrt(Math.pow(Math.PI, 2) * dim);
            return sum;
        }
    }

    /**
     * Rastrigin function with infinite values.
     */
    private static class RastriginFunctionWithInfiniteValues
        implements MultivariateFunction {
        private final int dim;

        public RastriginFunctionWithInfiniteValues(int dim) {
            this.dim = dim;
        }

        public double value(double[] x) {
            if (Math.abs(x[0]) > 10.0) {
                throw new IllegalArgumentException();
            }
            double sum = 0.0;
            for (int i = 0; i < dim; i++) {
                sum += Math.pow(x[i] * Math.pow(x[i], 2) - Math.cos(2.0 * Math.PI * x[i]), 2);
            }
            sum += dim * Math.PI * Math.sqrt(Math.pow(Math.PI, 2) * dim);
            return sum;
        }
    }

    /**
     * Function for testing with a custom simplex algorithm.
     */
    private static class FibonacciSearchFunction
        implements MultivariateFunction {
        private final int dim;

        public FibonacciSearchFunction(int dim) {
            this.dim = dim;
        }

        public double value(double[] x) {
            return Math.abs(Math.sin(Math.PI * x[0]));
        }
    }
}