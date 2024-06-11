import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidFunction_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, x -> x - 5, 3, 7);

        // Assert
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolve_WithFunctionHavingExactRootAtBounds_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, x -> x - 5, 5, 7);

        // Assert
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolve_WithFunctionHavingExactRootInInitialBound_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, x -> x - 5, 3, 8);

        // Assert
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolve_WithInvalidMaxEval_ThrowsMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act & Assert
        assertThrows(MathInternalError.class, () -> solver.solve(-1, x -> x - 5, 3, 7));
    }

    @Test
    public void testSolve_WithInvalidStartValue_ThrowsMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act & Assert
        assertThrows(MathInternalError.class, () -> solver.solve(100, x -> x - 5, 3, 7, 8));
    }

}