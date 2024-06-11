package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolveWithDefaultSolver() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double expectedResult = 0.366497; // a root of the test function

        double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1);
        assertEquals(expectedResult, result, 1e-8); // allow for small error
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction testFunction = new ThrowingFunction();
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(testFunction, x0, x1);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction testFunction = new DivergingFunction();
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(testFunction, x0, x1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws Exception {
        UnivariateRealFunction testFunction = null;
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(testFunction, x0, x1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 1.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(testFunction, x0, x1);
    }

    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-9; // smaller than the default absolute accuracy

        double result = UnivariateRealSolverUtils.solve(testFunction, x0, x1, absoluteAccuracy);
        double expectedResult = 0.366497; // a root of the test function

        assertEquals(expectedResult, result, 1e-10); // allow for smaller error due to increased accuracy
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction testFunction = new ThrowingFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws Exception {
        UnivariateRealFunction testFunction = new DivergingFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws Exception {
        UnivariateRealFunction testFunction = null;
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInitial() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double initial = 10.0; // initial is not between lowerBound and upperBound
        double lowerBound = -1.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double initial = 0.0;
        double lowerBound = 1.0; // lowerBound is greater than upperBound
        double upperBound = 0.0;

        UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction testFunction = new TestFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(testFunction, initial, lowerBound, upperBound);

        double a = bracketingValues[0];
        double b = bracketingValues[1];

        assertTrue("a is not less than b", a < b);
        assertTrue("function values of a and b have opposite signs", testFunction.value(a) * testFunction.value(b) < 0.0);
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;

        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(1.5, midpoint, 1e-8);
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 0.366497;
        }
    }

    private static class ThrowingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("FunctionEvaluationException thrown in test");
        }
    }

    private static class DivergingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2); // a squared function which diverges for negative x
        }
    }

}