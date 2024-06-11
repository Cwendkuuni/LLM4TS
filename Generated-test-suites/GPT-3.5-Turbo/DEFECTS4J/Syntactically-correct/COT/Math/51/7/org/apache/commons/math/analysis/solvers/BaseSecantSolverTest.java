import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        // Regular test case
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10);
        assertEquals(2.0, result, 1e-6);

        // Test case with function value exactly 0
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, -10, 10);
        assertEquals(0.0, result, 1e-6);

        // Test case with approximation already within tolerance
        result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -2, 3);
        assertEquals(2.0, result, 1e-6);
    }

}