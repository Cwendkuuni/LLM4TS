package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.MathOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.simple.SimpleXOptimizer;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** A simple multivariate function. */
    private static final MultivariateFunction FUNCTION = new MultivariateFunction() {
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }
    };

    /** A simplex optimizer instance. */
    private static final MathOptimizer OPTIMIZER = new SimplexOptimizer(new SimpleValueChecker());

    /** A simplex optimizer instance with custom convergence checker. */
    private static final MathOptimizer OPTIMIZER_CUSTOM = new SimplexOptimizer(
            1.0e-10, 1.0e-10, new MyConvergenceChecker());

    /** A simplex instance. */
    private static final AbstractSimplex SIMPLEX = new MySimplex();

    /**
     * Test that the optimizer returns a valid solution when optimizing a
     * quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        // Set the simplex.
        OPTIMIZER.setSimplex(SIMPLEX);

        // Optimize the function.
        PointValuePair solution = OPTIMIZER.optimize(100, FUNCTION, GoalType.MINIMIZE,
                new double[] { 1.0, 1.0 });

        // Check the solution.
        assertNotNull(solution);
        assertEquals(0.0, solution.getValue(), 1.0e-8);
        assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1.0e-8);
    }

    /**
     * Test that the optimizer returns a valid solution when optimizing a
     * quadratic function with a custom convergence checker.
     */
    @Test
    public void testQuadraticFunctionCustomChecker() {
        // Set the simplex.
        OPTIMIZER_CUSTOM.setSimplex(SIMPLEX);

        // Optimize the function.
        PointValuePair solution = OPTIMIZER_CUSTOM.optimize(100, FUNCTION, GoalType.MINIMIZE,
                new double[] { 1.0, 1.0 });

        // Check the solution.
        assertNotNull(solution);
        assertEquals(0.0, solution.getValue(), 1.0e-8);
        assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1.0e-8);
    }

    /**
     * Test that the optimizer throws an exception when the number of
     * evaluations exceeds the maximum number.
     */
    @Test
    public void testMaxEvalExceeded() {
        // Set the simplex.
        OPTIMIZER.setSimplex(SIMPLEX);

        // Optimize the function with too many evaluations.
        assertThrows(MaxEvalExceededException.class, () -> {
            OPTIMIZER.optimize(5, FUNCTION, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
        });
    }

    /**
     * Test that the optimizer returns a valid solution when optimizing a
     * multimodal function.
     */
    @Test
    public void testMultimodalFunction() {
        // Set the simplex.
        OPTIMIZER.setSimplex(SIMPLEX);

        // Optimize the function.
        PointValuePair solution = OPTIMIZER.optimize(100, new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(x[0]) * Math.sin(x[1]);
            }
        }, GoalType.MINIMIZE, new double[] { 0.0, 0.0 });

        // Check the solution.
        assertNotNull(solution);
        // The minimum value is not defined for this function
        // (it has many local minima), so we only check that the
        // solution is not null and its value is small enough.
        assertEquals(-1.0, solution.getValue(), 1.0e-5);
        assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1.0e-5);
    }

    /**
     * Test that the optimizer returns a valid solution when optimizing a
     * function with simple bounds.
     */
    @Test
    public void testFunctionWithSimpleBounds() {
        // Define a function with simple bounds.
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.exp(-x[0] * x[0] - x[1] * x[1]);
            }
        };

        // Set the simplex with bounds.
        AbstractSimplex simplex = new BoundedSimplex(2);
        simplex.setBounds(new double[][] { { -5.0, 5.0 }, { -5.0, 5.0 } });

        // Set the optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        // Optimize the function.
        PointValuePair solution = optimizer.optimize(100, function, GoalType.MINIMIZE, null);

        // Check the solution.
        assertNotNull(solution);
        // The minimum value is not defined for this function
        // (it has a global minimum but many local minima), so we only check that the
        // solution is not null and its value is small enough.
        assertEquals(-1.0, solution.getValue(), 1.0e-5);
        assertArrayEquals(new double[] { -2.0, -2.0 }, solution.getPoint(), 1.0e-5);
    }

    /**
     * Test that the optimizer throws an exception when an optimization data is
     * missing.
     */
    @Test(expected = NullArgumentException.class)
    public void testMissingOptimizationData() {
        // Set the optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Optimize the function without setting the simplex.
        optimizer.optimize(100, FUNCTION, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
    }

    /**
     * A simplex optimizer for testing purposes.
     */
    private static class MySimplex implements AbstractSimplex {
        @Override
        public void build(double[] point) {
            // Empty implementation.
        }

        @Override
        public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation.
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public double[] getPoint(int index) {
            return new double[] { 0.0 };
        }

        @Override
        public PointValuePair[] getPoints() {
            return new PointValuePair[0];
        }

        @Override
        public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Empty implementation.
        }
    }

    /**
     * A convergence checker for testing purposes.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair bestPrevious, PointValuePair bestCurrent) {
            // Always return false.
            return false;
        }
    }
}