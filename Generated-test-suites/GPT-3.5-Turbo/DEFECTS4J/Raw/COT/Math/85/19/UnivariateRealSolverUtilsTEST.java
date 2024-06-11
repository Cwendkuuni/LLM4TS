import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    // Test solve method
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, actual, 0.0001);
    }

    // Test solve method with absoluteAccuracy
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.0001;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, actual, absoluteAccuracy);
    }

    // Test bracket method
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 1;
        double upperBound = 3;
        int maximumIterations = 1000;
        double[] expected = {1, 3};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertArrayEquals(expected, actual, 0.0001);
    }

    // Test midpoint method
    @Test
    public void testMidpoint() {
        double a = 1;
        double b = 3;
        double expected = 2;
        double actual = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, actual, 0.0001);
    }
}