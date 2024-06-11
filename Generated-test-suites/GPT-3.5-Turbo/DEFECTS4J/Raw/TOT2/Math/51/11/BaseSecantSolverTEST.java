import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_DefaultConstructor_AnySide() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_NoRoot_Exception() {
        BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(100, x -> x + 5, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // success
        }
    }

    // Add more test cases for other methods as needed

}