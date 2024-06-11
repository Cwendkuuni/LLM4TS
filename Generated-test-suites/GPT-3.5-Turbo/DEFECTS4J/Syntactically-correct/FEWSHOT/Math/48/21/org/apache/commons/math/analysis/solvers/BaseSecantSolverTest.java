package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double actual = solver.solve(100, f, 0, 10);
        double expected = 2.0;
        assertEquals(expected, actual, 1e-6);
        
        solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        actual = solver.solve(100, f, 0, 10);
        assertEquals(expected, actual, 1e-6);
        
        solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        actual = solver.solve(100, f, 0, 10);
        assertEquals(expected, actual, 1e-6);
    }
}