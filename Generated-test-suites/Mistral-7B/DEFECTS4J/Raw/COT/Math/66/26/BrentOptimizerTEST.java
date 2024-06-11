package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructors() {
        // No arguments constructor
        BrentOptimizer optimizer = new BrentOptimizer();
        // Check that default values are set correctly
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);

        // Constructor with custom settings
        BrentOptimizer optimizerWithCustomSettings = new BrentOptimizer();
        optimizerWithCustomSettings.setMaxEvaluations(5);
        optimizerWithCustomSettings.setMaximalIterationCount(50);
        optimizerWithCustomSettings.setAbsoluteAccuracy(1E-5);
        optimizerWithCustomSettings.setRelativeAccuracy(2.0e-5);
        assertEquals(5, optimizerWithCustomSettings.getMaxEvaluations());
        assertEquals(50, optimizerWithCustomSettings.getMaximalIterationCount());
        assertEquals(1E-5, optimizerWithCustomSettings.getAbsoluteAccuracy(), 0);
        assertEquals(2.0e-5, optimizerWithCustomSettings.getRelativeAccuracy(), 0);
    }

    @Test
    public void testDoOptimizeMethodShouldThrowUnsupportedOperationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(UnsupportedOperationException.class, optimizer::doOptimize);
    }

    @Test
    public void testOptimizeMethodWithMinimizationGoalTypeAndValidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMinimize = f -> Math.pow(Math.sin(f), 2);
        double min = -1.5;
        double max = 1.5;
        double startValue = 0.0;

        PointValuePair result = optimizer.optimize(functionToMinimize, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(Math.PI / 2, result.getPoint(), 0);
        assertEquals(0.25, result.getValue(), 0);
    }

    @Test
    public void testOptimizeMethodWithMaximizationGoalTypeAndValidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMaximize = f -> Math.exp(-Math.pow(f, 2));
        double min = -1.5;
        double max = 1.5;
        double startValue = 0.0;

        PointValuePair result = optimizer.optimize(functionToMaximize, GoalType.MAXIMIZE, min, max, startValue);
        assertEquals(0.0, result.getPoint(), 0);
        assertEquals(Math.E, result.getValue(), 0);
    }

    @Test
    public void testOptimizeMethodWithMinimizationGoalTypeAndInvalidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMinimize = f -> Math.pow(Math.sin(f), 2);
        double min = -1.5;
        double max = 1.5;
        double startValue = 1.5; // startValue > max

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(functionToMinimize, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testOptimizeMethodWithMaximizationGoalTypeAndInvalidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMaximize = f -> Math.exp(-Math.pow(f, 2));
        double min = -1.5;
        double max = 1.5;
        double startValue = -1.5; // startValue < min

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(functionToMaximize, GoalType.MAXIMIZE, min, max, startValue));
    }

    @Test
    public void testOptimizeMethodWithFunctionThrowsException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMinimize = f -> {
            throw new FunctionEvaluationException("Test Error");
        };
        double min = -1.5;
        double max = 1.5;
        double startValue = 0.0;

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(functionToMinimize, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testLocalMinMethodWithMinimizationGoalTypeAndValidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMinimize = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 3);
        boolean isMinim = true;
        double lo = -5.0;
        double mid = 0.0;
        double hi = 5.0;
        double eps = 1.0e-10;
        double t = 1.0e-10;

        double result = optimizer.localMin(isMinim, functionToMinimize, null, lo, mid, hi, eps, t);
        assertEquals(-1.0, result, 0);
    }

    @Test
    public void testLocalMinMethodWithMaximizationGoalTypeAndValidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMaximize = new PolynomialFunction(new double[]{1.0, 1.0, 1.0}, 3);
        boolean isMinim = false;
        double lo = -5.0;
        double mid = 0.0;
        double hi = 5.0;
        double eps = 1.0e-10;
        double t = 1.0e-10;

        double result = optimizer.localMin(isMinim, functionToMaximize, null, lo, mid, hi, eps, t);
        assertEquals(1.0, result, 0);
    }

    @Test
    public void testLocalMinMethodWithInvalidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction functionToMinimize = new PolynomialFunction(new double[]{1.0, 1.0, 1.0}, 3);
        boolean isMinim = true;
        double lo = -5.0;
        double mid = 5.0;
        double hi = 5.0;
        double eps = -1.0e-10;
        double t = 1.0e-10;

        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.localMin(isMinim, functionToMinimize, null, lo, mid, hi, eps, t));
    }
}