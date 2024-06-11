package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private ConvergenceChecker<UnivariatePointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return iteration > 10;
            }
        };
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-15, 1e-8);
    }

    @Test
    public void testOptimizeWithSimpleFunction() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 1);
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeWithFunctionThatHasMinimumOutsideInterval() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        optimizer.optimize(function, 1, 3);
        assertNull(optimizer.getOptimizedPoint());
    }

    @Test
    public void testOptimizeWithFunctionThatHasMinimumInsideInterval() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 2);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeWithFunctionThatHasMultipleRoots() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2) * (x - 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 3);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(-2.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeWithConvergenceChecker() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 2);
        assertNotNull(result);
        assertTrue(convergenceChecker.converged(optimizer.getIterationCount(), null, result));
    }

    @Test
    public void testOptimizeWithGoalTypeMinimize() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 2);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(-2.0, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeWithGoalTypeMaximize() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 1, 2);
            }
        };
        optimizer = new BrentOptimizer(1e-5, 1e-8);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 2);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(2.0, result.getValue(), 1e-8);
    }

}