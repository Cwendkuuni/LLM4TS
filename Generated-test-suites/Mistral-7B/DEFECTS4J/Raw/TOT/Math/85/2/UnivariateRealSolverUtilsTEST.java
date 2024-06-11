package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.UnivariateSolver;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Test cases for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a simple polynomial function and given endpoints.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolvePolynomial() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = 1.5;
        UnivariateSolver solver = new BrentSolver();
         double root = UnivariateRealSolverUtils.solve(f, x0, x1);
         assertEquals(expected, root, 1e-5);
         System.out.println("Test solvePolynomial: passed.");
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with a polynomial function and endpoints that do not bracket a root.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveNoRoots() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -3.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            System.out.println("Test solveNoRoots: passed.");
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a simple polynomial function and given endpoints and accuracy.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolvePolynomialWithAccuracy() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double expected = 1.5;
        double absoluteAccuracy = 1e-7;
        UnivariateSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(absoluteAccuracy);
         double root = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
         assertEquals(expected, root, absoluteAccuracy);
         System.out.println("Test solvePolynomialWithAccuracy: passed.");
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with a polynomial function and endpoints that do not bracket a root, and accuracy.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveNoRootsWithAccuracy() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -3.0, 1.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-7;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            System.out.println("Test solveNoRootsWithAccuracy: passed.");
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with a simple polynomial function and given initial, lower bound and upper bound.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketPolynomial() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double[] expectedBrackets = {1.0, 2.0};
        assertArrayEquals(expectedBrackets, brackets, 1e-5);
        System.out.println("Test bracketPolynomial: passed.");
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a simple polynomial function, given initial, lower bound, upper bound and maximum iterations.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketPolynomialMaxIter() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -2.0, 1.0);
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        int maxIterations = 5;
        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
        double[] expectedBrackets = {1.0, 2.0};
        assertArrayEquals(expectedBrackets, brackets, 1e-5);
        System.out.println("Test bracketPolynomialMaxIter: passed.");
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with a polynomial function, given initial, lower bound, upper bound and maximum iterations,
     * and with a non-bracketing result.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketNoRoots() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3.0, 1.0, -3.0, 1.0);
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        int maxIterations = 5;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            System.out.println("Test bracketNoRoots: passed.");
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     * with two given values.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double actual = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, actual, 1e-5);
        System.out.println("Test midpoint: passed.");
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     * with a null function.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSetupNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}