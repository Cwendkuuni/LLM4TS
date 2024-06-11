import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidInputs_ReturnsRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, function, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithZeroFunctionValue_ReturnsExactRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, function, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_WithMaxIterationsReached_ThrowsConvergenceException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.solve(5, function, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}