package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver.AllowedSolution;

public class BracketingNthOrderBrentSolverTest {

    @org.junit.Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
        assertNull(solver.getFunction());
    }

    @org.junit.Test
    public void testConstructorWithAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(7, solver.getMaximalOrder());
        assertNull(solver.getFunction());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowerMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(7, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testSolveWithMinMaxAndFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-8);
    }

    @org.junit.Test
    public void testSolveWithMinMaxAndStartValueAndFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-8);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> Math.sin(x) - x;
        solver.solve(100, f, -3, 3, AllowedSolution.ANY_SIDE);
    }

    @org.junit.Test
    public void testAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.BELOW_SIDE);
        assertEquals(-Math.sqrt(2), result, 1e-8);
    }

    @org.junit.Test
    public void testAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.LEFT_SIDE);
        assertEquals(-Math.sqrt(2), result, 1e-8);
    }

    @org.junit.Test
    public void testAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.RIGHT_SIDE);
        assertEquals(Math.sqrt(2), result, 1e-8);
    }

    @org.junit.Test
    public void testAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.ABOVE_SIDE);
        assertEquals(-Math.sqrt(2), result, 1e-8);
    }

    @org.junit.Test
    public void testAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        UnivariateFunction f = (x) -> x * x - 2;
        double result = solver.solve(100, f, -3, 3, AllowedSolution.BELOW_SIDE);
        assertEquals(Math.sqrt(2), result, 1e-8);
    }

}