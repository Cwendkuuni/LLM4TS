package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BrentSolver.BrentMethod;
import org.apache.commons.math3.solver.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.solver.SecantSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * JUnit 4 test cases for UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, -4.0, 4.0, -1.0}, 0.0);
        double x0 = -3.0;
        double x1 = 3.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        double expected = Precision.round(2.0, 3.0);
        assertEquals(expected, result, 1e-6);

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 0.0);
        try {
            UnivariateRealSolverUtils.solve(f1, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException ex) {
            // expected
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, 1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.solve(f2, x0, x1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // expected
        }

        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0}, 0.0);
        double[] bracketing = UnivariateRealSolverUtils.bracket(f3, 1.0, 1.0, 5.0);
        double a = bracketing[0];
        double b = bracketing[1];
        UnivariateSolver solver = new SecantSolver();
        double solution = solver.solve(f3, a, b);
        assertEquals(solution, result, 1e-6);

        UnivariateRealFunction f4 = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 0.0);
        UnivariateRealSolver solver1 = new LevenbergMarquardtIterativeSolver();
        double solution1 = solver1.solve(f4, 1.0);
        double xResult = UnivariateRealSolverUtils.solve(f4, x0, x1);
        assertEquals(solution1, xResult, 1e-6);

        UnivariateRealFunction f5 = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0}, 0.0);
        UnivariateRealSolver solver2 = new BrentSolver(BrentMethod.BRENT);
        double solution2 = solver2.solve(f5, x0, x1);
        assertEquals(solution2, result, 1e-6);
    }

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, -4.0, 4.0, -1.0}, 0.0);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double a = result[0];
        double b = result[1];

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f1, initial, lowerBound, upperBound);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // expected
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, 1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f2, initial, lowerBound, upperBound);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0}, 0.0);
        double[] bracketingResult = UnivariateRealSolverUtils.bracket(f3, initial, lowerBound, upperBound, 10);
        double a1 = bracketingResult[0];
        double b1 = bracketingResult[1];
        double[] expectedBracketing = {1.0, 2.0};
        assertArrayEquals(expectedBracketing, new Object[]{a1, b1});

        UnivariateRealFunction f4 = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f4, initial, lowerBound, upperBound, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // expected
        }

        UnivariateRealFunction f5 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0}, 0.0);
        double[] bracketingResult1 = UnivariateRealSolverUtils.bracket(f5, initial, lowerBound, upperBound, 5);
        double a2 = bracketingResult1[0];
        double b2 = bracketingResult1[1];
        double[] expectedBracketing1 = {1.0, 3.0};
        assertArrayEquals(expectedBracketing1, new Object[]{a2, b2});

        UnivariateRealFunction f6 = new PolynomialFunction(new double[]{1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f6, initial, lowerBound, upperBound);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f7 = new PolynomialFunction(new double[]{1.0, 1.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f7, initial, lowerBound, upperBound);
            fail("Expected NoRootsException");
        } catch (NoRootsException ex) {
            // expected
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 2.5;
        double b = 4.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        double expected = (a + b) / 2.0;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testSetup() throws Exception {
        UnivariateRealFunction f = null;
        try {
            UnivariateRealSolverUtils.setup(f);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testSolveWithInvalidArguments() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 0.0);
        double x0 = 1.0;
        double x1 = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{1.0, 1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.solve(f1, x0, x1, 1.0, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0}, 0.0);
        double lowerBound = 1.0;
        double upperBound = 2.0;
        try {
            UnivariateRealSolverUtils.solve(f2, x0, lowerBound, upperBound, 1.0, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testBracketWithInvalidArguments() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -2.0}, 0.0);
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 1.0, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{1.0, 1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f1, initial, lowerBound, upperBound, 1.0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f2, initial, lowerBound, upperBound, 1, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f3 = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0}, 0.0);
        double lowerBound1 = 1.0;
        double upperBound1 = 2.0;
        try {
            UnivariateRealSolverUtils.bracket(f3, initial, lowerBound1, upperBound1, 5, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f4 = new PolynomialFunction(new double[]{1.0, 0.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f4, initial, lowerBound, upperBound);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        UnivariateRealFunction f5 = new PolynomialFunction(new double[]{1.0, 1.0}, 0.0);
        try {
            UnivariateRealSolverUtils.bracket(f5, initial, lowerBound, upperBound);
            fail("Expected NoRootsException");
        } catch (NoRootsException ex) {
            // expected
        }
    }
}