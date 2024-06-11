import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);
        
        // Test with a function that has a root at x = 2
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        double result = solver.solve(100, f, 0, 4, 1);
        assertEquals(2, result, 1e-6);
        
        // Test with a function that has no root in the specified interval
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        
        try {
            result = solver.solve(100, f, 0, 4, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}