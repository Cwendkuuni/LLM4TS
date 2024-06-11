package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeFunctionWithBounds() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1});
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        double expectedResult = -2.0;
        assertEquals(expectedResult, result.getPoint(), 1e-8);
        assertEquals(expectedResult, result.getValue(), 1e-8);
    }

    @Test
    public void testOptimizeFunctionWithoutBounds() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1});
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType);

        double expectedResult = -2.0;
        assertEquals(expectedResult, result.getPoint(), 1e-8);
        assertEquals(expectedResult, result.getValue(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizerWithNegativeTolerance() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1});
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;
        double tolerance = -0.001;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(tolerance);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizerWithNegativeMaxIterations() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1});
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;
        int maxIterations = -1;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(maxIterations);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizerWithMaxIterationsExceeded() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1});
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;
        int maxIterations = 1;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(maxIterations);
        optimizer.optimize(f, goalType, min, max, startValue);
    }
}