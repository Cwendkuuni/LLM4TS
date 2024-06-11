package org.apache.commons.math.analysis.solvers;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver.AllowedSolution;

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
        assertEquals(1e-6, solver.getAbsoluteAccuracy());
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        assertEquals(1e-8, solver.getAbsoluteAccuracy());
        assertEquals(10, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-12, 10);
        assertEquals(1e-8, solver.getRelativeAccuracy());
        assertEquals(1e-12, solver.getAbsoluteAccuracy());
        assertEquals(10, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-12, 1e-15, 10);
        assertEquals(1e-8, solver.getRelativeAccuracy());
        assertEquals(1e-12, solver.getAbsoluteAccuracy());
        assertEquals(1e-15, solver.getFunctionValueAccuracy());
        assertEquals(10, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolve() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2);
        assertEquals(0, x, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolveWithStartValue() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2, 1);
        assertEquals(0, x, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolveWithAllowedSolution() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2, 1, AllowedSolution.LEFT_SIDE);
        assertEquals(-2, x, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolveWithAllowedSolutionBelow() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2, 1, BracketingNthOrderBrentSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(-2, x, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolveWithAllowedSolutionAbove() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2, 1, BracketingNthOrderBrentSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2, x, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double, AllowedSolution)}.
     * @throws NoBracketingException
     */
    @Test
    public void testSolveWithAllowedSolutionAnySide() throws NoBracketingException {
        UnivariateFunction f = f -> Math.pow(f.value(1d), 2) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double x = solver.solve(10, f, -2, 2, 1, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0, x, 1e-6);
    }

}