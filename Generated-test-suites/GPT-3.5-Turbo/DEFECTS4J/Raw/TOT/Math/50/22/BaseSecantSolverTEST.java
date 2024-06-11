import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;
    
    @Test
    public void testSolve_QuadraticFunction() {
        // Create a quadratic function: f(x) = x^2 + 2x + 1
        UnivariateRealFunction f = x -> x * x + 2 * x + 1;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, -10, 10);

        // Check if the result is close to the expected root (-1)
        assertEquals(-1, result, EPSILON);
    }

    @Test
    public void testSolve_ZeroFunction() {
        // Create a constant zero function: f(x) = 0
        UnivariateRealFunction f = x -> 0;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, -10, 10);

        // Check if the result is any value between -10 and 10
        assertTrue(result >= -10 && result <= 10);
    }

    @Test
    public void testSolve_ExponentialFunction() {
        // Create an exponential function: f(x) = e^x - 1
        UnivariateRealFunction f = x -> Math.exp(x) - 1;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, f, -10, 10);

        // Check if the result is close to the expected root (0)
        assertEquals(0, result, EPSILON);
    }

    @Test
    public void testSolve_SingularFunction() {
        // Create a singular function: f(x) = 1 / x
        UnivariateRealFunction f = x -> 1 / x;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, -10, 10);

        // Check if the result is any value between -10 and 10 excluding 0
        assertTrue(result >= -10 && result < 0);
        assertTrue(result > 0 && result <= 10);
        assertNotEquals(0, result, EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSolve_InvalidBounds() {
        // Create a linear function: f(x) = x
        UnivariateRealFunction f = x -> x;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, 10, -10);
        
        // Should throw an IllegalArgumentException due to invalid bounds
    }

    @Test(expected=MathInternalError.class)
    public void testSolve_InvalidMethod() {
        // Create a linear function: f(x) = x
        UnivariateRealFunction f = x -> x;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        // Change the solver's method to an invalid one
        solver.method = null;

        double result = solver.solve(100, f, -10, 10);
        
        // Should throw a MathInternalError due to invalid method
    }

    @Test(timeout=1000)
    public void testSolve_LongRunningFunction() {
        // Create a long running function: f(x) = x^2
        UnivariateRealFunction f = x -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return x * x;
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, -10, 10);
        
        // Check if the result is close to the expected root (0)
        assertEquals(0, result, EPSILON);
    }

}