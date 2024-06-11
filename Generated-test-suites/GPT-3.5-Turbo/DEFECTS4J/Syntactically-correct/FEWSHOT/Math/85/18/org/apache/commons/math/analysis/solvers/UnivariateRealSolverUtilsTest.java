package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    // Helper method to create a fake function for testing
    private UnivariateRealFunction fakeFunction;
    
    public UnivariateRealSolverUtilsTest() {
        fakeFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x - 4;
            }
        };
    }
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(fakeFunction, 0, 10);
        assertEquals(2, result, 0);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(fakeFunction, 0, 10, 0.001);
        assertEquals(2, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(fakeFunction, 10, 0);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(fakeFunction, 3, 0, 10);
        assertEquals(2, result[0], 0);
        assertEquals(4, result[1], 0);
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(fakeFunction, 3, 0, 10, 100);
        assertEquals(2, result[0], 0);
        assertEquals(4, result[1], 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 3, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(fakeFunction, 3, 10, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithInvalidBracketing() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(fakeFunction, 0, 1, 2);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5, result, 0);
    }
}