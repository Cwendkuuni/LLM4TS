package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeDetector;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionWithConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LUSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BracketFinder;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Testing {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test with a simple objective function.
     */
    @Test
    public void testSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);

        UnivariateFunction f = x -> Math.pow(x, 2);
        double[] startPoint = new double[] {5.0};
        PointValuePair result = optimizer.optimize(f, startPoint);
        Assertions.assertEquals(25.0, result.getValue(), 1e-8);
        Assertions.assertEquals(2.0, result.getPoint(), 1e-8);
    }

    /**
     * Test with a complex objective function with constraints.
     */
    @Test
    public void testComplexFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);

        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(
                new double[][] {{-1.0, 3.0, 1.0}, {-3.0, 5.0, -2.0}, {1.0, -2.0, 2.0}}),
                new double[] {-5.0, -5.0, 5.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[] {1.0, 1.0, 1.0}, ConstraintType.EQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[] {1.0, -1.0, 1.0}, ConstraintType.GE, 0.0));

        PointValuePair result = optimizer.optimize(new LinearObjectiveFunctionWithConstraints(f, constraints),
                new double[] {1.0, 1.0, 1.0}, new MaxEval(100));
        Assertions.assertEquals(-5.0 / 3.0, result.getValue(), 1e-6);
        Assertions.assertArrayEquals(new double[] {1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test with a non-differentiable objective function.
     */
    @Test
    public void testNonDifferentiableFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, new GoalTypeDetector(),
                new SimpleUnivariateValueChecker(1e-6, 1e-8));

        UnivariateFunction f = x -> {
            if (x < 0) {
                return Double.POSITIVE_INFINITY;
            }
            return Math.abs(Math.sin(Math.PI / x)) / (x * x);
        };
        double[] startPoint = new double[] {1.0};
        PointValuePair result = optimizer.optimize(f, startPoint);
        Assertions.assertEquals(1.0 / (2 * Math.PI), result.getValue(), 1e-6);
        Assertions.assertEquals(1.0 / (2 * Math.PI), result.getPoint(), 1e-6);
    }
}