import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        assertEquals(0.0, BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(0.0, BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.LEFT_SIDE), 1e-6);
        assertEquals(0.0, BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.RIGHT_SIDE), 1e-6);
        assertEquals(0.0, BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.BELOW_SIDE), 1e-6);
        assertEquals(0.0, BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.ABOVE_SIDE), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveNoSolution() {
        BaseSecantSolver.solve(100, new NoSolutionFunction(), -1, 1, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveInvalidMethod() {
        BaseSecantSolver.solve(100, new TestFunction(), -1, 1, AllowedSolution.ANY_SIDE, BaseSecantSolver.Method.SECANT);
    }

    private static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return Math.sin(x);
        }
    }

    private static class NoSolutionFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x - 1;
        }
    }
}