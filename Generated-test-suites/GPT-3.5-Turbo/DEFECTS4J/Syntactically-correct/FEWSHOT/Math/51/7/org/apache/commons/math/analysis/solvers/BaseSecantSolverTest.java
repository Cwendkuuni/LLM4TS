package org.apache.commons.math.analysis.solvers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        solver.solve(100, (x) -> x * x - 4, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solver.getResult(), 0.0);
    }

    @Test
    public void testSolveWithRelativeAndAbsoluteError() {
        solver = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(100, (x) -> x * x - 4, -10, 10);
        assertEquals(2.0, solver.getResult(), 0.0);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        solver = new BaseSecantSolver(1e-6, 1e-8, 1e-10, BaseSecantSolver.Method.PEGASUS);
        solver.solve(100, (x) -> x * x - 4, -10, 10);
        assertEquals(2.0, solver.getResult(), 0.0);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        solver.solve(100, (x) -> x * x - 4, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, solver.getResult(), 0.0);
    }
}