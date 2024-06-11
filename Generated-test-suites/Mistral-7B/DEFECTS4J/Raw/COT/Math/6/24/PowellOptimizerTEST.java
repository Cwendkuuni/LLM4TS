package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeThreshold;
import org.apache.commons.math3.optim.nonlinear.scalar.goal.AbstractGoalTypeThreshold;
import org.apache.commons.math3.optim.nonlinear.scalars.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalars.GoalTypeMaximize;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.*;

public class PowellOptimizerTest {

    private PowellOptimizer powellOptimizer;
    private UnivariateFunction testFunction;
    private double[] startPoint;
    private double tolerance;
    private int maxIterations;

    @Before
    public void setup() {
        double relTolerance = 1e-6;
        double absTolerance = 1e-7;
        tolerance = Math.min(relTolerance, absTolerance);
        maxIterations = 100;
        powellOptimizer = new PowellOptimizer(relTolerance, absTolerance);
        testFunction = mock(UnivariateFunction.class);
        when(testFunction.value(Double.class, anyDouble()))
                .thenReturn(0.0);
    }

    @Test
    public void testConstructorWithDefaultValues() {
        PowellOptimizer powellOptimizer = new PowellOptimizer();
        Assert.assertNotNull(powellOptimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsTolerance() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelTolerance() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(-1.0, 1.0);
    }

    @Test
    public void testDoOptimizeWithDefaultGoalType() {
        startPoint = new double[]{1.0, 1.0};
        powellOptimizer.optimize(testFunction, startPoint);
        verify(testFunction, atLeastOnce()).value(anyDouble());
    }

    @Test
    public void testDoOptimizeWithMinimizeGoalType() {
        startPoint = new double[]{1.0, 1.0};
        powellOptimizer = new PowellOptimizer(new GoalTypeMinimize());
        powellOptimizer.optimize(testFunction, startPoint);
        verify(testFunction, atLeastOnce()).value(anyDouble());
    }

    @Test
    public void testDoOptimizeWithMaximizeGoalType() {
        startPoint = new double[]{1.0, 1.0};
        powellOptimizer = new PowellOptimizer(new GoalTypeMaximize());
        powellOptimizer.optimize(testFunction.negate(), startPoint);
        verify(testFunction, atLeastOnce()).value(anyDouble());
    }

    @Test
    public void testDoOptimizeWithCustomGoalType() {
        startPoint = new double[]{1.0, 1.0};
        double customThreshold = 0.1;
        powellOptimizer = new PowellOptimizer(new AbstractGoalTypeThreshold(customThreshold, GoalType.MINIMIZE));
        powellOptimizer.optimize(testFunction, startPoint);
        verify(testFunction, atLeastOnce()).value(anyDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullFunction() {
        powellOptimizer.optimize(null, startPoint);
    }

    @Test
    public void testDoOptimizeWithNullStartPoint() {
        powellOptimizer.optimize(testFunction, null);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        startPoint = new double[]{1.0, 1.0};
        double[] expectedResult = new double[]{0.0, 0.0};
        powellOptimizer = new PowellOptimizer(tolerance, tolerance, new MyConvergenceChecker());
        powellOptimizer.optimize(testFunction, startPoint);
        PointValuePair result = powellOptimizer.getLastIterate();
        Assert.assertArrayEquals(expectedResult, result.getPoint(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullLastIterate() {
        powellOptimizer.setLastIterate(null);
        powellOptimizer.optimize(testFunction, startPoint);
    }

    @Test
    public void testLargestStepSize() {
        startPoint = new double[]{1.0, 1.0};
        powellOptimizer = new PowellOptimizer(1.0, 1.0, 1.0, 1.0, null);
        powellOptimizer.optimize(testFunction, startPoint);
        verify(testFunction, atLeastOnce()).value(anyDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidLineSearchTolerances() {
        powellOptimizer = new PowellOptimizer(-1.0, -1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullConvergenceChecker() {
        powellOptimizer = new PowellOptimizer(tolerance, tolerance, null);
    }

    @Test
    public void testNumberOfEvaluations() {
        int expectedNumberOfEvaluations = maxIterations + 2;
        powellOptimizer = new PowellOptimizer(tolerance, tolerance);
        powellOptimizer.optimize(testFunction, startPoint);
        verify(testFunction, atLeast(expectedNumberOfEvaluations)).value(anyDouble());
    }

    @Test
    public void testDoOptimizeWithEmptyFunction() {
        powellOptimizer = new PowellOptimizer();
        powellOptimizer.optimize(new UnivariateFunctionFactory() {
            @Override
            public UnivariateFunction createFunction(GoalType goalType) {
                return new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return 0;
                    }
                };
            }
        }, new double[0]);
        verify(testFunction, never()).value(anyDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullFunctionFactory() {
        powellOptimizer = new PowellOptimizer();
        powellOptimizer.optimize(null, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithEmptyStartPoint() {
        powellOptimizer = new PowellOptimizer();
        powellOptimizer.optimize(testFunction, new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullStartPointArray() {
        powellOptimizer = new PowellOptimizer();
        powellOptimizer.optimize(testFunction, null);
    }

    private static class MyConvergenceChecker extends SimpleUnivariateValueChecker {
        @Override
        public boolean converged(int iteration, PointValuePair currentValue, PointValuePair previousValue) {
            double tolerance = 1e-3;
            return Math.abs(previousValue.getValue() - currentValue.getValue()) < tolerance;
        }
    }
}