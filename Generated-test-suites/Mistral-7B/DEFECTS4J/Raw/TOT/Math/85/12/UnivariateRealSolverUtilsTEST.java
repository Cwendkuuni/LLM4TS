package org.apache.commons.math.analysis.solvers.utils;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.IllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a simple function and default solver.
     */
    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction function = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;

        try {
            double root = UnivariateRealSolverUtils.solve(function, x0, x1);
            Assert.assertEquals(1.5, root, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a simple function and default solver and specified accuracy.
     */
    @Test
    public void testSolveWithDefaultSolverAndSpecifiedAccuracy() {
        UnivariateRealFunction function = new SimpleFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.01;

        try {
            double root = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
            Assert.assertEquals(1.5, root, absoluteAccuracy);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a function that has no roots in the given interval.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveWithNoRoots() {
        UnivariateRealFunction function = new NoRootsFunction();
        double x0 = 1.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a function that throws an exception when evaluated.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() {
        UnivariateRealFunction function = new ExceptionFunction();
        double x0 = 1.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with a simple function.
     */
    @Test
    public void testBracketWithSimpleFunction() {
        UnivariateRealFunction function = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            Assert.assertArrayEquals(new double[]{1.0, 1.5}, bracketingValues, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a simple function and a specified maximum number of iterations.
     */
    @Test
    public void testBracketWithSimpleFunctionAndMaxIterations() {
        UnivariateRealFunction function = new SimpleFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 5;

        try {
            double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            Assert.assertArrayEquals(new double[]{1.0, 1.5}, bracketingValues, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with a function that has no roots in the given interval.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketWithNoRoots() {
        UnivariateRealFunction function = new NoRootsFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a function that has no roots in the given interval and a specified maximum number of iterations.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketWithNoRootsAndMaxIterations() {
        UnivariateRealFunction function = new NoRootsFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 5;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }
}

class SimpleFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x - 1.5, 2);
    }
}

class NoRootsFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x - 2, 2) + 1;
    }
}

class ExceptionFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) throws FunctionEvaluationException {
        if (x > 1.0) {
            throw new FunctionEvaluationException("FunctionEvaluationException test");
        }
        return Math.pow(x - 1.5, 2);
    }
}