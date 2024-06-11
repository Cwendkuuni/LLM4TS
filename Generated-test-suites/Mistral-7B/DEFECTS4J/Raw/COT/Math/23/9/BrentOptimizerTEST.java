package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.simple.AbstractUnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketedUnivariateFunction;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}.
     */
    @Test
    public void testOptimize() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(-2.0, result.getPoint(), 0);
        assertEquals(-4.0, result.getValue(), 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(BracketedUnivariateFunction)}.
     */
    @Test
    public void testOptimizeBracketedFunction() {
        BracketedUnivariateFunction function = new BracketedUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double bracketMin(double lo, double hi) {
                return super.bracketMin(lo, hi) - 0.1;
            }

            @Override
            public double bracketMax(double lo, double hi) {
                return super.bracketMax(lo, hi) + 0.1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(-1.2732055648483989d, result.getPoint(), 0.0000001);
        assertEquals(-0.500000000214522d, result.getValue(), 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType)}.
     */
    @Test
    public void testOptimizeGoalType() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0) - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(2.0, result.getPoint(), 0);
        assertEquals(4.0, result.getValue(), 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidAbsThreshold() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(-1e-5, 1e-5, null);
        optimizer.optimize(function);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidRelThreshold() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-5, null);
        optimizer.optimize(function);
    }
}