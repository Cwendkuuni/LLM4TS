package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.UnivariatePointValuePair;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 *
 * @author <a href="mailto:alexandre.bosc@exoplatform.com">Alexandre Bosc</a>
 */
public class BrentOptimizerTest {

    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.sin(x)) - 2 * Math.cos(x) + 1;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x) * Math.exp(Math.sin(x)) + 2 * Math.sin(x);
            }
        };
    }

    @Test
    public void testConstructorWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(
                function, 1e-6, 1e-6, GoalType.MINIMIZE);
        assertFalse(optimizer.isMaximizing());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsThreshold() {
        new BrentOptimizer(0.5, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdLessThanMachineEpsilon() {
        new BrentOptimizer(1e-12, 1.0, null);
    }

    @Test
    public void testFindMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(function, 1e-6, 1e-6, GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        assertTrue(result instanceof UnivariatePointValuePair);
        UnivariatePointValuePair pointValuePair = (UnivariatePointValuePair) result;
        assertTrue(Math.abs(pointValuePair.getPoint() - Math.PI) < 1e-8);
        assertTrue(Math.abs(function.value(pointValuePair.getPoint()) - E.EULER_NUMBER.doubleValue()) < 1e-8);
    }

    @Test
    public void testFindMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(
                function, 1e-6, 1e-6, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize();
        assertTrue(result instanceof UnivariatePointValuePair);
        UnivariatePointValuePair pointValuePair = (UnivariatePointValuePair) result;
        assertTrue(Math.abs(pointValuePair.getPoint() - Math.PI) < 1e-8);
        assertTrue(Math.abs(function.value(pointValuePair.getPoint()) - E.EULER_NUMBER.doubleValue()) < 1e-8);
    }

    @Test(expected = ConvergenceException.class)
    public void testFindMinimumWithNoConvergenceChecker() {
        new BrentOptimizer(function, 1e-16, 1e-16, GoalType.MINIMIZE).optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testFindMinimumWithConvergenceCheckerThatAlwaysFail() {
        BrentOptimizer optimizer = new BrentOptimizer(
                function, 1e-6, 1e-6, GoalType.MINIMIZE,
                new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean converged(int iteration,
                                              PointValuePair previous,
                                              PointValuePair current) {
                        return false;
                    }
                });
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testFindMinimumWithConvergenceCheckerThatAlwaysSucceed() {
        BrentOptimizer optimizer = new BrentOptimizer(
                function, 1e-6, 1e-6, GoalType.MINIMIZE,
                new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean converged(int iteration,
                                              PointValuePair previous,
                                              PointValuePair current) {
                        return true;
                    }
                });
        optimizer.optimize();
    }

    @Test
    public void testFindMinimumWithCustomConvergenceChecker() {
        int maxIterations = 100;
        double tolerance = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(
                function, tolerance, tolerance, GoalType.MINIMIZE,
                new ConvergenceChecker<PointValuePair>() {
                    private int counter = 0;

                    @Override
                    public boolean converged(int iteration,
                                              PointValuePair previous,
                                              PointValuePair current) {
                        if (counter++ >= maxIterations) {
                            return true;
                        }
                        return Math.abs(previous.getValue() - current.getValue()) < tolerance;
                    }
                });

        PointValuePair result = optimizer.optimize();
        assertTrue(result instanceof UnivariatePointValuePair);
        UnivariatePointValuePair pointValuePair = (UnivariatePointValuePair) result;
        assertTrue(Math.abs(pointValuePair.getPoint() - Math.PI) < 1e-8);
        assertTrue(Math.abs(function.value(pointValuePair.getPoint()) - E.EULER_NUMBER.doubleValue()) < 1e-8);
    }

    @Test
    public void testFindMinimumWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(
                function, 1e-6, 1e-6, GoalType.MINIMIZE, 2.0);
        PointValuePair result = optimizer.optimize();
        assertTrue(result instanceof UnivariatePointValuePair);
        UnivariatePointValuePair pointValuePair = (UnivariatePointValuePair) result;
        assertTrue(Math.abs(pointValuePair.getPoint() - Math.PI) < 1e-8);
        assertTrue(Math.abs(function.value(pointValuePair.getPoint()) - E.EULER_NUMBER.doubleValue()) < 1e-8);
    }
}