package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /** A test function for the solver. */
    private UnivariateRealFunction testFunction;

    /** The solver under test. */
    private BaseSecantSolver solver;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {

            /** {@inheritDoc} */
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
    }

    /**
     * Test the solver with a simple function.
     *
     * @throws ConvergenceException   If the solver fails to converge.
     * @throws MaxEvalExceededException If the maximum number of evaluations is
     *                                 exceeded.
     * @throws NoBracketingException  If the initial bounds do not bracket the
     *                                 root.
     * @throws UnsupportedOperationException If the solver does not support the
     *                                      given function.
     */
    @Test
    public void testSolve() throws ConvergenceException, MaxEvalExceededException, NoBracketingException, UnsupportedOperationException {
        double root = solver.solve(testFunction, -5.0, 5.0);
        Assert.assertEquals(Precision.SAFE_DOUBLE, 1.0, root, 1e-6);
    }

    /**
     * Test the solver with a function that has no real roots.
     *
     * @throws ConvergenceException   If the solver fails to converge.
     * @throws MaxEvalExceededException If the maximum number of evaluations is
     *                                 exceeded.
     * @throws NoBracketingException  If the initial bounds do not bracket the
     *                                 root.
     * @throws UnsupportedOperationException If the solver does not support the
     *                                      given function.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNoRealRoots() throws ConvergenceException, MaxEvalExceededException, NoBracketingException, UnsupportedOperationException {
        UnivariateRealFunction testFunctionWithNoRealRoots = new UnivariateRealFunction() {

            /** {@inheritDoc} */
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        solver.solve(testFunctionWithNoRealRoots, -5.0, 5.0);
    }

    /**
     * Test the solver with a function that has multiple roots.
     *
     * @throws ConvergenceException   If the solver fails to converge.
     * @throws MaxEvalExceededException If the maximum number of evaluations is
     *                                 exceeded.
     * @throws NoBracketingException  If the initial bounds do not bracket the
     *                                 root.
     * @throws UnsupportedOperationException If the solver does not support the
     *                                      given function.
     */
    @Test
    public void testSolveWithMultipleRoots() throws ConvergenceException, MaxEvalExceededException, NoBracketingException, UnsupportedOperationException {
        UnivariateRealFunction testFunctionWithMultipleRoots = new UnivariateRealFunction() {

            /** {@inheritDoc} */
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        double root1 = solver.solve(testFunctionWithMultipleRoots, -5.0, 0.0);
        Assert.assertEquals(Precision.SAFE_DOUBLE, -1.0, root1, 1e-6);
        double root2 = solver.solve(testFunctionWithMultipleRoots, 0.0, 5.0);
        Assert.assertEquals(Precision.SAFE_DOUBLE, 1.0, root2, 1e-6);
    }

    /**
     * Test the solver with a function that has a vertical asymptote.
     *
     * @throws ConvergenceException   If the solver fails to converge.
     * @throws MaxEvalExceededException If the maximum number of evaluations is
     *                                 exceeded.
     * @throws NoBracketingException  If the initial bounds do not bracket the
     *                                 root.
     * @throws UnsupportedOperationException If the solver does not support the
     *                                      given function.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveWithVerticalAsymptote() throws ConvergenceException, MaxEvalExceededException, NoBracketingException, UnsupportedOperationException {
        UnivariateRealFunction testFunctionWithVerticalAsymptote = new UnivariateRealFunction() {

            /** {@inheritDoc} */
            @Override
            public double value(double x) {
                if (x > 0) {
                    return 1.0 / x;
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            }
        };
        solver.solve(testFunctionWithVerticalAsymptote, -5.0, 5.0);
    }

    /**
     * Test the solver with a function that has a horizontal asymptote.
     *
     * @throws ConvergenceException   If the solver fails to converge.
     * @throws MaxEvalExceededException If the maximum number of evaluations is
     *                                 exceeded.
     * @throws NoBracketingException  If the initial bounds do not bracket the
     *                                 root.
     * @throws UnsupportedOperationException If the solver does not support the
     *                                      given function.
     */
    @Test
    public void testSolveWithHorizontalAsymptote() throws ConvergenceException, MaxEvalExceededException, NoBracketingException, UnsupportedOperationException {
        UnivariateRealFunction testFunctionWithHorizontalAsymptote = new UnivariateRealFunction() {

            /** {@inheritDoc} */
            @Override
            public double value(double x) {
                if (x < -5) {
                    return -1.0;
                } else if (x > 5) {
                    return 1.0;
                } else {
                    return Math.sin(x) / x;
                }
            }
        };
        double root = solver.solve(testFunctionWithHorizontalAsymptote, -5.5, 5.5);
        Assert.assertEquals(Precision.SAFE_DOUBLE, 0.0, root, 1e-6);
    }
}