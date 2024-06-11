import org.junit.Test;
import org.apache.commons.math.analysis.solvers.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Typical use case: Root is present within the bounds
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3);
        assert Math.abs(result - 2.0) < 1e-6;

        // Edge case: Same value at the boundaries
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        result = solver.solve(100, f, 2, 2);
        assert Math.abs(result - 2.0) < 1e-6;

        // Error scenario: No root within the bounds
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            result = solver.solve(100, f, 10, 20);
            assert false; // Exception not thrown, test failed
        } catch (IllegalStateException e) {
            // Exception thrown, test passed
        }
    }
}