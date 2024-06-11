package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointVectorValue;
import org.apache.commons.math3.optim.point.PointValue;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.initializer.RandomInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.LinConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FallingStepSizeGradientDescent;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteThresholds() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0);
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteThresholdsAndChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 1.0, new MyConvergenceChecker());
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsThresholds() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdBelowMinimum() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 1.0);
    }

    @Test
    public void testOptimizeWithUnivariateFunction() {
        double[] startPoint = new double[]{-1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyUnivariateFunction();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.5, result.getPoint()[0], 0.0001);
        Assert.assertEquals(-2.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithLinearObjectiveFunction() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        LinearObjectiveFunction function = new MyLinearObjectiveFunction();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithConstraint() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyObjectiveFunctionWithConstraint();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(new double[]{1.5, 0.0}, result.getPoint(), 0.0001);
        Assert.assertEquals(-2.25, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyObjectiveFunction();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, null, new RandomInitialGuess(2));
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new double[]{-1.5, -0.5}, result.getPoint(), 0.0001);
        Assert.assertEquals(-2.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithFallingStepSizeGradientDescent() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyObjectiveFunction();
        NonlinearOptimizer initialOptimizer = new FallingStepSizeGradientDescent(function, 0.1);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint, initialOptimizer);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.5, result.getPoint()[0], 0.0001);
        Assert.assertEquals(-2.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001, new MyCustomConvergenceChecker());
        ObjectiveFunction function = new MyObjectiveFunction();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.5, result.getPoint()[0], 0.0001);
        Assert.assertEquals(-2.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithInvalidGoalType() {
        double[] startPoint = new double[]{1.0, 1.0};
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyObjectiveFunction();
        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, startPoint);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeWithNullObjectiveFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, new double[]{0.0});
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeWithNullStartingPoint() {
        PowellOptimizer optimizer = new PowellOptimizer();
        ObjectiveFunction function = new MyObjectiveFunction();
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    private static class MyUnivariateFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }
    }

    private static class MyLinearObjectiveFunction implements LinearObjectiveFunction {
        public double value(LinearPointValuePair linearPointValuePair) {
            return linearPointValuePair.getPoint()[0] * linearPointValuePair.getPoint()[0] + 2 * linearPointValuePair.getPoint()[1];
        }
    }

    private static class MyObjectiveFunction implements ObjectiveFunction {
        public double value(PointVectorValue pointVectorValue) {
            return Math.pow(pointVectorValue.getPoint()[0], 2) + Math.pow(pointVectorValue.getPoint()[1], 2) - 2 * pointVectorValue.getPoint()[0] - 2 * pointVectorValue.getPoint()[1] + 1;
        }

        public void gradient(PointVectorValue pointVectorValue, double[] grad) {
            double[] point = pointVectorValue.getPoint();
            grad[0] = 2 * point[0] - 2;
            grad[1] = 2 * point[1] - 2;
        }
    }

    private static class MyObjectiveFunctionWithConstraint implements ObjectiveFunction {
        @Override
        public double value(PointVectorValue pointVectorValue) {
            double[] point = pointVectorValue.getPoint();
            return Math.pow(point[0], 2) + Math.pow(point[1], 2) - 2 * point[0] - 2 * point[1] + 1;
        }

        @Override
        public void gradient(PointVectorValue pointVectorValue, double[] grad) {
            double[] point = pointVectorValue.getPoint();
            grad[0] = 2 * point[0] - 2;
            grad[1] = 2 * point[1] - 2;
        }

        @Override
        public LinearConstraint[] getConstraints() {
            return new LinearConstraint[]{new LinConstraintSet(new SimpleBounds(0.0, Double.POSITIVE_INFINITY), new SimpleBounds(-Double.POSITIVE_INFINITY, 0.0))};
        }
    }

    private static class MyCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double[] previousPoint = previous.getPoint();
            double[] currentPoint = current.getPoint();
            double relativeDifference = Arrays.stream(previousPoint).map(d -> Math.abs(d - currentPoint[Arrays.indexOf(previousPoint, d)])).max().orElse(0.0);
            return relativeDifference < 0.001;
        }
    }
}