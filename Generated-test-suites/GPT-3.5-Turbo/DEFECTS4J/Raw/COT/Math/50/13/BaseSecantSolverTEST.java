
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testWithSimpleRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testWithMultipleRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(root == 2 || root == -2);
    }

    @Test(expected = RuntimeException.class)
    public void testWithNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testWithNaNOrInfinity() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x / 0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = RuntimeException.class)
    public void testWithMaxEvaluations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(1, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
}

