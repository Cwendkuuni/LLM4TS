import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Typical use case
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4; // Root at x = 2
            }
        }, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Edge case: Function value accuracy
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4; // Root at x = 2
            }
        }, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-20);

        // Error scenario: Function does not cross x-axis
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x*x + 4; // No root
            }
        }, 1.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, result, 1e-6);
    }
}