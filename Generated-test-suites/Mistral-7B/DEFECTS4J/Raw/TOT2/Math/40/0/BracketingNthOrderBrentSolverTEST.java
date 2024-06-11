package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;

import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for
     * {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver =
                new BracketingNthOrderBrentSolver(1e-9, 3);
        assertEquals(3, solver.getMaximalOrder());
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
    }

    /**
     * Test method for
     * {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelAccuracyAndAbsAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver =
                new BracketingNthOrderBrentSolver(1e-8, 1e-9, 3);
        assertEquals(3, solver.getMaximalOrder());
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-8, solver.getRelativeAccuracy(), 1e-11);
    }

    /**
     * Test method for
     * {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelAccuracyAbsAccuracyFunctionValueAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver =
                new BracketingNthOrderBrentSolver(1e-7, 1e-9, 1e-10, 4);
        assertEquals(4, solver.getMaximalOrder());
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 1e-11);
        assertEquals(1e-10, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4}, new double[] {1, 16});
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(root, solver.solve(100, f, 1.0, 5.0), 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4}, new double[] {1, 16});
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(root, solver.solve(100, f, 1.0, 5.0, 3.0), 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 4}, new double[] {1, 16});
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(root, solver.solve(100, f, 1.0, 5.0, 3.0, AllowedSolution.ANY_SIDE), 1e-9);
        assertFalse(solver.solve(100, f, 1.0, 5.0, 3.0, AllowedSolution.LEFT_SIDE).isNaN());
        assertFalse(solver.solve(100, f, 1.0, 5.0, 3.0, AllowedSolution.RIGHT_SIDE).isNaN());
        assertFalse(solver.solve(100, f, 1.0, 5.0, 3.0, AllowedSolution.BELOW_SIDE).isNaN());
        assertFalse(solver.solve(100, f, 1.0, 5.0, 3.0, AllowedSolution.ABOVE_SIDE).isNaN());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * <p>
     * Check that the returned value is the one expected,
     * depending on the {@link AllowedSolution} parameter.
     * </p>
     */
    @Test
    public void testSolveWithAllowedSolutionAndEdgeCases() {
        final UnivariateFunction rootFindFunction = new PolynomialFunction(new double[] {-1, 3}, new double[] {1, 9});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Test with allowed solution: LEFT_SIDE
        double rootLeft = 1.0;
        double result = solver.solve(100, rootFindFunction, 0.0, 3.0, rootLeft, AllowedSolution.LEFT_SIDE);
        assertEquals(result, rootLeft, 1e-9);

        // Test with allowed solution: RIGHT_SIDE
        double rootRight = 3.0;
        result = solver.solve(100, rootFindFunction, 0.0, 3.0, rootRight, AllowedSolution.RIGHT_SIDE);
        assertEquals(result, rootRight, 1e-9);

        // Test with allowed solution: BELOW_SIDE
        double rootBelow = 1.5;
        result = solver.solve(100, rootFindFunction, 0.0, 3.0, rootBelow, AllowedSolution.BELOW_SIDE);
        assertEquals(result, rootBelow, 1e-9);

        // Test with allowed solution: ABOVE_SIDE
        double rootAbove = 3.5;
        result = solver.solve(100, rootFindFunction, 0.0, 3.0, rootAbove, AllowedSolution.ABOVE_SIDE);
        assertEquals(result, rootAbove, 1e-9);

        // Test with allowed solution: ANY_SIDE
        double anySideResult = solver.solve(100, rootFindFunction, 0.0, 3.0, rootLeft, AllowedSolution.ANY_SIDE);
        assertEquals(anySideResult, rootLeft, 1e-9);
        anySideResult = solver.solve(100, rootFindFunction, 0.0, 3.0, rootRight, AllowedSolution.ANY_SIDE);
        assertEquals(anySideResult, rootRight, 1e-9);
        anySideResult = solver.solve(100, rootFindFunction, 0.0, 3.0, rootBelow, AllowedSolution.ANY_SIDE);
        assertEquals(anySideResult, rootBelow, 1e-9);
        anySideResult = solver.solve(100, rootFindFunction, 0.0, 3.0, rootAbove, AllowedSolution.ANY_SIDE);
        assertEquals(anySideResult, rootAbove, 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with function that
     * cannot bracket root.
     */
    @Test
    public void testSolveWithCannotBracket() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 1}, new double[] {1, 1});
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, 0.0, 1.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with negative order.
     */
    @Test
    public void testSolveWithNegativeOrder() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 3}, new double[] {1, 9});
        assertThrows(NumberIsTooSmallException.class,
                () -> solver.solve(100, f, 0.0, 3.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with very small order.
     */
    @Test
    public void testSolveWithVerySmallOrder() {
        final UnivariateFunction f = new PolynomialFunction(new double[] {-1, 3}, new double[] {1, 9});
        assertThrows(NumberIsTooSmallException.class,
                () -> solver.solve(100, f, 0.0, 3.0, 1.0, AllowedSolution.ANY_SIDE));
    }

}