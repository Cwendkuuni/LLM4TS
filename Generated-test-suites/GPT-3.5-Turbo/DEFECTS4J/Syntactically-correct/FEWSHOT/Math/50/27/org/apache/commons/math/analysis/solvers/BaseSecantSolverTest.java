package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(2.0, solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE), 1e-6);
        assertEquals(2.0, solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE), 1e-6);
        assertNull(solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE));
        assertNull(solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE));
    }
}