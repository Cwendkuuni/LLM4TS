package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinExample() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        PointValuePair solution = optimizer.optimize(f, -2.0, 2.0);

        assertEquals(-2.0, solution.getPoint(), 0.001);
        assertEquals(-4.0, solution.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerAbsoluteThresholdNegative() {
        new BrentOptimizer(-0.001, 0.001, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        new BrentOptimizer(0.0, 0.001, null);
    }

    @Test
    public void testBrentOptimizerNoConvergenceChecker() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        PointValuePair solution = optimizer.optimize(f, -10.0, 10.0);

        // The function does not have a finite minimum or maximum in [-10, 10]
        assertNull(solution);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = (iter, previous, current) -> {
            double previousValue = previous.getValue();
            double currentValue = current.getValue();
            return Math.abs(currentValue - previousValue) < 0.001;
        };

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) * Math.exp(-Math.pow(x, 2));
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001, convergenceChecker);
        PointValuePair solution = optimizer.optimize(f, -10.0, 10.0);

        // The function has a minimum at x = 1.1071467859243132
        assertEquals(1.1071467859243132, solution.getPoint(), 0.001);
        assertEquals(-0.7361582373785007, solution.getValue(), 0.001);
    }
}