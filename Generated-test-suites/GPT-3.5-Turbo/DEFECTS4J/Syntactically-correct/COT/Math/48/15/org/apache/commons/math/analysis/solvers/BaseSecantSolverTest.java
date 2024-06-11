import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test 1: Basic functionality
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution1 = solver.solve(100, f1, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assert Math.abs(solution1 - 2.0) <= 1e-6;

        // Test 2: Boundary cases
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double solution2 = solver.solve(100, f2, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assert Math.abs(solution2 - Math.PI) <= 1e-6;

    }

    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.SECANT);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

}