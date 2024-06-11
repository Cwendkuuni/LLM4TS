package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = (double x) -> x - 5;
        double result = UnivariateRealSolverUtils.solve(f, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = (double x) -> x * x - 25;
        double result = UnivariateRealSolverUtils.solve(f, 0, 10, 0.0001);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = (double x) -> x * x - 25;
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 1, -10, 10);
        assertEquals(-4.0, bracket[0], 0.0001);
        assertEquals(6.0, bracket[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, midpoint, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealSolverUtils.solve(null, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() {
        UnivariateRealFunction f = (double x) -> x - 5;
        UnivariateRealSolverUtils.solve(f, 10, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoot() {
        UnivariateRealFunction f = (double x) -> x * x + 25;
        UnivariateRealSolverUtils.bracket(f, 1, -10, 10);
    }

}