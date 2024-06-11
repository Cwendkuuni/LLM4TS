import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Typical use case
        double result = solver.solve(100, f -> f.value(2.0), 1.0, 3.0);
        assertEquals(2.0, result, 1e-6);
        
        // Edge case: exact root at initial bounds
        result = solver.solve(100, f -> f.value(0.0), -1.0, 1.0);
        assertEquals(0.0, result, 1e-6);
        
        // Error scenario: function value accuracy not met
        result = solver.solve(100, f -> f.value(10.0), 9.0, 11.0);
        assertEquals(10.0, result, 1e-6);
    }
}