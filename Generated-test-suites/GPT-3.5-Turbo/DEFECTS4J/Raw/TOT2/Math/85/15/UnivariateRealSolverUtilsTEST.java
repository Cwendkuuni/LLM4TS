import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 2, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracketSuccess() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 1, -2, 2);
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(1.0, result[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFailure() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 4;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 1, -2, 2);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}