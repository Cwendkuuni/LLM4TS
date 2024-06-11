package org.apache.commons.math.analysis.solvers; // make sure the package is correct

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Test class for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionIsNull() {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    @Test
    public void testSolve_InvalidBounds() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double x0 = Double.NEGATIVE_INFINITY;
        double x1 = Double.POSITIVE_INFINITY;

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    @Test
    public void testSolve_DefaultSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double x0 = 1.0;
        double x1 = 2.0;

        double solution = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(1.0, solution, 0.0001);
    }

    @Test
    public void testSolve_DefaultSolverWithAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;

        double solution = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(1.0, solution, absoluteAccuracy);
    }

    @Test
    public void testBracket_FunctionIsNull() {
        UnivariateRealFunction f = null;
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracket_InvalidBounds() {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double initial = 0.0;
        double lowerBound = Double.NEGATIVE_INFINITY;
        double upperBound = Double.POSITIVE_INFINITY;

        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracket_MaxIterationsExceeded() throws FunctionEvaluationException {
        UnivariateRealFunction f = (x -> Math.abs(Math.sin(x) - x));
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracket_FunctionRootBracketed() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = (x -> Math.pow(x, 2) - 1);
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 2.0;

        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(1.0, bracketingPoints[0], 0.0001);
        assertEquals(2.0, bracketingPoints[1], 0.0001);
        assertFalse(Double.isNaN(bracketingPoints[0]) || Double.isNaN(bracketingPoints[1]));
    }

}