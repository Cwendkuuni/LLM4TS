package org.apache.commons.math.analysis.solvers;

import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        // Test case 1
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver1.solve(100, f1, 0, 10);
        assertEquals(2.000001, result1, 1e-6);

        // Test case 2
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result2 = solver2.solve(100, f2, 0, 10);
        assertEquals(3.141592, result2, 1e-6);

        // Test case 3
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        double result3 = solver3.solve(100, f3, 0, 5);
        assertEquals(0.693146, result3, 1e-6);
    }
}