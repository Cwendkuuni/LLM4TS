import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Define a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        
        // Test with initial values
        double solution = solver.solve(100, f, 0, 5, 2, AllowedSolution.ANY_SIDE);
        assertEquals(2, solution, 1e-6);
        
        // Test with different allowed solutions
        solution = solver.solve(100, f, 0, 5, 2, AllowedSolution.LEFT_SIDE);
        assertEquals(2, solution, 1e-6);
        
        solution = solver.solve(100, f, 0, 5, 2, AllowedSolution.RIGHT_SIDE);
        assertEquals(2, solution, 1e-6);
        
        solution = solver.solve(100, f, 0, 5, 2, AllowedSolution.BELOW_SIDE);
        assertEquals(2, solution, 1e-6);
        
        solution = solver.solve(100, f, 0, 5, 2, AllowedSolution.ABOVE_SIDE);
        assertEquals(2, solution, 1e-6);
    }
    
    @Test
    public void testDoSolve() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Define a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 9; // f(x) = x^2 - 9
            }
        };
        
        // Test with initial values
        solver.setMin(0);
        solver.setMax(10);
        double solution = solver.doSolve();
        assertEquals(3, solution, 1e-6);
    }
}