package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BracketedBrentSolver;
import org.apache.commons.math3.solver.LaguerreSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.SecantSolver;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Test class for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {
    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)} method.
     */
    @org.junit.Test
    public void testSolve() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
        assertEquals("The result is not correct.", -1.0, result, 1e-6);

        function = new PolynomialFunction(x -> x * x + 2.0);
        try {
            result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
            assertThrows(MaxIterationsExceededException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0));
        } catch (ConvergenceException e) {
            // expected
        }

        function = new PolynomialFunction(x -> x);
        try {
            result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
            assertThrows(IllegalArgumentException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0));
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)} method.
     */
    @org.junit.Test
    public void testSolveWithPrecision() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-8);
        assertEquals("The result is not correct.", -1.0, result, 1e-10);

        function = new PolynomialFunction(x -> x * x + 2.0);
        try {
            result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-8);
            assertThrows(MaxIterationsExceededException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-8));
        } catch (ConvergenceException e) {
            // expected
        }

        function = new PolynomialFunction(x -> x);
        try {
            result = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-8);
            assertThrows(IllegalArgumentException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-8));
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)} method.
     */
    @org.junit.Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0);
        assertEquals("The bracketing interval is not correct.", -1.0, result[0], 1e-6);
        assertEquals("The bracketing interval is not correct.", 1.0, result[1], 1e-6);

        function = new PolynomialFunction(x -> x * x + 2.0);
        try {
            result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0);
            assertThrows(ConvergenceException.class, () ->
                    UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0));
        } catch (FunctionEvaluationException e) {
            // expected
        }

        function = new PolynomialFunction(x -> x);
        try {
            result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0);
            assertThrows(IllegalArgumentException.class, () ->
                    UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0));
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)} method.
     */
    @org.junit.Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 5);
        assertEquals("The bracketing interval is not correct.", -1.0, result[0], 1e-6);
        assertEquals("The bracketing interval is not correct.", 1.0, result[1], 1e-6);

        function = new PolynomialFunction(x -> x * x + 2.0);
        try {
            result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 5);
            assertThrows(ConvergenceException.class, () ->
                    UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 5));
        } catch (FunctionEvaluationException e) {
            // expected
        }

        function = new PolynomialFunction(x -> x);
        try {
            result = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 5);
            assertThrows(IllegalArgumentException.class, () ->
                    UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 5));
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)} method with different solvers.
     */
    @org.junit.Test
    public void testSolveWithDifferentSolvers() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);

        UnivariateRealSolver solver1 = new BrentSolver();
        double result1 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver1);
        assertEquals("The result is not correct for BrentSolver.", -1.0, result1, 1e-6);

        UnivariateRealSolver solver2 = new SecantSolver();
        double result2 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver2);
        assertEquals("The result is not correct for SecantSolver.", -1.0, result2, 1e-6);

        UnivariateRealSolver solver3 = new LaguerreSolver();
        try {
            result3 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver3);
            assertThrows(UnsupportedOperationException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver3));
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealSolver solver4 = new PowellSolver();
        try {
            result4 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver4);
            assertThrows(FunctionEvaluationException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver4));
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, UnivariateRealSolver)} method with different solvers.
     */
    @org.junit.Test
    public void testSolveWithDifferentSolversAndPrecision() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2.0);

        UnivariateRealSolver solver1 = new BrentSolver();
        double result1 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver1, 1e-8);
        assertEquals("The result is not correct for BrentSolver.", -1.0, result1, 1e-10);

        UnivariateRealSolver solver2 = new SecantSolver();
        double result2 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver2, 1e-8);
        assertEquals("The result is not correct for SecantSolver.", -1.0, result2, 1e-10);

        UnivariateRealSolver solver3 = new LaguerreSolver();
        try {
            result3 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver3, 1e-8);
            assertThrows(UnsupportedOperationException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver3, 1e-8));
        } catch (ConvergenceException e) {
            // expected
        }

        UnivariateRealSolver solver4 = new PowellSolver();
        try {
            result4 = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver4, 1e-8);
            assertThrows(FunctionEvaluationException.class, () ->
                    UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver4, 1e-8));
        } catch (ConvergenceException e) {
            // expected
        }
    }
}