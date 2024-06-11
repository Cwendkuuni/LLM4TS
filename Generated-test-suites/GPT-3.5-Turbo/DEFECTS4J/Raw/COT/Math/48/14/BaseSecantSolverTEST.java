import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Test computeObjectiveValue() method

    @Test
    public void testComputeObjectiveValue() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.computeObjectiveValue(2.0);
        assertEquals(0.0, result, 1e-6);
    }

    // Test solve() method

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0.0, 5.0);
        assertEquals(2.0, result, 1e-6);
    }

    // Test solve() method with known root

    @Test
    public void testSolveWithKnownRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 1.0, 3.0);
        assertEquals(2.0, result, 1e-6);
    }

    // Test solve() method with maximum number of iterations reached

    @Test(expected = ConvergenceException.class)
    public void testSolveWithMaxIterationsReached() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(1, f, 0.0, 5.0);
    }

    // Test solve() method with function value within the allowed solution

    @Test
    public void testSolveWithFunctionValueWithinAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0.0, 5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    // Test solve() method with function value outside the allowed solution

    @Test(expected = ConvergenceException.class)
    public void testSolveWithFunctionValueOutsideAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0.0, 5.0, AllowedSolution.LEFT_SIDE);
    }

}