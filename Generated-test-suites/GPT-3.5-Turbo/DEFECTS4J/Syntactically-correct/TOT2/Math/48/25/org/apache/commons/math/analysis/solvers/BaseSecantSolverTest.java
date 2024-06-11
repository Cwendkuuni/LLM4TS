import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(1, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    // Add more test cases for other methods and edge cases

    private class BaseSecantSolverImpl extends BaseSecantSolver {

        public BaseSecantSolverImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double doSolve() {
            // Dummy implementation for testing
            return 0;
        }
    }
}