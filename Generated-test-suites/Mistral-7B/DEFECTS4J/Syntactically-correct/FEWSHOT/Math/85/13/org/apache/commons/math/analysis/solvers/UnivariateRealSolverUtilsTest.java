package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double expectedResult = 0.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.bracket(function, x0, x0, x1);
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new ThrowingUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = null;
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 0.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.bracket(function, x0, x0, x1);
        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new ThrowingUnivariateRealFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = null;
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double[] expectedResult = {0.2, 0.8};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new ThrowingUnivariateRealFunction();
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 0.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new MockUnivariateRealFunction();
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int maximumIterations = 5;
        double[] expectedResult = {0.2, 0.8};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertArrayEquals(expectedResult, result);
    }

    private static class MockUnivariateRealFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) / x;
        }
    }

    private static class ThrowingUnivariateRealFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Function evaluation exception");
        }
    }

}