package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultiPointValuePair;
import org.apache.commons.math3.optim.initializer.AverageStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.initializer.RandomStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.MaxEval;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * A simple test function.
     */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        public double value(double x) {
            return x * x;
        }
    };

    /**
     * A test function with multiple local minimums and a global minimum at 1.
     */
    private static final UnivariateFunction TEST_FUNCTION_MULTIPLE_MINIMUMS = new UnivariateFunction() {
        public double value(double x) {
            return Math.pow(x, 4) - 4 * Math.pow(x, 3) + 6 * Math.pow(x, 2) - 4 * x + 3;
        }
    };

    /**
     * A test function with a local minimum at -1 and a global minimum at 1.
     */
    private static final UnivariateFunction TEST_FUNCTION_MINUS_ONE = new UnivariateFunction() {
        public double value(double x) {
            return Math.pow(x + 1, 2) - Math.pow(x - 1, 2);
        }
    };

    /**
     * A test function with a local minimum at 0 and a global minimum at 1.
     */
    private static final UnivariateFunction TEST_FUNCTION_ZERO = new UnivariateFunction() {
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }
    };

    /**
     * Test with the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    /**
     * Test with a custom tolerance.
     */
    @Test
    public void testCustomTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-2);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test with a custom line search tolerance.
     */
    @Test
    public void testCustomLineSearchTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-2, 1e-6, 1e-3);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test with a custom goal type.
     */
    @Test
    public void testCustomGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-2);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AverageStartingPointInitializer(5),
                                                   GoalType.MAXIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), -1e-7);
    }

    /**
     * Test with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-2, new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    /**
     * Test with multiple local minimums.
     */
    @Test
    public void testMultipleMinimums() {
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_MULTIPLE_MINIMUMS, new RandomStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null, new LinearConstraint[] {new LinearConstraint(2, 1.0)},
                                                   new MaxEval(500));
        assertEquals(1.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    /**
     * Test with a local minimum at -1.
     */
    @Test
    public void testMinusOne() {
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_MINUS_ONE, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(-1.0, result.getPoint(), 1e-7);
        assertEquals(1.0, result.getValue(), 1e-7);
    }

    /**
     * Test with a local minimum at 0.
     */
    @Test
    public void testZero() {
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair result = optimizer.optimize(TEST_FUNCTION_ZERO, new AverageStartingPointInitializer(5),
                                                   GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(1.0, result.getValue(), 1e-7);
    }

    /**
     * Custom convergence checker for testing purposes.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous.getValue() > current.getValue();
        }
    }
}