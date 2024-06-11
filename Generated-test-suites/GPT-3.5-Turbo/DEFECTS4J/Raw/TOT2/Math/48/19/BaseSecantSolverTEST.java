import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Typical use case
        double result = solver.solve(100, Math::cos, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.739085133, result, 1e-6);

        // Edge cases
        result = solver.solve(100, Math::cos, 0, Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, result, 1e-6);

        result = solver.solve(100, Math::cos, -Math.PI, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(-Math.PI, result, 1e-6);

        // Error scenarios
        boolean exceptionThrown = false;
        try {
            solver.solve(10, Math::cos, 0, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        } catch (ConvergenceException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}