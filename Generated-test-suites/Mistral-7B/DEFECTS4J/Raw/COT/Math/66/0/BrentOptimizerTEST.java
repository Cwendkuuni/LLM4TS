package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeFast;
import org.junit.Test;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        assertEquals(GoalTypeFast.MINIMIZE, goalType);
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testOptimizeMaximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        assertEquals(GoalTypeFast.MAXIMIZE, goalType);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeNullFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testOptimizeBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        double startValue = 3.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        assertEquals(GoalTypeFast.MINIMIZE, goalType);
        assertEquals(-3.0, result.getPoint(), 1e-6);
        assertEquals(9.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 3.0;
        double max = 2.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 4.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1e-6);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(-1);
        optimizer.optimize(f, goalType, min, max, startValue);
    }
}