package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PowellOptimizerTest {
    private MultivariateFunction testFunction;
    private MultivariateOptimizer optimizer;

    @Before
    public void setup() {
        testFunction = new TestFunction();
        optimizer = new PowellOptimizer(1e-5, 1e-5);
    }

    @Test
    public void testMinimize() {
        double[] startPoint = new double[]{3.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE);
        Assert.assertEquals(1.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testMaximize() {
        double[] startPoint = new double[]{-3.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint, GoalType.MAXIMIZE);
        Assert.assertEquals(-1.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{-1.0}, result.getPoint(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsThreshold() {
        optimizer = new PowellOptimizer(-1.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelThreshold() {
        optimizer = new PowellOptimizer(0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer = new PowellOptimizer(1e-5, 1e-5);
        optimizer.optimize(testFunction, new double[]{}, GoalType.NEUTRAL);
    }

    @Test
    public void testCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, p1, p2) -> true;
        optimizer = new PowellOptimizer(1e-5, 1e-5, customChecker);

        double[] startPoint = new double[]{3.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE);
        Assert.assertEquals(1.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0}, result.getPoint(), 1e-5);
    }

    @Test(expected = org.apache.commons.math3.optimization.OptimizationError.class)
    public void testFunctionThrowsException() {
        MultivariateFunction exceptionFunction = new TestFunctionWithException();
        optimizer.optimize(exceptionFunction, new double[]{0.0}, GoalType.MINIMIZE);
    }

    static class TestFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return x[0] * x[0] - 2 * x[0] + 3;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    static class TestFunctionWithException implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            throw new RuntimeException("Function throws exception");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}