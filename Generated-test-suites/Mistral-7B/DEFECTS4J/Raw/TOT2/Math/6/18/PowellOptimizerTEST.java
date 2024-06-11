package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction.Direction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorObjectiveFunctionSet;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test for the optimization when no bracketing is needed.
     */
    @Test
    public void testOptimization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, null);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0);
        PointValuePair result = optimizer.optimize(startPoint, function);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-8);
    }

    /**
     * Test for the optimization when bracketing is needed.
     */
    @Test
    public void testOptimizationWithBracketing() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new BracketFinder());
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0);
        PointValuePair result = optimizer.optimize(startPoint, function);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-8);
    }

    /**
     * Test for the optimization with user-defined tolerances.
     */
    @Test
    public void testOptimizationWithCustomTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, null);
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0);
        PointValuePair result = optimizer.optimize(startPoint, function);
        Assert.assertEquals(0.0, result.getValue(), 1e-4);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-4);
    }

    /**
     * Test for the optimization with user-defined convergence checker.
     */
    @Test
    public void testOptimizationWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, null, null, new CustomChecker());
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0);
        PointValuePair result = optimizer.optimize(startPoint, function);
        Assert.assertEquals(0.0, result.getValue(), 1e-4);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-4);
    }

    /**
     * Test for the optimization with linear constraints.
     */
    @Test
    public void testOptimizationWithLinearConstraints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, null);
        double[] startPoint = {1.0, 2.0};
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0)});
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 0.0, ConstraintType.LEQ),
                new LinearConstraint(new double[]{1.0, 1.0}, 1.0, ConstraintType.GEQ)});
        PointValuePair result = optimizer.optimize(startPoint, objectiveFunctions, constraints);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-8);
    }

    /**
     * Test for the optimization with vector constraints.
     */
    @Test
    public void testOptimizationWithVectorConstraints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, null);
        double[] startPoint = {1.0, 2.0};
        VectorObjectiveFunctionSet objectiveFunctions = new VectorObjectiveFunctionSet(new VectorObjectiveFunction[]{
                new VectorObjectiveFunction(new double[]{-3.0, 4.0}, 0)});
        VectorConstraintSet constraints = new VectorConstraintSet(new VectorConstraint[]{
                new VectorConstraint(new double[]{1.0, 1.0}, new double[]{0.0}, ConstraintType.LEQ),
                new VectorConstraint(new double[]{1.0, 1.0}, new double[]{1.0}, ConstraintType.GEQ)});
        PointValuePair result = optimizer.optimize(startPoint, objectiveFunctions, constraints);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-8);
    }

    /**
     * Custom convergence checker for testing purposes.
     */
    private static class CustomChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return Math.abs(current.getValue() - previous.getValue()) < 1e-6;
        }
    }

    /**
     * Test for the optimization with custom convergence checker and user-defined
     * tolerances.
     */
    @Test
    public void testOptimizationWithCustomCheckerAndCustomTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, null, null, new CustomChecker());
        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 0);
        PointValuePair result = optimizer.optimize(startPoint, function);
        Assert.assertEquals(0.0, result.getValue(), 1e-4);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-4);
    }
}