package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(f, 2) - 4.0;
        double result = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-6);

        solver = new BracketingNthOrderBrentSolver(0.001, 10);
        result = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-9);

        function = f -> Math.exp(f) - 2.718281828;
        result = solver.solve(100, function, -3.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-6);

        solver = new BracketingNthOrderBrentSolver(0.001, 10, 1e-8, 10);
        result = solver.solve(100, function, -3.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-9);

        function = f -> Math.sqrt(f + 1) - 2.0;
        result = solver.solve(100, function, -3.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-2.41421356237, result, 1e-6);

        solver = new BracketingNthOrderBrentSolver(0.001, 10, 1e-8, 10);
        result = solver.solve(100, function, -3.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-2.41421377493, result, 1e-9);

        function = f -> Math.log(f) - Math.log(2.718281828);
        result = solver.solve(100, function, -3.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, result, 1e-6);

        solver = new BracketingNthOrderBrentSolver(0.001, 10, 1e-8, 10);
        result = solver.solve(100, function, -3.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, result, 1e-9);
    }
}