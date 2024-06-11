package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithSimpleFunction() {
        UnivariateFunction func = f -> Math.pow(f, 2) - Math.PI * Math.sin(f);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", Precision.round(Math.PI, 6), root, 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithFunctionWithoutBracketing() {
        UnivariateFunction func = f -> Math.pow(f, 2) - Math.PI * Math.sin(f - 1);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, func, -5.0, 4.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithFunctionWithoutBracketingException() {
        UnivariateFunction func = f -> Math.pow(f, 2) - Math.PI * Math.sin(f - 1);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, func, -5.0, 4.0, AllowedSolution.LEFT_SIDE);
    }

    @Test
    public void testSolverWithFunctionWithManyRoots() {
        UnivariateFunction func = f -> Math.pow(f, 2) - 4 * f + 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root1 = solver.solve(100, func, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root1 not found", -1.0, root1, 1e-6);
        double root2 = solver.solve(100, func, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root2 not found", 1.0, root2, 1e-6);
    }

    @Test
    public void testSolverWithFunctionWithLimitCycle() {
        UnivariateFunction func = f -> Math.log(Math.abs(f)) / Math.log(2);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        try {
            solver.solve(100, func, 0.5, 1.0, AllowedSolution.ANY_SIDE);
            fail("Limit cycle should not be found");
        } catch (MaxCountExceededException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSolverWithFunctionWithDivergence() {
        UnivariateFunction func = f -> 1 / f;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        try {
            solver.solve(100, func, -1.0, 0.0, AllowedSolution.ANY_SIDE);
            fail("Divergence should not be found");
        } catch (NumberIsTooSmallException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSolverWithFunctionWithNaN() {
        UnivariateFunction func = f -> Math.sqrt(f * (f - 2));
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        try {
            solver.solve(100, func, 1.0, 2.0, AllowedSolution.ANY_SIDE);
            fail("NaN should not be found");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSolverWithFunctionWithInfiniteValue() {
        UnivariateFunction func = f -> Math.exp(f) - Math.exp(2.0);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        try {
            solver.solve(100, func, -100.0, -50.0, AllowedSolution.ANY_SIDE);
            fail("Infinite value should not be found");
        } catch (NumberIsTooSmallException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSolverWithFunctionWithLargeValue() {
        UnivariateFunction func = f -> Math.pow(f, 1000) - 1;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        try {
            double root = solver.solve(100, func, -1000.0, 1000.0, AllowedSolution.ANY_SIDE);
            fail("Large value should not be found");
        } catch (NumberIsTooSmallException e) {
            // Expected behavior
        }
    }

}