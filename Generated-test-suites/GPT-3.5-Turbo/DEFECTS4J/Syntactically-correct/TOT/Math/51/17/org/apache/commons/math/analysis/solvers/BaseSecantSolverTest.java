import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidValue_ReturnsRoot() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);

        // Act
        double result = solver.solve(100, new MyFunction(), -1, 1);

        // Assert
        assertEquals(0, result, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidMaxEval_ThrowsIllegalArgumentException() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);

        // Act & Assert
        solver.solve(-1, new MyFunction(), -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidBounds_ThrowsIllegalArgumentException() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);

        // Act & Assert
        solver.solve(100, new MyFunction(), 1, -1);
    }

    @Test
    public void testSolve_WithAllowedLeftSide_ReturnsRootOnLeftSide() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);
        solver.setAllowed(AllowedSolution.LEFT_SIDE);

        // Act
        double result = solver.solve(100, new MyFunction(), -1, 1);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    public void testSolve_WithAllowedRightSide_ReturnsRootOnRightSide() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);
        solver.setAllowed(AllowedSolution.RIGHT_SIDE);

        // Act
        double result = solver.solve(100, new MyFunction(), -1, 1);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    public void testSolve_WithAllowedBelowSide_ReturnsRootBelow() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);
        solver.setAllowed(AllowedSolution.BELOW_SIDE);

        // Act
        double result = solver.solve(100, new MyFunction(), -1, 1);

        // Assert
        assertTrue(result <= 0);
    }

    @Test
    public void testSolve_WithAllowedAboveSide_ReturnsRootAbove() {
        // Arrange
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new MySecantSolver(absoluteAccuracy);
        solver.setAllowed(AllowedSolution.ABOVE_SIDE);

        // Act
        double result = solver.solve(100, new MyFunction(), -1, 1);

        // Assert
        assertTrue(result >= 0);
    }
}