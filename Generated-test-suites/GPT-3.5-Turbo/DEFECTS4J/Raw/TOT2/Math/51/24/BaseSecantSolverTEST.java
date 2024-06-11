import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithExactRoot() {
        // Create instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Define function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        // Solve for root
        double root = solver.solve(100, function, 4, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Check if root is correct
        assertEquals(5.0, root, 0.0);
    }

    @Test
    public void testSolve_WithUnderApproximation() {
        // Create instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Define function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x*x - 9;
            }
        };

        // Solve for root
        double root = solver.solve(100, function, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Check if root is correct
        assertEquals(3.0, root, 0.0);
    }

    @Test
    public void testSolve_WithInvalidBounds_ExceptionThrown() {
        // Create instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Define function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        try {
            // Solve for root with invalid bounds
            double root = solver.solve(100, function, 6, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Exception not thrown for invalid bounds");
        } catch (MathIllegalArgumentException e) {
            // Exception should be thrown
            assertTrue(true);
        }
    }
}