package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Test cases for the BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer()}.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * This method should not be called directly, so it's not tested here.
     */

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeMinimum() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        double min = 0;
        double max = 2;
        double startValue = 1;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(min, optimum, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMaximum() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        double min = 0;
        double max = 2;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        double optimum = optimizer.optimize(function, GoalType.MAXIMIZE, min, max);

        assertEquals(max, optimum, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test the case when the minimum is at the boundary of the interval.
     */
    @Test
    public void testOptimizeMinimumBoundary() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x >= 0) {
                    return Math.pow(x, 2);
                } else {
                    return Math.abs(x) * Math.log(Math.abs(x));
                }
            }
        };

        double min = 0;
        double max = 1;
        double startValue = -1;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        double optimum = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(min, optimum, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test the case when the minimum is not inside the interval.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeMinimumOutsideInterval() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        double min = 3;
        double max = 2;
        double startValue = 1;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test the case when the function is not defined in the interval.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionNotDefined() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sqrt(x);
                } else {
                    throw new UnsupportedOperationException("Function not defined for x <= 0");
                }
            }
        };

        double min = 0;
        double max = 1;
        double startValue = -1;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test the case when the maximum number of iterations is exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException, NotStrictlyPositiveException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double min = 0;
        double max = 2;
        double startValue = 1;

        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
    }
}