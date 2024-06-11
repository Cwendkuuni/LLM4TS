package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) throws FunctionEvaluationException {
            return x * x - 4;
        }
    };

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 10.0);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 10.0, 0.0001);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 0.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(function, 10.0, 0.0);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 3.0, 0.0, 10.0);
        Assert.assertEquals(2.0, result[0], 0.0001);
        Assert.assertEquals(4.0, result[1], 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 3.0, 0.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 3.0, 10.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 3.0, 0.0, 10.0, -1);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2.0, 4.0);
        Assert.assertEquals(3.0, result, 0.0001);
    }
}