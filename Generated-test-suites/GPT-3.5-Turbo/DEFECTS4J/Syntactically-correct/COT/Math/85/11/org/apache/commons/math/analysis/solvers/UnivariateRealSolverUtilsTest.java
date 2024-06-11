import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnivariateRealSolverUtilsTest {

    // Test solve(UnivariateRealFunction f, double x0, double x1)
    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new TestFunction();
        double x0 = -1.0;
        double x1 = 1.0;
        double expected = 0.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, result, 0.0001);
    }

    // Test solve(UnivariateRealFunction f, double x0, double x1, double absoluteAccuracy)
    @Test
    public void testSolveWithAccuracy() throws Exception {
        UnivariateRealFunction f = new TestFunction();
        double x0 = -1.0;
        double x1 = 1.0;
        double absoluteAccuracy = 0.0001;
        double expected = 0.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, result, absoluteAccuracy);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound)
    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new TestFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] expected = {-1.0, 1.0};

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(expected[0], result[0], 0.0001);
        assertEquals(expected[1], result[1], 0.0001);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations)
    @Test
    public void testBracketWithIterations() throws Exception {
        UnivariateRealFunction f = new TestFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = 10;
        double[] expected = {-1.0, 1.0};

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(expected[0], result[0], 0.0001);
        assertEquals(expected[1], result[1], 0.0001);
    }

    // Test midpoint(double a, double b)
    @Test
    public void testMidpoint() throws Exception {
        double a = -1.0;
        double b = 1.0;
        double expected = 0.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 0.0001);
    }

    // Test solve(UnivariateRealFunction f, double x0, double x1) with null function
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        double x0 = -1.0;
        double x1 = 1.0;
        
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound) with null function
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound) with invalid bounds
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidBounds() throws Exception {
        UnivariateRealFunction f = new TestFunction();
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = -1.0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double v) {
            return v * v - 1.0;
        }
    }
}