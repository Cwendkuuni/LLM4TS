package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.special.Constants;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    /**
     * Test function: f(x) = x^3 - 2*x^2 + x - 1
     */
    @Test
    public void testBrentOptimizerExample() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 4 * x + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(-2.15443236285234e-13, function.value(result.getPoint()), 1e-6);
    }

    /**
     * Test function: f(x) = Math.exp(x) - x - 1
     */
    @Test
    public void testBrentOptimizerExpFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - x - 1;
            }

            @Override
            public double derivative(double x) {
                return Math.exp(x) + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(Constants.Euler, result.getPoint(), 1e-4);
        assertEquals(-1.71267452823211e-8, function.value(result.getPoint()), 1e-6);
    }

    /**
     * Test function with no real roots: f(x) = x^2 - 2
     */
    @Test(expected = org.apache.commons.math3.exception.NoSolutionException.class)
    public void testBrentOptimizerNoRealRoots() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, GoalType.MINIMIZE);
    }

    /**
     * Test function with no local minimum: f(x) = |x|
     */
    @Test
    public void testBrentOptimizerNoLocalMinimum() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }

            @Override
            public double derivative(double x) {
                return Math.signum(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);

        assertNull(result);
    }
}