package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    /**
     * Test constructor with function.
     */
    @Test
    public void testBisectionSolverConstructorFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, 1.0, -1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver(f);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testBisectionSolverDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
    }

    /**
     * Test solve method with valid input.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveValidInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, new int[] {2, 1, 2});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with initial point in the interval.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveInitialPointInInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {-1.0, 3.0, -2.0}, new int[] {2, 1, 2});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 1.0;
        double result = solver.solve(f, -2.0, 2.0, 1.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots at the endpoints.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsAtEndpoints() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;
        double result = solver.solve(f, 0.0, 0.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots between the endpoints.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootBetweenEndpoints() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -2.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = -1.0;
        double result = solver.solve(f, 0.0, 2.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots at the boundaries of the interval.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsAtBoundaries() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -2.0, 2.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots at the midpoint of the interval.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootAtMidpoint() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -2.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = -1.0;
        double result = solver.solve(f, -3.0, 1.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with empty interval.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveEmptyInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, new int[] {2, 1, 2});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 0.0);
    }

    /**
     * Test solve method with infinite interval.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInfiniteInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, new int[] {2, 1, 2});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Test solve method with function that doesn't bracket the root.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionDoesntBracketRoot() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, new int[] {2, 1, 2});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 1.0);
    }

    /**
     * Test solve method with function that has roots with small differences.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsWithSmallDifferences() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -5.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = -2.0;
        double result = solver.solve(f, -3.0, 1.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots with large differences.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsWithLargeDifferences() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -5.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = -2.0;
        double result = solver.solve(f, -10.0, 10.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots with opposite signs.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsWithOppositeSigns() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, 4.0, -4.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 2.0;
        double result = solver.solve(f, -5.0, 5.0);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that has roots with small absolute values.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFunctionWithRootsWithSmallAbsoluteValues() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0E-6, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;
        double result = solver.solve(f, -1.0E-6, 1.0E-6);
        assertEquals("The expected result does not match the actual result", expectedResult, result, Precision.SAFE_EPSILON);
    }

    /**
     * Test solve method with function that is always positive.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionThatIsAlwaysPositive() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, 1.0, 1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solve method with function that is always negative.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionThatIsAlwaysNegative() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0, -1.0, -1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solve method with function that has a vertical asymptote.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionWithVerticalAsymptote() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0/10000.0, 1.0, 1.0/10000.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solve method with function that has a horizontal asymptote.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionWithHorizontalAsymptote() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1.0/10000.0, 1.0, 1.0/10000.0+1.0}, new int[] {3, 2, 1});
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }
}