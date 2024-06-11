import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolveWithMinAndMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, 2.5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1e-6);
    }
}