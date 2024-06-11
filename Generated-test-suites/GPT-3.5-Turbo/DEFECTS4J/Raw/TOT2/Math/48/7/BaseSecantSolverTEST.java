import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Typical use case
        BaseSecantSolver solver = new SubclassOfBaseSecantSolver();
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Function for testing
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
        
        // Edge cases
        // Test solving when the function value is zero at the initial boundaries
        double resultZeroBoundaries = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0; // Constant function
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, resultZeroBoundaries, 1e-6);
        
        // Test for extremum values
        double resultExtremeValues = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 16; // Quadratic function
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(4.0, resultExtremeValues, 1e-6);
        
        // Error scenarios
        // Test for convergence exception
        try {
            solver.solve(100, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x; // Function that won't converge
                }
            }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Convergence exception not thrown");
        } catch (ConvergenceException e) {
            // Exception expected
        }
    }
}