import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolverStub(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_WhenNoRootInRange_ShouldThrowConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(false);
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.0;

        try {
            solver.solve(100, f, min, max, startValue);
        } catch (ConvergenceException e) {
            assertEquals("No root found in the given interval", e.getMessage());
        }
    }

    @Test
    public void testSolve_WhenRootInRange_ShouldReturnRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(true);
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.0;

        double result = solver.solve(100, f, min, max, startValue);

        assertTrue(Math.abs(result - 1.5) < 1e-6);
    }

    @Test
    public void testSolve_WhenRootOnLeftBoundary_ShouldReturnRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(true);
        double min = 0.0;
        double max = 2.0;
        double startValue = 0.0;

        double result = solver.solve(100, f, min, max, startValue);

        assertTrue(Math.abs(result - 0.0) < 1e-6);
    }

    @Test
    public void testSolve_WhenRootOnRightBoundary_ShouldReturnRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(true);
        double min = 0.0;
        double max = 2.0;
        double startValue = 2.0;

        double result = solver.solve(100, f, min, max, startValue);

        assertTrue(Math.abs(result - 2.0) < 1e-6);
    }

    @Test
    public void testSolve_WhenMaxEvalExceeded_ShouldThrowConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(true);
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.0;

        try {
            solver.solve(5, f, min, max, startValue);
        } catch (ConvergenceException e) {
            assertEquals("Maximum number of iterations exceeded", e.getMessage());
        }
    }

    private static class BaseSecantSolverStub extends BaseSecantSolver {

        public BaseSecantSolverStub(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double doSolve() {
            return 0;
        }
    }

    private static class UnivariateRealFunctionStub implements UnivariateRealFunction {

        private final boolean hasRoot;

        public UnivariateRealFunctionStub(boolean hasRoot) {
            this.hasRoot = hasRoot;
        }

        @Override
        public double value(double x) {
            if (hasRoot) {
                return x - 1.5;
            } else {
                return x + 1.0;
            }
        }
    }
}