package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver.AllowedSolution;

public class BracketingNthOrderBrentSolverTest {

    @org.junit.Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-7, 4);
        assertEquals(1e-7, solver1.getAbsoluteAccuracy(), 1e-11);
        assertEquals(4, solver1.getMaximalOrder());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-7, 1);
    }

    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-7, 4);
        assertEquals(4, solver1.getMaximalOrder());
    }

    @org.junit.Test
    public void testSolveSimpleCase() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result = solver.solve(10, f, -2, 2);
        assertEquals(0, result, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithCustomAccuracy() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double result = solver.solve(10, f, -10, 10);
        assertEquals(0, result, 1e-9);
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testNoBracketingException() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.solve(10, f, 1, 1);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketingException2() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        solver.solve(10, f, -2, 1);
    }

    @org.junit.Test
    public void testAllowedSolution() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result = solver.solve(10, f, -10, 10, AllowedSolution.BELOW_SIDE);
        assertTrue(Math.sin(result) < 0);

        result = solver.solve(10, f, -10, 10, AllowedSolution.ABOVE_SIDE);
        assertTrue(Math.sin(result) > 0);
    }

    @org.junit.Test
    public void testEdgeCases() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.abs(x - 3);
            }
        };

        double result = solver.solve(10, f, 2, 4);
        assertEquals(3, result, 1e-6);

        result = solver.solve(10, f, 4, 2);
        assertEquals(3, result, 1e-6);

        result = solver.solve(10, f, 1, 3);
        assertEquals(3, result, 1e-6);

        result = solver.solve(10, f, 3, 1);
        assertEquals(3, result, 1e-6);
    }

}