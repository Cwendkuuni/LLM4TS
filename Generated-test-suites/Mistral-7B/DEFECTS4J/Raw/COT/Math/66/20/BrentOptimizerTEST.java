package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValueWithMessage;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class
 */
public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeWithSimpleFunction() {
        UnivariateFunction function = new FunctionWithMinimum(1.0);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        Assert.assertEquals(1.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithBoundaryConditions() {
        UnivariateFunction function = new FunctionWithMinimum(0.0);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomFunctionAndMessage() {
        UnivariateFunction function = new CustomFunctionWithMinimum(0.0, "Minimum reached.");
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertEquals("Minimum reached.", optimizer.getResultMessage());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeAbsoluteAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0);
        UnivariateFunction function = new FunctionWithMinimum(0.0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeRelativeAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-1.0);
        UnivariateFunction function = new FunctionWithMinimum(0.0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationCountExceeded() {
        UnivariateFunction function = new FunctionWithMinimum(1.0);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    private static class FunctionWithMinimum implements UnivariateFunction {

        private final double minValue;

        public FunctionWithMinimum(double minValue) {
            this.minValue = minValue;
        }

        @Override
        public double value(double x) {
            return Math.pow(x - minValue, 2);
        }
    }

    private static class CustomFunctionWithMinimum implements ObjectiveFunction {

        private final double minValue;
        private final String message;

        public CustomFunctionWithMinimum(double minValue, String message) {
            this.minValue = minValue;
            this.message = message;
        }

        @Override
        public double value(double x) {
            return Math.pow(x - minValue, 2);
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] hessian(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] hessian(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double getMinValue(GoalTypeConstraint constraint, double... initialGuess) throws MaxIterationsExceededException {
            return minValue;
        }

        @Override
        public PointValuePair minimize(GoalTypeValue goal, InitialGuess initialGuess, MaxEval maxEval) throws MaxIterationsExceededException {
            UnivariateOptimizer optimizer = new BrentOptimizer();
            PointValuePair result = optimizer.optimize(this, goal, 0.0, 10.0, initialGuess.getPoint());
            return result;
        }

        @Override
        public PointValuePair minimize(GoalTypeValueWithMessage goal, InitialGuess initialGuess, MaxEval maxEval) throws MaxIterationsExceededException {
            UnivariateOptimizer optimizer = new BrentOptimizer();
            PointValuePair result = optimizer.optimize(this, goal, 0.0, 10.0, initialGuess.getPoint());
            optimizer.setResultMessage(message);
            return result;
        }
    }
}