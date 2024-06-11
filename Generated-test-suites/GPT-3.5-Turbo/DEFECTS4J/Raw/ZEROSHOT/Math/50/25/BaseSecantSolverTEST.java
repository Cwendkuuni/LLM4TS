import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, function, 0, 10);
        assertEquals(2.0, result, 1e-6);
    }

    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        double result = solver.solve(100, function, 0, 10, AllowedSolution.LEFT_SIDE);
        assertEquals(3.0, result, 1e-6);
    }

    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(10, function, 0, 10);
        assertEquals(2.0, result, 1e-6);
    }

    private void assertEquals(double expected, double actual, double epsilon) {
        if (Math.abs(expected - actual) > epsilon) {
            throw new AssertionError("Expected: " + expected + ", actual: " + actual);
        }
    }
}