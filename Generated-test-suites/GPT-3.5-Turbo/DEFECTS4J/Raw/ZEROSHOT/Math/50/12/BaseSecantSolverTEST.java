import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test case 1
        double result1 = solver.solve(100, new TestFunction(), 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result1, 1e-6);
        
        // Test case 2
        double result2 = solver.solve(100, new TestFunction(), -10, 10, 0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, result2, 1e-6);
        
        // Test case 3
        double result3 = solver.solve(100, new TestFunction(), -10, 10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(5.0, result3, 1e-6);
        
        // Test case 4
        double result4 = solver.solve(100, new TestFunction(), -10, 10, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(4.0, result4, 1e-6);
        
        // Test case 5
        double result5 = solver.solve(100, new TestFunction(), -10, 10, 2, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(3.0, result5, 1e-6);
    }
    
    private class TestFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return x * x - 4;
        }
    }
}