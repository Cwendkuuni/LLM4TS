import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, -2, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, -2, 2, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(1.0, result[1], 0.0001);
    }

    @Test
    public void testBracketWithIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2, 100);
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(1.0, result[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(-2, 2);
        assertEquals(0.0, result, 0.0001);
    }
}