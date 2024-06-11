package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Test with any side allowed solution
        double solution = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 0.000001);
        // Test with left side allowed solution
        solution = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, solution, 0.000001);
        // Test with right side allowed solution
        solution = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(-2.0, solution, 0.000001);
        // Test with below side allowed solution
        solution = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, solution, 0.000001);
        // Test with above side allowed solution
        solution = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(-2.0, solution, 0.000001);
    }

    @Test
    public void testSolveWithCustomAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-10, 1e-12, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };

        // Test with any side allowed solution
        double solution = solver.solve(100, function, 0, FastMath.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.57079633, solution, 0.000001);
        // Test with left side allowed solution
        solution = solver.solve(100, function, 0, FastMath.PI, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(1.57079633, solution, 0.000001);
        // Test with right side allowed solution
        solution = solver.solve(100, function, 0, FastMath.PI, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(4.71238898, solution, 0.000001);
        // Test with below side allowed solution
        solution = solver.solve(100, function, 0, FastMath.PI, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.57079633, solution, 0.000001);
        // Test with above side allowed solution
        solution = solver.solve(100, function, 0, FastMath.PI, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(4.71238898, solution, 0.000001);
    }
}