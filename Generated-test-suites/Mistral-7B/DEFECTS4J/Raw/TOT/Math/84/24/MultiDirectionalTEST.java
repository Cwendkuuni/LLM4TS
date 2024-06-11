package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NorootFoundException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci2Search;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing MultiDirectional optimizer class.
 */
public class MultiDirectionalTest {

    /**
     * Test MultiDirectional optimizer with default parameters.
     */
    @Test
    public void testDefault() {
        ObjectiveFunction function = new FunctionWithSimpleRoot();
        MultiDirectional optimizer = new MultiDirectional();

        GoalType goalType = new SimpleGoalType(1.0e-8, 1.0e-8);
        InitialGuess initialGuess = new InitialGuess(new double[] {0.5});
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(goalType, 100);

        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, initialGuess, convergenceChecker);

        PointValuePair solution = optimizer.getSolution();
        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getPoint()[0], 1.0e-6);
        Assert.assertEquals(0.0, solution.getValue(), 1.0e-8);
    }

    /**
     * Test MultiDirectional optimizer with provided parameters.
     */
    @Test
    public void testCustom() {
        ObjectiveFunction function = new FunctionWithSimpleRoot();
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.75);

        GoalType goalType = new SimpleGoalType(1.0e-8, 1.0e-8);
        InitialGuess initialGuess = new InitialGuess(new double[] {0.5});
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(goalType, 100);

        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, initialGuess, convergenceChecker);

        PointValuePair solution = optimizer.getSolution();
        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getPoint()[0], 1.0e-6);
        Assert.assertEquals(0.0, solution.getValue(), 1.0e-8);
    }

    /**
     * Test MultiDirectional optimizer with invalid input parameters.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidExpansionCoefficient() {
        ObjectiveFunction function = new FunctionWithSimpleRoot();
        MultiDirectional optimizer = new MultiDirectional(-1.0, 0.5);
    }

    /**
     * Test MultiDirectional optimizer with invalid input parameters.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidContractionCoefficient() {
        ObjectiveFunction function = new FunctionWithSimpleRoot();
        MultiDirectional optimizer = new MultiDirectional(2.0, -0.5);
    }

    /**
     * Test MultiDirectional optimizer with a function having no roots.
     */
    @Test
    public void testNoRoots() {
        ObjectiveFunction function = new FunctionWithNoRoots();
        MultiDirectional optimizer = new MultiDirectional();

        GoalType goalType = new SimpleGoalType(1.0e-8, 1.0e-8);
        InitialGuess initialGuess = new InitialGuess(new double[] {0.5});
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(goalType, 100);

        optimizer.setMaxEvaluations(1000);
        try {
            optimizer.optimize(function, initialGuess, convergenceChecker);
            Assert.fail("Should have thrown NorootFoundException.");
        } catch (NorootFoundException e) {
            // Expected outcome
        }
    }

    /**
     * Function with a simple root.
     */
    private static class FunctionWithSimpleRoot implements ObjectiveFunction {

        @Override
        public double value(final double x) {
            return Math.pow(x - 3.0, 2.0);
        }

        @Override
        public double gradient(final double x) {
            return 2.0 * (x - 3.0);
        }

        @Override
        public double[] gradient(final double[] x) {
            return new double[] {2.0 * (x[0] - 3.0)};
        }

        @Override
        public double value(final double[] x) {
            return Math.pow(x[0] - 3.0, 2.0);
        }
    }

    /**
     * Function with no roots.
     */
    private static class FunctionWithNoRoots implements ObjectiveFunction {

        @Override
        public double value(final double x) {
            return Math.sin(Math.PI * x);
        }

        @Override
        public double gradient(final double x) {
            return Math.cos(Math.PI * x);
        }

        @Override
        public double[] gradient(final double[] x) {
            return new double[] {-Math.sin(Math.PI * x)};
        }

        @Override
        public double value(final double[] x) {
            return Math.sin(Math.PI * x[0]);
        }
    }
}