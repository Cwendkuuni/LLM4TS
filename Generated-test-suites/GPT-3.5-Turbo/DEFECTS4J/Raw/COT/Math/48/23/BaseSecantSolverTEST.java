
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        }, 0, 2);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolve_BelowSide() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        }, 1, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolve_AboveSide() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        }, 0, 1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_IllegalArgumentException() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, null, 0, 1);
    }

    @Test
    public void testSolve_MaximumIterations() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        }, 0, 2);
        assertEquals(1.0, root, 1e-6);
    }

    private static class BaseSecantSolverImpl extends BaseSecantSolver {
        public BaseSecantSolverImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        public double computeObjectiveValue(double x) {
            return 0;
        }
    }
}
