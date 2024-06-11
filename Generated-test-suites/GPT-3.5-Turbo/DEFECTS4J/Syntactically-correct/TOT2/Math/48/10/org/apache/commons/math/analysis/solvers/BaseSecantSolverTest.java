import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithExactRoot() {
        // Test when the function value at the initial bounds is exact root
        double expectedRoot = 3.0;
        double tolerance = 1e-6;
        
        BaseSecantSolver solver = new BaseSecantSolver() {
            @Override
            protected double computeObjectiveValue(double x) {
                if (x == expectedRoot) {
                    return 0.0;
                }
                return super.computeObjectiveValue(x);
            }
        };

        double result = solver.solve(100, x -> x * x - 9, 2.0, 4.0, AllowedSolution.ANY_SIDE);

        assertEquals(expectedRoot, result, tolerance);
    }

    @Test
    public void testSolve_WithFunctionValueTooSmall() {
        // Test when function value at the last approximation is too small
        double expectedRoot = 2.0;
        double tolerance = 1e-6;

        BaseSecantSolver solver = new BaseSecantSolver() {
            @Override
            protected double computeObjectiveValue(double x) {
                return super.computeObjectiveValue(x) + 1e-10;
            }
        };

        double result = solver.solve(100, x -> x * x - 4, 1.0, 3.0, AllowedSolution.ANY_SIDE);

        assertEquals(expectedRoot, result, tolerance);
    }

    @Test
    public void testSolve_WithInvertedInterval() {
        // Test when the interval is inverted
        double expectedRoot = 2.5;
        double tolerance = 1e-6;

        BaseSecantSolver solver = new BaseSecantSolver() {
            @Override
            protected double computeObjectiveValue(double x) {
                return super.computeObjectiveValue(x);
            }
        };

        double result = solver.solve(100, x -> x * x - 6, 3.0, 2.0, AllowedSolution.ANY_SIDE);

        assertEquals(expectedRoot, result, tolerance);
    }

    @Test
    public void testSolve_ExceptionHandling() {
        // Test exception handling for convergence exception
        BaseSecantSolver solver = new BaseSecantSolver() {
            @Override
            protected double computeObjectiveValue(double x) {
                return super.computeObjectiveValue(x);
            }
        };

        try {
            solver.solve(5, x -> x * x - 9, 2.0, 4.0, AllowedSolution.ANY_SIDE);
            fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            // test passed
        }
    }
}