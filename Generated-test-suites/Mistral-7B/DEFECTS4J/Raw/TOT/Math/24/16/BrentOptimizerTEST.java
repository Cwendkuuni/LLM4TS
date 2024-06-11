package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.convergence.SimpleValueChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private ConvergenceChecker<UnivariatePointValuePair> checker;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-3);
        checker = new SimpleValueChecker(1e-6, 1e-3);
    }

    @Test
    public void testMinimum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE, checker);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testMaximum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 0, 5, GoalType.MAXIMIZE, checker);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2, 0);
        assertEquals(expected, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1e-6, -1e-3);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        optimizer = new BrentOptimizer(1e-10, 1e-3);
    }

    @Test
    public void testConvergenceChecker() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-3, checker);

        UnivariatePointValuePair result = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE, checker);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(Math.PI, 0);
        assertEquals(expected, result);
        assertTrue(checker.converged(result.getIterationNumber(), null, result));
    }

    @Test
    public void testNoConvergence() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-3, checker);

        UnivariatePointValuePair result = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE, checker);
        assertFalse(checker.converged(result.getIterationNumber(), null, result));
    }
}