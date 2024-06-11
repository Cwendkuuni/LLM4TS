import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.ConvergenceException;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // x^2 - 4 = 0, x = 2, -2
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10);
        assertEquals(2.0, root, 1e-6);

        root = solver.solve(100, f, -10, 0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithInvalidBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, f, 1, 0); // Invalid bounds should throw ConvergenceException
    }

    @Test
    public void testSolveWithTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // x^2 - 4 = 0, x = 2, -2
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10);
        assertEquals(2.0, root, 1e-8);
    }

}