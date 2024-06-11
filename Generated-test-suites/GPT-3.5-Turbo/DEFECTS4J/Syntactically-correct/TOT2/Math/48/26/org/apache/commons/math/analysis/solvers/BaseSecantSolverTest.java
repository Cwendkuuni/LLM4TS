import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Typical use case
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, 1e-6);

        // Edge case: Exact root at bounds
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, result, 1e-6);

        // Error scenario: Function value not changing signs
        try {
            solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return 5.0;
                }
            }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // As expected
        }
    }

}