import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: Simple function with root at x = 0
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, -10, 10);
        assertEquals(0, result, 1e-6);

        // Test case 2: Minimum and maximum bounded
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * (x - 5);
            }
        }, 2, 4);
        assertEquals(0, result, 1e-6);

        // Test case 3: Function with multiple roots
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, -10, 10);
        assertTrue(result == Math.PI || result == -Math.PI);

        // Test case 4: Function with no root in range
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        }, -10, -5);
        assertTrue(Double.isNaN(result));
    }
}