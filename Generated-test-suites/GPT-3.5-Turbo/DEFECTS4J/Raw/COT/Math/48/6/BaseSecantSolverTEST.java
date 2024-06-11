import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.Method;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.util.FastMath;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithNoRoot() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, -2);
    }

    @Test
    public void testSolveWithAllowedSolution() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolveWithDifferentMethod() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, Method.ILLINOIS);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMethod() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, null);
        double result = solver.solve(100, new TestFunction(), 0, 2);
    }

    @Test
    public void testSolveWithPrecision() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-8, Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        double expected = 1.0;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testSolveWithRelativeAccuracy() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, 1e-10, Method.REGULA_FALSI);
        double result = solver.solve(100, new TestFunction(), 0, 2);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() throws ConvergenceException {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, Method.REGULA_FALSI);
        double result = solver.solve(1, new TestFunction(), 0, 2);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    private class TestSecantSolver extends BaseSecantSolver {
        public TestSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        @Override
        protected double computeObjectiveValue(double x) {
            double result = x - 1.0;
            return result;
        }
    }

    private class TestFunction implements org.apache.commons.math.analysis.UnivariateRealFunction {
        @Override
        public double value(double x) {
            double result = x - 1.0;
            return result;
        }
    }
}