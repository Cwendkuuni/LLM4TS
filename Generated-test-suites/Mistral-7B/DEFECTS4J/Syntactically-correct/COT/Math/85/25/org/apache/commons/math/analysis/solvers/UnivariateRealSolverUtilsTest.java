package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double expectedResult = 0.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedResult, result, 1e-6);

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1));

        UnivariateRealFunction invalidFunction = null;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(invalidFunction, x0, x1));

        UnivariateRealFunction lowerBoundGreaterThanUpperBoundFunction = new PolynomialFunction(new double[]{1.0, 1.0, 0.0});
        double lowerBound = 2.0;
        double upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(lowerBoundGreaterThanUpperBoundFunction, lowerBound, upperBound));
    }

    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.01;
        double expectedResult = 0.5;
        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(expectedResult, result, absoluteAccuracy);

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy));

        UnivariateRealFunction invalidFunction = null;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(invalidFunction, x0, x1, absoluteAccuracy));

        UnivariateRealFunction lowerBoundGreaterThanUpperBoundFunction = new PolynomialFunction(new double[]{1.0, 1.0, 0.0});
        double lowerBound = 2.0;
        double upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(lowerBoundGreaterThanUpperBoundFunction, lowerBound, upperBound, absoluteAccuracy));

        UnivariateRealFunction functionWithInadequateAccuracy = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0e-20});
        x0 = 0.0;
        x1 = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(functionWithInadequateAccuracy, x0, x1, absoluteAccuracy));
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double[] expectedResult = {0.25, 0.75};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertEquals(expectedResult[0], result[0], 1e-6);
        assertEquals(expectedResult[1], result[1], 1e-6);

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound));

        UnivariateRealFunction invalidFunction = null;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(invalidFunction, initial, lowerBound, upperBound));

        UnivariateRealFunction lowerBoundGreaterThanUpperBoundFunction = new PolynomialFunction(new double[]{1.0, 1.0, 0.0});
        initial = 0.0;
        lowerBound = 2.0;
        upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(lowerBoundGreaterThanUpperBoundFunction, initial, lowerBound, upperBound));

        UnivariateRealFunction functionWithMaxIterationsReached = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(functionWithMaxIterationsReached, initial, lowerBound, upperBound, 0));

        UnivariateRealFunction functionWithInitialOutOfInterval = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        initial = -1.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(functionWithInitialOutOfInterval, initial, lowerBound, upperBound));

        UnivariateRealFunction functionWithLowerBoundGreaterThanUpperBound = new PolynomialFunction(new double[]{1.0, 1.0, 0.0});
        initial = 0.0;
        lowerBound = 2.0;
        upperBound = 1.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(functionWithLowerBoundGreaterThanUpperBound, initial, lowerBound, upperBound));
    }

    @org.junit.Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        int maximumIterations = 1;
        double[] expectedResult = {0.25, 0.75};
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(expectedResult[0], result[0], 1e-6);
        assertEquals(expectedResult[1], result[1], 1e-6);

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        maximumIterations = 0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations));

        UnivariateRealFunction invalidFunction = null;
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(invalidFunction, initial, lowerBound, upperBound, maximumIterations));

        UnivariateRealFunction lowerBoundGreaterThanUpperBoundFunction = new PolynomialFunction(new double[]{1.0, 1.0, 0.0});
        initial = 0.0;
        lowerBound = 2.0;
        upperBound = 1.0;
        maximumIterations = 1;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(lowerBoundGreaterThanUpperBoundFunction, initial, lowerBound, upperBound, maximumIterations));

        UnivariateRealFunction functionWithMaxIterationsReached = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 1.0;
        maximumIterations = 1;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(functionWithMaxIterationsReached, initial, lowerBound, upperBound, maximumIterations));
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 2.0;
        double b = 3.0;
        double expectedResult = 2.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedResult, result, 1e-6);

        a = 1.0;
        b = 2.0;
        expectedResult = 1.5;
        result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expectedResult, result, 1e-6);
    }
}