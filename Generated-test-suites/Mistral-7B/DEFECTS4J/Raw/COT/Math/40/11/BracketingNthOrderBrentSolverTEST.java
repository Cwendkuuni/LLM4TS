package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @org.junit.Test
    public void testDefaultConstructor() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testCustomConstructorWithAbsoluteAccuracyAndMaximalOrder()
            throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(3, solver.getMaximalOrder());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testCustomConstructorWithMaximalOrderLowerThanTwo()
            throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @org.junit.Test
    public void testDoSolveWithCustomFunction() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        double root = solver.solve(100, f, -2, 2);
        assertEquals(0.5, root, 1e-6);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testDoSolveWithNoBracketing() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        solver.solve(100, f, -2, 2);
    }

    @org.junit.Test
    public void testDoSolveWithDifferentAllowedSolutions() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double root;

        // default is ANY_SIDE
        root = solver.solve(100, f, -3, 3);
        assertTrue(Math.abs(root) < 0.01);

        // set to LEFT_SIDE
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        root = solver.solve(100, f, -3, 3);
        assertTrue(Math.abs(root - Precision.PI_HALF) < 0.01);

        // set to RIGHT_SIDE
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        root = solver.solve(100, f, -3, 3);
        assertTrue(Math.abs(root + Precision.PI_HALF) < 0.01);

        // set to BELOW_SIDE
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        root = solver.solve(100, f, -3, 3);
        assertTrue(Math.abs(Math.sin(root) - 0.5) < 0.01);

        // set to ABOVE_SIDE
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        root = solver.solve(100, f, -3, 3);
        assertTrue(Math.abs(Math.sin(root) + 0.5) < 0.01);
    }

}