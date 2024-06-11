import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return 2 * x + 1; // y = 2x + 1
        }
    };

    @Test
    public void testSolve() {
        double result = UnivariateRealSolverUtils.solve(linearFunction, 0, 10);
        assertEquals(-0.5, result, 0.001); // Expected result: x = -0.5
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        double result = UnivariateRealSolverUtils.solve(linearFunction, 0, 10, 0.001);
        assertEquals(-0.5, result, 0.001); // Expected result: x = -0.5
    }

    @Test
    public void testBracket() {
        double[] bracket = UnivariateRealSolverUtils.bracket(linearFunction, 5, -10, 10, 100);
        assertEquals(4, bracket[0], 0.001); // Expected result: a = 4.0
        assertEquals(6, bracket[1], 0.001); // Expected result: b = 6.0
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(4, 6);
        assertEquals(5.0, result, 0.001); // Expected result: midpoint = 5.0
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 0, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithInvalidInterval() {
        UnivariateRealSolverUtils.bracket(linearFunction, 5, 10, -10, 100);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterationsReached() {
        UnivariateRealSolverUtils.bracket(linearFunction, 5, -10, 10, 1);
    }

}