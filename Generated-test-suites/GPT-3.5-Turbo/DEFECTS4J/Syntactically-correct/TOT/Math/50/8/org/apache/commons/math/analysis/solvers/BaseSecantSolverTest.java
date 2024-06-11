import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenRootWithinBounds_ShouldReturnRoot() {
        // Arrange
        double expected = 3.14159265;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), 0, 6, AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testSolve_WhenRootIsAtBound_ShouldReturnBound() {
        // Arrange
        double expected = 0;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), -1, 0, AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsLeftSideAndRootIsOnRight_ShouldReturnNextBestRoot() {
        // Arrange
        double expected = 3.14159265;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), -6, 0, AllowedSolution.LEFT_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testSolve_WhenAllowedSolutionIsRightSideAndRootIsOnLeft_ShouldReturnNextBestRoot() {
        // Arrange
        double expected = -3.14159265;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), 0, 6, AllowedSolution.RIGHT_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testSolve_WhenFunctionValueTooSmall_ShouldReturnApproximation() {
        // Arrange
        double expected = 3.14159265;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), 0, 6, AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testSolve_WhenIntervalWithinAccuracies_ShouldReturnApproximation() {
        // Arrange
        double expected = 3.14159265;
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), 0, 6, AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(expected, result, 0.00001);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_WhenUndefinedMethod_ShouldThrowMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver();

        // Act
        double result = solver.solve(100, Math.sin(x), 0, 6, AllowedSolution.ANY_SIDE);

        // Assert
        // Exception expected
    }
}