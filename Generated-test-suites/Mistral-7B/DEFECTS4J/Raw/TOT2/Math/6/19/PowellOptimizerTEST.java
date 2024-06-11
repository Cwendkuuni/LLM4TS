package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.Points2D;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testSimpleMinimization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(function, goalType, startPoint);
        double[] expected = {1.0, 1.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testMinimizationWithConstraints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
                LinearConstraint.LEQ(1.0, 0.5 * point[0] + 0.5 * point[1]),
                LinearConstraint.LEQ(0.5 * point[0] + 0.5 * point[1], 3.5)
        });
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(1.0, 1.0)
        });
        Points2D initialPoint = new Points2D(startPoint);
        PointValuePair result = optimizer.optimize(function, objectiveFunctionSet, goalType, initialPoint, constraints);
        double[] expected = {1.0, 1.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, -1.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        optimizer.optimize(function, goalType, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = null;
        optimizer.optimize(function, goalType, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = null;
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        optimizer.optimize(function, goalType, startPoint);
    }

    @Test(expected = IllegalStateException.class)
    public void testNonConvergence() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0) - Math.log(Math.abs(point[0] - 1.0) + Math.abs(point[1] - 2.0) + 1);
            }
        };
        optimizer.optimize(function, goalType, startPoint);
    }
}