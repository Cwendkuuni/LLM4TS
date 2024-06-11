package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    private static UnivariateFunction testFunction;

    static {
        testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.pow(x, 2) + 4 * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 8 * x + 4;
            }
        };
    }

    @org.junit.Test
    public void testConstructorDefault() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorCustom() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 10);
        assertEquals(10, solver.getMaximalOrder());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(0.001, 1);
    }

    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(testFunction, -3.0, 2.0);
        assertEquals(1.0, solution, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(testFunction, -3.0, 2.0, 0.5);
        assertEquals(0.5, solution, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(testFunction, -3.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(testFunction.value(solution) > 0);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(testFunction, 2.0, 2.0);
    }

    @org.junit.Test
    public void testSolveWithRelativeAccuracyAndAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-8);
        double solution = solver.solve(testFunction, -3.0, 2.0);
        assertEquals(1.0, solution, 1e-8);
    }

    @org.junit.Test
    public void testSolveWithFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-8, 1e-8);
        double solution = solver.solve(testFunction, -3.0, 2.0);
        assertEquals(1.0, solution, 1e-8);
    }

}