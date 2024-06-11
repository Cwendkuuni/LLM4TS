package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /** Test function for testing the solver. */
    private static final UnivariateFunction TEST_FUNCTION = new PolynomialFunction(x -> Math.pow(x, 3) - 4 * x * x + 2);

    /**
     * Tests {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()} with default accuracy and maximal order.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)} with given accuracy and maximal order.
     */
    @Test
    public void testConstructorWithAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 4);
        assertEquals(1e-4, solver.getAbsoluteAccuracy(), 0);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)} with given relative accuracy, absolute accuracy, and maximal order.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 5);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)} with given relative accuracy, absolute accuracy, function value accuracy, and maximal order.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 0);
        assertEquals(6, solver.getMaximalOrder());
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#getMaximalOrder()} returns the maximal order.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        assertEquals(6, solver.getMaximalOrder());
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} with given maximum number of evaluations, function, minimum, and maximum.
     */
    @Test
    public void testSolveWithMaxEvaluations() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -2.0, 2.0);
        assertEquals(0.5, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} with given maximum number of evaluations, function, minimum, maximum, and start value.
     */
    @Test
    public void testSolveWithMaxEvaluationsAndStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -2.0, 2.0, 1.0);
        assertEquals(0.5, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with given maximum number of evaluations, function, minimum, maximum, start value, and allowed solution.
     */
    @Test
    public void testSolveWithMaxEvaluationsAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Math.sqrt(2.0) / 2.0, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with given maximum number of evaluations, function, minimum, maximum, start value, and allowed solution.
     */
    @Test
    public void testSolveWithMaxEvaluationsAllowedSolutionAndStartValueBelowRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -3.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Math.sqrt(2.0) / 2.0, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with given maximum number of evaluations, function, minimum, maximum, start value, and allowed solution.
     */
    @Test
    public void testSolveWithMaxEvaluationsAllowedSolutionAndStartValueAboveRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -2.0, 2.0, 3.0, AllowedSolution.LEFT_SIDE);
        assertEquals(Math.sqrt(2.0) / 2.0, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with given maximum number of evaluations, function, minimum, maximum, start value, and allowed solution.
     */
    @Test
    public void testSolveWithMaxEvaluationsAllowedSolutionAndStartValueAtRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        double root = solver.solve(10, TEST_FUNCTION, -2.0, 2.0, 0.5, AllowedSolution.LEFT_SIDE);
        assertEquals(0.5, root, 1e-5);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} with given maximum number of evaluations, function, minimum, and maximum.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithoutBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, 6);
        solver.solve(10, TEST_FUNCTION, 0.0, 0.0);
    }

    /**
     * Tests {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with given maximum number of evaluations, function, minimum, maximum, start value, and allowed solution.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-6, 1e-7, -2);
    }
}