import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithExactRoot_ReturnsRoot() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return 0.0; // Simulating exact root
            }
        };

        // When
        double root = solver.solve(100, x -> x * x - 4, 1, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Then
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithToleranceReached_ReturnsApproximateRoot() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        // When
        double root = solver.solve(100, x -> x * x - 4, 1, 2.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Then
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithMaxEvaluationsReached_ThrowsMathIllegalArgumentException() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        // When/Then
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(1, x -> x * x - 4, 1, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE));
    }
}