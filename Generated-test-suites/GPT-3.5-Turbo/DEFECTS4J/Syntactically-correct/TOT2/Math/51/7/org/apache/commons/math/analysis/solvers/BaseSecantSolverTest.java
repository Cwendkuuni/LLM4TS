import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolveWithMaxEval() {
        // Test solving with maximum evaluation
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        // Test solving with a specific start value
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, x -> x * x - 4, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // Test solving with different allowed solutions
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, x -> x * x - 4, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        // Test solving with function value accuracy
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-9, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, x -> x * x - 4, 1, 3, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithInvalidBounds() {
        // Test solving with invalid bounds
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double solution = solver.solve(100, x -> x * x - 4, 3, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

}