package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test default values
        assertEquals(Double.MAX_VALUE, optimizer.getMaxEvaluations(), 0);
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);

        // Test setting values
        optimizer.setMaxEvaluations(50);
        optimizer.setMaximalIterationCount(50);
        optimizer.setAbsoluteAccuracy(1E-8);
        optimizer.setRelativeAccuracy(1.0e-7);

        assertEquals(50, optimizer.getMaxEvaluations(), 0);
        assertEquals(50, optimizer.getMaximalIterationCount());
        assertEquals(1E-8, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-7, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testOptimizeSimpleFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 2.5;

        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        assertEquals(Math.pow(2.5, 2), result.getValue(), 0.001);
        assertEquals(2.5, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeComplexFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        double min = -5;
        double max = 5;

        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, 0);

        // The minimum of this function is approximately -3.1313
        assertEquals(-3.1313, result.getValue(), 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 2.5;

        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(function, goalType, min, max, startValue);
    }
}