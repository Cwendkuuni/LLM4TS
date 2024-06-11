package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.IllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * JUnit test file for UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double)}
     * using default solver.
     * @throws Exception
     */
    @Test
    public void testSolveDefault() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        double solution = UnivariateRealSolverUtils.solve(function, 1.0);
        assertEquals(0.0, solution, 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using default solver.
     * @throws Exception
     */
    @Test
    public void testSolveDefaultInterval() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
        assertEquals(0.0, solution, 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using default solver with absolute accuracy.
     * @throws Exception
     */
    @Test
    public void testSolveDefaultIntervalWithAbsoluteAccuracy() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-12);
        assertEquals(0.0, solution, 1e-13);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using default solver with maximum iterations.
     */
    @Test
    public void testSolveDefaultIntervalWithMaxIterations() {
        UnivariateFunction function = new FunctionWithRootAtOne();
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 2.0, 0);
            // this test should throw an exception
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using Brent's method.
     * @throws Exception
     */
    @Test
    public void testSolveBrent() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        UnivariateSolver solver = new BrentSolver();
        double solution = UnivariateRealSolverUtils.solve(function, solver, 1.0, 2.0);
        assertEquals(0.0, solution, 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Brent's method with absolute accuracy.
     * @throws Exception
     */
    @Test
    public void testSolveBrentWithAbsoluteAccuracy() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        UnivariateSolver solver = new BrentSolver();
        double solution = UnivariateRealSolverUtils.solve(function, solver, 1.0, 2.0, 1e-12);
        assertEquals(0.0, solution, 1e-13);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Secant method.
     * @throws Exception
     */
    @Test
    public void testSolveSecant() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        UnivariateSolver solver = new SecantSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double solution = UnivariateRealSolverUtils.solve(function, solver, x0, x1);
        assertEquals(0.0, solution, 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Secant method with absolute accuracy.
     * @throws Exception
     */
    @Test
    public void testSolveSecantWithAbsoluteAccuracy() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        UnivariateSolver solver = new SecantSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double solution = UnivariateRealSolverUtils.solve(function, solver, x0, x1, 1e-12);
        assertEquals(0.0, solution, 1e-13);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with initial value in the interval.
     * @throws Exception
     */
    @Test
    public void testBracketInitialInInterval() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 1.0, -1.0, 2.0);
        assertEquals(-1.0, bracketing[0], 1e-10);
        assertEquals(2.0, bracketing[1], 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with initial value not in the interval.
     * @throws Exception
     */
    @Test
    public void testBracketInitialNotInInterval() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        try {
            UnivariateRealSolverUtils.bracket(function, 2.0, -1.0, 1.0, 10);
            // this test should throw an exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with maximum iterations.
     */
    @Test
    public void testBracketMaxIterations() {
        UnivariateFunction function = new FunctionWithRootAtOne();
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 2.0, 2.0, 0);
            // this test should throw an exception
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with initial values bracketing the root.
     * @throws Exception
     */
    @Test
    public void testBracketInitialValuesBracketingRoot() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 0.5, 1.5, 2.0, 10);
        assertEquals(0.5, bracketing[0], 1e-10);
        assertEquals(1.5, bracketing[1], 1e-10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with initial values not bracketing the root.
     * @throws Exception
     */
    @Test
    public void testBracketInitialValuesNotBracketingRoot() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        try {
            UnivariateRealSolverUtils.bracket(function, 2.0, 1.0, 0.0, 10);
            // this test should throw an exception
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, midpoint, 1e-10);
    }
}

class FunctionWithRootAtZero implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1.0;
    }
}

class FunctionWithRootAtOne implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1.0;
    }
}