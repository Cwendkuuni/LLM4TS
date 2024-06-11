import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new TestFunction();
        double result = UnivariateRealSolverUtils.solve(f, 0, 1);
        assertEquals(0, result, 1e-5);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new TestFunction();
        double result = UnivariateRealSolverUtils.solve(f, 0, 1, 1e-5);
        assertEquals(0, result, 1e-5);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new TestFunction();
        double[] result = UnivariateRealSolverUtils.bracket(f, 0.5, 0, 1);
        assertEquals(0, result[0], 0.001);
        assertEquals(1, result[1], 0.001);
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new TestFunction();
        double[] result = UnivariateRealSolverUtils.bracket(f, 0.5, 0, 1, 100);
        assertEquals(0, result[0], 0.001);
        assertEquals(1, result[1], 0.001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 1);
        assertEquals(0.5, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 0, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new TestFunction();
        UnivariateRealSolverUtils.solve(f, 0, 0);
    }

    private class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x;
        }
    }
}