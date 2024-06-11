package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.function.LinearFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.PowellSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolveDefault() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        double x1 = 3.0;
        double expectedResult = 1.0;
        double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1);
        assertEquals(expectedResult, result, 1e-10);
    }

    @Test
    public void testSolveAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        double x1 = 3.0;
        double absoluteAccuracy = 1e-12;
        double expectedResult = 1.0;
        double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test
    public void testSolveExceptionNullFunction() {
        double x0 = 2.0;
        double x1 = 3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve((UnivariateRealFunction) null, x0, x1));
    }

    @Test
    public void testSolveExceptionNullArguments() {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(testFunction, -1.0, 2.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(testFunction, 2.0, -1.0));
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] expectedResult = {1.0, 2.0};
        double[] result = UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testBracketExceptionNullFunction() {
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket((UnivariateRealFunction) null, initial, lowerBound, upperBound));
    }

    @Test
    public void testBracketExceptionIllegalArguments() {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, -1.0, 2.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, 2.0, 1.0, 3.0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, 2.0, 2.0, 2.0));
    }

    @Test
    public void testBracketExceptionMaxIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound, maximumIterations));
    }

    @Test
    public void testBisectionSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        double x1 = 3.0;
        BisectionSolver bisectionSolver = new BisectionSolver();
        double result = bisectionSolver.solve(testFunction, x0, x1);
        assertEquals(expectedResult, result, 1e-10);
    }

    @Test
    public void testSecantSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        double x1 = 1.0;
        SecantSolver secantSolver = new SecantSolver();
        double result = secantSolver.solve(testFunction, x0, x1);
        assertEquals(expectedResult, result, 1e-10);
    }

    @Test
    public void testPowellSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        PowellSolver powellSolver = new PowellSolver();
        double result = powellSolver.solve(testFunction, x0);
        assertEquals(expectedResult, result, 1e-10);
    }

    @Test
    public void testBrentSolver() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction testFunction = new LinearFunction(1.0, -1.0);
        double x0 = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        BrentSolver brentSolver = new BrentSolver();
        double result = brentSolver.solve(testFunction, x0, lowerBound, upperBound);
        assertEquals(expectedResult, result, 1e-10);
    }
}