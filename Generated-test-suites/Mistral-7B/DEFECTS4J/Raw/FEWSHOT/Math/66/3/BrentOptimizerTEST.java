package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testInvalidParameters() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(-1.0));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setRelativeAccuracy(-1.0));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaximalIterationCount(-1));
        assertThrows(IllegalArgumentException.class, () -> optimizer.setMaxEvaluations(-1));
    }

    @Test
    public void testOptimizeFunctionMin() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1);
        double min = -1.0;
        double startValue = 0.0;
        double expectedResult = -1.2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, min + 1, startValue);
        assertEquals(expectedResult, result, 0.0001);
    }

    @Test
    public void testOptimizeFunctionMax() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, 1);
        double min = -1.0;
        double max = 1.0;
        double expectedResult = 1.2;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max);
        assertEquals(expectedResult, result, 0.0001);
    }

    @Test
    public void testOutsideBounds() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1);
        double min = 1.0;
        double max = -1.0;
        double startValue = 0.0;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testInitialGuess() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1);
        double min = -1.0;
        double max = 1.0;
        double startValue = 2.0;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        InitialGuess initialGuess = new SimpleValueChecker(new double[]{1.0, 2.0}, 1.0);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);
        // The result depends on the initial guess
    }
}