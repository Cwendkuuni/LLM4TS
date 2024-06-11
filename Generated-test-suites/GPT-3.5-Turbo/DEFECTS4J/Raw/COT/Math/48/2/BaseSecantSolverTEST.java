import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: f(x) = x^2 - 4, expected root = 2
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        double root1 = solver.solve(100, f1, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root1, 1e-6);

        // Test case 2: f(x) = x^3 - 27, expected root = 3
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x*x - 27;
            }
        };
        double root2 = solver.solve(100, f2, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(3.0, root2, 1e-6);

        // Test case 3: f(x) = sin(x), expected root = 0
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double root3 = solver.solve(100, f3, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root3, 1e-6);
    }

    // TODO: Add test cases for edge cases and exception handling scenarios

}