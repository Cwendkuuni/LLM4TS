package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test when the root is exactly at the min bound
        double root1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 100, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0, root1, 1e-6);

        // Test when the root is exactly at the max bound
        double root2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 100;
            }
        }, 0, 100, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(100, root2, 1e-6);

        // Test when the root is within the bounds
        double root3 = solver.solve(500, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 50;
            }
        }, 0, 100, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(50, root3, 1e-6);

        // Test when the root is outside the bounds
        double root4 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 150;
            }
        }, 0, 100, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, root4, 1e-6);
    }

    @Test
    public void testDoSolve_RegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test when the function value of the last approximation is too small
        double result1 = solver.doSolve();
        assertEquals(0, result1, 1e-6);

        // Test when the current interval is within the given accuracies
        double result2 = solver.doSolve();
        assertEquals(0, result2, 1e-6);
    }

    @Test
    public void testDoSolve_Illinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test when the function value of the last approximation is too small
        double result1 = solver.doSolve();
        assertEquals(0, result1, 1e-6);

        // Test when the current interval is within the given accuracies
        double result2 = solver.doSolve();
        assertEquals(0, result2, 1e-6);
    }

    @Test
    public void testDoSolve_Pegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test when the function value of the last approximation is too small
        double result1 = solver.doSolve();
        assertEquals(0, result1, 1e-6);

        // Test when the current interval is within the given accuracies
        double result2 = solver.doSolve();
        assertEquals(0, result2, 1e-6);
    }
}