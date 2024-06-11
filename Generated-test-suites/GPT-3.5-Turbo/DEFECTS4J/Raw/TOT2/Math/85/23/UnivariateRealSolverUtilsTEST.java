import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0);
        assertEquals(2.0, result, 1e-5);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, 1e-5);
        assertEquals(2.0, result, 1e-5);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0);
        assertArrayEquals(new double[]{1.0, 3.0}, result, 1e-5);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        assertEquals(2.0, result, 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealSolverUtils.solve(null, 1.0, 3.0);
    }

    @Test
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Test passes
        }
    }
}