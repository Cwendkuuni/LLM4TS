package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFindFailureException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple polynomial function for testing. */
    private static final UnivariateFunction FUNCTION = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0});

    /** A specific instance of the solver for testing. */
    private BaseSecantSolver solver;

    /**
     * Set up the test by creating a specific instance of the solver.
     */
    @Before
    public void setUp() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLENYI);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function.
     */
    @Test(expected = RootFindFailureException.class)
    public void testSolveSimplePolynomialFailure() {
        try {
            solver.solve(0, FUNCTION, -1.0, 1.0, 0.0);
        } catch (MaxEvalExceededException e) {
            throw new RootFindFailureException("Unexpected max evaluations exceeded.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function.
     */
    @Test
    public void testSolveSimplePolynomial() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the bracketing correctly.
     */
    @Test
    public void testSolveSimplePolynomialBracketing() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -2.0, 0.5, 1.5);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that a zero is correctly identified.
     */
    @Test
    public void testSolveSimplePolynomialZero() {
        double root = 0.0;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the allowed solutions correctly.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolutions() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the allowed solutions incorrectly.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveSimplePolynomialAllowedSolutionsInvalid() {
        double root = 0.5;
        solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles a large number of max evaluations.
     */
    @Test
    public void testSolveSimplePolynomialMaxEvaluations() {
        double root = 0.5;
        double solution = solver.solve(1_000_000, FUNCTION, -1.0, 1.0, 0.0);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the function value accuracy.
     */
    @Test
    public void testSolveSimplePolynomialFunctionValueAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, 1e-12);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the absolute accuracy.
     */
    @Test
    public void testSolveSimplePolynomialAbsoluteAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, 1e-6, 1e-12);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the relative accuracy.
     */
    @Test
    public void testSolveSimplePolynomialRelativeAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, 1e-4, 1e-6);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveSimplePolynomialConvergenceException() {
        solver = new IllinoisSolver(1e-12, IllinoisSolver.Method.ILLENYI);
        try {
            solver.solve(0, FUNCTION, -1.0, 1.0, 0.0);
        } catch (MaxEvalExceededException e) {
            throw new ConvergenceException("Unexpected maximum evaluations exceeded.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple polynomial function, checking that the solver handles the MathInternalError exception.
     */
    @Test(expected = MathInternalError.class)
    public void testSolveSimplePolynomialMathInternalError() {
        try {
            new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, null);
        } catch (IllegalArgumentException e) {
            throw new MathInternalError("Unexpected IllegalArgumentException.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function.
     */
    @Test(expected = RootFindFailureException.class)
    public void testSolveSimplePolynomialAllowedSolutionFailure() {
        try {
            solver.solve(0, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.ABOVE_SIDE);
        } catch (MaxEvalExceededException e) {
            throw new RootFindFailureException("Unexpected max evaluations exceeded.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolution() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the allowed solutions incorrectly.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveSimplePolynomialAllowedSolutionInvalid() {
        solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles a large number of max evaluations.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolutionMaxEvaluations() {
        double root = 0.5;
        double solution = solver.solve(1_000_000, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the function value accuracy.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolutionFunctionValueAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE, 1e-12);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the absolute accuracy.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolutionAbsoluteAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE, 1e-6, 1e-12);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the relative accuracy.
     */
    @Test
    public void testSolveSimplePolynomialAllowedSolutionRelativeAccuracy() {
        double root = 0.5;
        double solution = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE, 1e-4, 1e-6);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveSimplePolynomialAllowedSolutionConvergenceException() {
        solver = new IllinoisSolver(1e-12, IllinoisSolver.Method.ILLENYI);
        try {
            solver.solve(0, FUNCTION, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        } catch (MaxEvalExceededException e) {
            throw new ConvergenceException("Unexpected maximum evaluations exceeded.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a simple polynomial function, checking that the solver handles the MathInternalError exception.
     */
    @Test(expected = MathInternalError.class)
    public void testSolveSimplePolynomialAllowedSolutionMathInternalError() {
        try {
            new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, null, AllowedSolution.LEFT_SIDE);
        } catch (IllegalArgumentException e) {
            throw new MathInternalError("Unexpected IllegalArgumentException.", e);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method
     * with a simple polynomial function and a zero function value.
     */
    @Test
    public void testSolveSimplePolynomialZeroFunctionValue() {
        double root = 0.0;
        double solution = new RegulaFalsiSolver(1e-6).solve(10, FUNCTION, -1.0, 1.0, 0.0);
        assertEquals(Precision.safeEqual(root, solution), true);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method
     * with a simple polynomial function and a function value that is very close to zero.
     */
    @Test
    public void testSolveSimplePolynomialFunctionValueCloseToZero() {
        double root = 0.5;
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0e-15});
        double solution = new RegulaFalsiSolver(1e-6).solve(10, function, -1.0, 1.0, 0.0);
        assertEquals(Precision.safeEqual(root, solution), true);
    }
}