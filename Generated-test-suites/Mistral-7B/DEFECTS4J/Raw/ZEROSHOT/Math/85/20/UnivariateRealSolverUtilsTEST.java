package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;

import org.junit.Test;

/**
 * Test file for UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with default solver and default accuracy.
     */
    @Test
    public void testSolveDefault() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with default solver and default accuracy.
     */
    @Test
    public void testSolveDefaultAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1e-6);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with default solver and negative accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNegativeAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        UnivariateRealSolverUtils.solve(function, -2.0, 2.0, -1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with function that has no roots.
     */
    @Test(expected = NoRootsException.class)
    public void testSolveNoRoots() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with function that has no roots and specified accuracy.
     */
    @Test(expected = NoRootsException.class)
    public void testSolveNoRootsSpecifiedAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -1.0}) {
            @Override
            public double value(double x) {
                if (Math.abs(x - 0.0) < 1e-6) {
                    throw new ConvergenceException("Iterations exceeded");
                }
                return Math.pow(x, 2) + 1;
            }
        };
        UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with convergence exception and specified maximum iterations.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceExceptionMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -1.0}) {
            @Override
            public double value(double x) {
                if (Math.abs(x - 0.0) < 1e-6) {
                    throw new ConvergenceException("Iterations exceeded");
                }
                return Math.pow(x, 2) + 1;
            }
        };
        UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        assertEquals(-1.0, bracketing[0], 1e-6);
        assertEquals(1.0, bracketing[1], 1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     */
    @Test
    public void testBracketMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        double[] bracketing = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 10);
        assertEquals(-1.0, bracketing[0], 1e-6);
        assertEquals(1.0, bracketing[1], 1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function that has no bracketing.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketNoBracketing() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function that has no roots.
     */
    @Test(expected = NoRootsException.class)
    public void testBracketNoRoots() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 10);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with maximum iterations and function that has no bracketing.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketNoBracketingMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1000);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function that has no roots and specified maximum iterations.
     */
    @Test(expected = NoRootsException.class)
    public void testBracketNoRootsMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1000);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     */
    @Test
    public void testMidpoint() {
        assertEquals(0.5, UnivariateRealSolverUtils.midpoint(1.0, 2.0), 1e-6);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     */
    @Test
    public void testSetup() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, 1.0, 1.0});
        UnivariateRealSolverUtils.setup(function);
    }
}