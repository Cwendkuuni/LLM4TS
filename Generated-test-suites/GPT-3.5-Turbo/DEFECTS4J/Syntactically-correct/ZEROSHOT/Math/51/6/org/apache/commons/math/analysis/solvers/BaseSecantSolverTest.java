import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: f(x) = x^2 - 4, expected root = 2.0
        double result = solver.solve(100, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test case 2: f(x) = sin(x), expected root = PI
        result = solver.solve(100, Math::sin, 2, 4, Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, result, 1e-6);

        // Test case 3: f(x) = x^3 - x - 1, expected root = 1.324717957244746
        result = solver.solve(100, x -> x * x * x - x - 1, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.324717957244746, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test case 1: f(x) = x^2 - 4, expected root = 2.0
        double result = solver.solve(10, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test case 2: f(x) = sin(x), expected root = PI
        result = solver.solve(10, Math::sin, 2, 4, Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, result, 1e-6);

        // Test case 3: f(x) = x^3 - x - 1, expected root = 1.324717957244746
        result = solver.solve(10, x -> x * x * x - x - 1, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.324717957244746, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test case 1: f(x) = x^2 - 4, expected root = 2.0
        double result = solver.solve(100, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test case 2: f(x) = sin(x), expected root = PI
        result = solver.solve(100, Math::sin, 2, 4, Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, result, 1e-6);

        // Test case 3: f(x) = x^3 - x - 1, expected root = 1.324717957244746
        result = solver.solve(100, x -> x * x * x - x - 1, 1, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.324717957244746, result, 1e-6);
    }
}