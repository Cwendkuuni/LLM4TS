package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BisectionSolver;
import org.apache.commons.math3.solver.BracketedRealRootFinder;
import org.apache.commons.math3.solver.FibonacciSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.SecantSolver;
import org.apache.commons.math3.solver.SimpleBracketedRealSolver;
import org.apache.commons.math3.solver.UnivariateSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    /**
     * Test solve method with default solver and default accuracy.
     */
    @Test
    public void testDefaultSolver() {
        UnivariateRealFunction f = new FunctionWithRootAtZero();
        double x0 = 1.0;
        double x1 = 2.0;

        try {
            double root = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(0.0, root, Precision.EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test solve method with default solver and custom accuracy.
     */
    @Test
    public void testCustomSolverAccuracy() {
        UnivariateRealFunction f = new FunctionWithRootAtZero();
        double x0 = 1.0;
        double x1 = 2.0;
        double accuracy = 0.001;

        try {
            double root = UnivariateRealSolverUtils.solve(f, x0, x1, accuracy);
            assertEquals(0.0, root, Precision.epsilon(accuracy));
        } catch (ConvergenceException | FunctionEvaluationException |
                IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test solve method with custom solver.
     */
    @Test
    public void testCustomSolver() {
        UnivariateRealFunction f = new FunctionWithRootAtZero();
        double x0 = 1.0;
        double x1 = 2.0;

        UnivariateSolver solver = new BrentSolver();

        try {
            double root = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            assertEquals(0.0, root, Precision.EPSILON);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test bracket method with default solver and default parameters.
     */
    @Test
    public void testBracketDefault() {
        UnivariateRealFunction f = new FunctionWithRootsAtMinusOneAndOne();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        try {
            double[] roots = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertArrayEquals(new double[]{ -1.0, 1.0 }, roots, 0.0);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test bracket method with custom solver and default parameters.
     */
    @Test
    public void testBracketCustomSolver() {
        UnivariateRealFunction f = new FunctionWithRootsAtMinusOneAndOne();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateSolver solver = new SecantSolver();

        try {
            double[] roots = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, solver);
            assertArrayEquals(new double[]{ -1.0, 1.0 }, roots, 0.0);
        } catch (ConvergenceException | FunctionEvaluationException |
                IllegalArgumentException | NoBracketingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test bracket method with custom solver, custom parameters, and 10
     * iterations.
     */
    @Test
    public void testBracketCustomSolverIterations() {
        UnivariateRealFunction f = new FunctionWithRootsAtMinusOneAndOne();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        UnivariateSolver solver = new BracketedRealRootFinder(new SecantSolver());

        try {
            double[] roots = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations, solver);
            assertArrayEquals(new double[]{ -1.0, 1.0 }, roots, 0.0);
        } catch (ConvergenceException | FunctionEvaluationException |
                IllegalArgumentException | NoBracketingException |
                TooManyIterationsException | PrecisionRequiredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test bracket method with function that has no roots in the interval.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoots() {
        UnivariateRealFunction f = new FunctionWithNoRoots();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    /**
     * Test midpoint method.
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;

        double expected = 1.5;

        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(expected, midpoint, Precision.EPSILON);
    }

    /**
     * Test setup method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }

    /**
     * Test solve method with function that throws FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new FunctionWithExceptionAtOne();
        double x0 = 0.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /**
     * Test bracket method with function that throws FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() {
        UnivariateRealFunction f = new FunctionWithExceptionAtOne();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    /**
     * Test bracket method with function that throws ConvergenceException.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new FunctionWithCustomMaxIterations();
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 1);
    }

    /**
     * Test solve method with function that throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() {
        UnivariateRealFunction f = new FunctionWithIllegalParameters();
        double x0 = 0.0;
        double x1 = 2.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /**
     * Test bracket method with function that throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() {
        UnivariateRealFunction f = new FunctionWithIllegalParameters();
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 2.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    /**
     * Function with a root at zero.
     */
    private static class FunctionWithRootAtZero implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }
    }

    /**
     * Function with roots at -1 and 1.
     */
    private static class FunctionWithRootsAtMinusOneAndOne implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x + 1.0, 2) - 1.0;
        }
    }

    /**
     * Function with no roots in the interval.
     */
    private static class FunctionWithNoRoots implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) + 1.0;
        }
    }

    /**
     * Function that throws FunctionEvaluationException at x = 1.
     */
    private static class FunctionWithExceptionAtOne implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x == 1.0) {
                throw new FunctionEvaluationException("Function evaluation exception at x = 1.0");
            }
            return Math.pow(x, 2);
        }
    }

    /**
     * Function with custom maximum iterations.
     */
    private static class FunctionWithCustomMaxIterations implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 0.5) {
                throw new ConvergenceException("Maximum iterations exceeded");
            }
            return Math.sin(x);
        }
    }

    /**
     * Function with illegal parameters.
     */
    private static class FunctionWithIllegalParameters implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new IllegalArgumentException("Illegal parameters");
        }
    }
}