package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateValueChecker;
import org.apache.commons.math3.test.TestCase;
import org.junit.Test;

/**
 * Test cases for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends TestCase {
    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
        assertNotNull(optimizer.getLineSearch());
        assertNull(optimizer.getGoalType());
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceCheckerConstructor() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertNotNull(optimizer.getLineSearch());
        assertNull(optimizer.getGoalType());
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceCheckerOptimization() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
        assertEquals(0.0, result.getValue(), 0.0);
        assertArrayEquals(new double[]{3.0, 4.0}, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with default convergence checker.
     */
    @Test
    public void testDefaultConvergenceCheckerOptimization() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        try {
            PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
            fail("Expected MaxEvaluationsExceededException");
        } catch (MaxEvaluationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test optimization with custom convergence checker and custom goal type.
     */
    @Test
    public void testCustomConvergenceCheckerOptimizationWithGoalType() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker, goalType);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
        assertEquals(0.0, result.getValue(), 0.0);
        assertArrayEquals(new double[]{3.0, 4.0}, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with default convergence checker and custom goal type.
     */
    @Test(expected = RuntimeException.class)
    public void testDefaultConvergenceCheckerOptimizationWithGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, GoalType.MAXIMIZE);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        try {
            PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    /**
     * Test optimization with custom convergence checker, custom goal type,
     * and custom line search tolerance.
     */
    @Test
    public void testCustomConvergenceCheckerOptimizationWithGoalTypeAndLineSearchTolerance() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001, checker, goalType);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
        assertEquals(0.0, result.getValue(), 0.0);
        assertArrayEquals(new double[]{3.0, 4.0}, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with default convergence checker, custom goal type,
     * and custom line search tolerance.
     */
    @Test(expected = RuntimeException.class)
    public void testDefaultConvergenceCheckerOptimizationWithGoalTypeAndLineSearchTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, GoalType.MAXIMIZE, 0.0001);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 3.0, 2.0) + Math.pow(point[1] - 4.0, 2.0);
            }
        };
        try {
            PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}