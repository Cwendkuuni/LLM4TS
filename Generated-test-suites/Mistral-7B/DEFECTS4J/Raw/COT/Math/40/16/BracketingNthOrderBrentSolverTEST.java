package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        double relAccuracy = 1e-4;
        double absAccuracy = 1e-7;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relAccuracy, absAccuracy, maximalOrder);
        assertEquals(relAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#doSolve()}.
     * @throws MathException if solver fails
     */
    @Test
    public void testSolve() throws MathException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 3);
        UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 4 * x + 3);
        double result = solver.solve(f, -3.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, result, 1e-7);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with
     * custom start value.
     * @throws MathException if solver fails
     */
    @Test
    public void testSolveWithStartValue() throws MathException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 3);
        UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 4 * x + 3);
        double result = solver.solve(f, -3.0, 2.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, result, 1e-7);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} with invalid input.
     * @throws MathException if solver fails
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws MathException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 3);
        UnivariateFunction f = new PolynomialFunction(x -> x * x - 4 * x + 3);
        solver.solve(f, -3.0, 1.0);
    }

    /**
     * Test {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, int)} with invalid input.
     * @throws MathException if solver fails
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws MathException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 3);
        UnivariateFunction f = new PolynomialFunction(x -> x * x * x - 4 * x + 3);
        solver.solve(f, -3.0, 2.0, 1);
    }

}