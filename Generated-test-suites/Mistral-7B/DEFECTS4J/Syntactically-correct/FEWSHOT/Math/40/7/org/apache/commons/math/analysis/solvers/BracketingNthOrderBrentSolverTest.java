package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @SuppressWarnings("unchecked")
    private static UnivariateFunction func = new UnivariateFunction() {
        public double value(double x) {
            return Math.pow(x, 2) - 3 * x + 2;
        }

        public double derivative(double x) {
            return 2 * x - 3;
        }
    };

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithMaximalOrderBelowTwo() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithRelativeAccuracyBelowZero() {
        new BracketingNthOrderBrentSolver(-1e-6, 1e-6, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithAbsoluteAccuracyBelowZero() {
        new BracketingNthOrderBrentSolver(1e-7, -1e-6, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithFunctionValueAccuracyBelowZero() {
        new BracketingNthOrderBrentSolver(1e-6, 1e-6, 1e-7, 5);
    }

    @Test
    public void testSolveWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolveWithCustomValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-8);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.5, 5.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithUnbracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, func, 2.5, 2.5, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testAllowedSolutionAnySide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root1 = solver.solve(100, func, -5.5, 5.5, AllowedSolution.ANY_SIDE);
        double root2 = solver.solve(100, func, -5.5, 5.5, AllowedSolution.LEFT_SIDE);
        double root3 = solver.solve(100, func, -5.5, 5.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, root1, 1e-6);
        assertEquals(-1.0, root2, 1e-6);
        assertEquals(1.0, root3, 1e-6);
    }

    @Test
    public void testAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.5, 5.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

}