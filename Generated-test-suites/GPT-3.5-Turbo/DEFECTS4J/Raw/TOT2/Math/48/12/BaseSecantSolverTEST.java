import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new ConcreteSecantSolver(); // ConcreteSecantSolver is an implementation class that extends BaseSecantSolver
        double root = solver.solve(100, Math::sin, 0, Math.PI, AllowedSolution.ANY_SIDE);
        
        assertEquals(0, Math.sin(root), 1e-6);
    }

    @Test
    public void testSolve_Bracketing() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        double root = solver.solve(100, Math::cos, 0, 3 * Math.PI, AllowedSolution.ANY_SIDE);
        
        assertTrue(root >= 0 && root <= 2 * Math.PI);
    }

    @Test
    public void testSolve_EdgeCases() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        double root1 = solver.solve(100, Math::exp, -1, 1, AllowedSolution.ANY_SIDE);
        double root2 = solver.solve(100, Math::exp, -10, 10, AllowedSolution.ANY_SIDE);
        
        assertNotNull(root1);
        assertNotNull(root2);
    }

    @Test
    public void testSolve_ExceptionHandling() {
        BaseSecantSolver solver = new ConcreteSecantSolver();
        
        try {
            solver.solve(100, x -> {
                if (x == 0) {
                    throw new IllegalArgumentException("Division by zero");
                }
                return 1 / x;
            }, -1, 1, AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}