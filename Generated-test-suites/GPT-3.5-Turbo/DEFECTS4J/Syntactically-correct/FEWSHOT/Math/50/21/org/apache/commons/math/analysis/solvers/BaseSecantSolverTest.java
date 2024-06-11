package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, f, 0, 10);
        assertEquals(2, solution, 1e-6);

        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solution = solver.solve(100, f, 0, 10);
        assertEquals(2, solution, 1e-6);

        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solution = solver.solve(100, f, 0, 10);
        assertEquals(2, solution, 1e-6);
    }
}