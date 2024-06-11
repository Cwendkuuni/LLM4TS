package org.apache.commons.math.analysis.solvers.utils;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.PrecisionModelRuntimeException;

/**
 * Test for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest extends TestCase {

    /**
     * Test for solve method.
     */
    public void testSolve() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double expected = 0.0;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expected, result, 1e-12);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method with given absolute accuracy.
     */
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double expected = 0.0;
        double absoluteAccuracy = 1e-15;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(expected, result, absoluteAccuracy);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method with null function.
     */
    public void testSolveWithNullFunction() {
        UnivariateFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;

        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("NullArgumentException was not thrown.");
        } catch (NullArgumentException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method with invalid interval.
     */
    public void testSolveWithInvalidInterval() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;

        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("ConvergenceException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Ignore
        } catch (ConvergenceException e) {
            // Expected
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was not thrown.");
        }
    }

    /**
     * Test for bracket method.
     */
    public void testBracket() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertEquals(lowerBound, result[0], 1e-12);
            assertEquals(upperBound, result[1], 1e-12);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for bracket method with null function.
     */
    public void testBracketWithNullFunction() {
        UnivariateFunction f = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            fail("NullArgumentException was not thrown.");
        } catch (NullArgumentException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for bracket method with invalid interval.
     */
    public void testBracketWithInvalidInterval() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 0.0;

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            fail("IllegalArgumentException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Ignore
        } catch (ConvergenceException e) {
            fail("ConvergenceException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for bracket method with maximum iterations.
     */
    public void testBracketWithMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 0;

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            fail("ConvergenceException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Ignore
        } catch (ConvergenceException e) {
            // Expected
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was not thrown.");
        }
    }

    /**
     * Test for solve method using SecantSolver.
     */
    public void testSolveWithSecantSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;
        BracketedUnivariateSolver solver = new SecantSolver();

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, x0, x1, solver);
            assertEquals(bracketedValues[0], x0, 1e-12);
            assertEquals(bracketedValues[1], x1, 1e-12);
            assertEquals(result, f.value(x1), 1e-12);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method using BrentSolver.
     */
    public void testSolveWithBrentSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;
        BracketedUnivariateSolver solver = new BrentSolver();

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, x0, x1, solver);
            assertEquals(bracketedValues[0], x0, 1e-12);
            assertEquals(bracketedValues[1], x1, 1e-12);
            assertEquals(result, f.value(x1), 1e-12);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method using LaguerreSolver.
     */
    public void testSolveWithLaguerreSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;
        BracketedUnivariateSolver solver = new LaguerreSolver();

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, x0, x1, solver);
            assertEquals(bracketedValues[0], x0, 1e-12);
            assertEquals(bracketedValues[1], x1, 1e-12);
            assertEquals(result, f.value(x1), 1e-12);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method with invalid solver.
     */
    public void testSolveWithInvalidSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;

        try {
            BracketedUnivariateSolver solver = new Object();
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            fail("IllegalArgumentException was not thrown.");
        } catch (NullArgumentException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for bracket method with invalid solver.
     */
    public void testBracketWithInvalidSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            BracketedUnivariateSolver solver = new Object();
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, solver);
            fail("IllegalArgumentException was not thrown.");
        } catch (NullArgumentException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException was thrown but not expected.");
        } catch (ConvergenceException e) {
            fail("ConvergenceException was thrown but not expected.");
        }
    }

    /**
     * Test for solve method with max iterations exceeded.
     */
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;
        BracketedUnivariateSolver solver = new LaguerreSolver();
        solver.setMaxIterations(0);

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
            fail("MaxIterationsExceededException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Ignore
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test for bracket method with max iterations exceeded.
     */
    public void testBracketWithMaxIterationsExceeded() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        BracketedUnivariateSolver solver = new LaguerreSolver();
        solver.setMaxIterations(0);

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, solver);
            fail("MaxIterationsExceededException was not thrown.");
        } catch (FunctionEvaluationException e) {
            // Ignore
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}