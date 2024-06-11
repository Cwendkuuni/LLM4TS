package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.Log;
import org.apache.commons.math3.special.MathConstants;
import org.apache.commons.math3.special.Rational;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with default {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerDefault() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2 * Math.exp(Rational.ONE.doubleValue() / 3 * x)
                       + Rational.ONE.doubleValue() / 3 * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateOptimizer result = optimizer.optimize(f, -5, 5);
        PointValuePair expected = new PointValuePair(MathConstants.E, f.value(MathConstants.E));
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerCustomChecker() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x) + 1) - E.E;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6,
                (iter, previous, current) -> current.getValue() < previous.getValue());
        UnivariateOptimizer result = optimizer.optimize(f, 1, 10);
        PointValuePair expected = new PointValuePair(Log.E, -E.E);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with negative {@code relativeThreshold}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        new BrentOptimizer(-0.01, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with zero {@code absoluteThreshold}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerZeroAbsoluteThreshold() {
        new BrentOptimizer(1e-6, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with null {@code ConvergenceChecker}.
     */
    @Test(expected = NullPointerException.class)
    public void testBrentOptimizerNullChecker() {
        new BrentOptimizer(1e-6, 1e-6, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with invalid {code goalType}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidGoalType() {
        new BrentOptimizer(1e-6, 1e-6, null, GoalType.MAXIMIZE);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} with
     * {@code lo > hi}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setMin(5d);
        optimizer.setMax(3d);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        });
    }
}