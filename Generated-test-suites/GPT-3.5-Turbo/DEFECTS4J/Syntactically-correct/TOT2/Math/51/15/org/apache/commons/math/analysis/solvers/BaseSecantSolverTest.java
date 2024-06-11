import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithRootInInterval_ShouldReturnCorrectRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double expectedRoot = 2.0;
        
        // Act
        double root = solver.solve(100, x -> x - 2, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Assert
        assertEquals(expectedRoot, root, 1e-6);
    }

    @Test
    public void testSolve_WithExactRootInInterval_ShouldReturnExactRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double expectedRoot = 5.0;
        
        // Act
        double root = solver.solve(100, x -> x - 5, 4, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Assert
        assertEquals(expectedRoot, root, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueTooSmall_ShouldReturnClosestRoot() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double expectedRoot = 10.0;
        
        // Act
        double root = solver.solve(100, x -> 2 * x - 20, 9, 11, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        
        // Assert
        assertEquals(expectedRoot, root, 1e-6);
    }

    @Test
    public void testSolve_WithMaxEvaluationsReached_ShouldThrowMathInternalError() {
        // Arrange
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Act & Assert
        assertThrows(MathInternalError.class, () -> solver.solve(1, x -> x - 2, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE));
    }

}