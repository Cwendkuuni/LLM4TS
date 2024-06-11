import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidInput_ReturnsRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // x^2 - 4 = 0, root at x = 2
            }
        };
        double min = 1.0;
        double max = 3.0;
        
        // Act
        double result = solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Assert
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidInput_ThrowsException() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);        
        UnivariateRealFunction f = null;
        double min = 1.0;
        double max = 3.0;
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            solver.solve(100, f, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        });
    }

    // Add more test cases for other methods and edge cases

}