import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test when the function is null
        try {
            UnivariateRealSolverUtils.solve(null, 1.0, 2.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the endpoints do not specify a valid interval
        try {
            UnivariateRealSolverUtils.solve(x -> x, 2.0, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the iteration count is exceeded
        try {
            UnivariateRealSolverUtils.solve(x -> x, -1e9, 1e9);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with valid inputs
        double result = UnivariateRealSolverUtils.solve(x -> x - 1, 0, 2);
        assertEquals(1.0, result, 1e-7);
    }

    @Test
    public void testSolveWithAccuracy() {
        // Test when the function is null
        try {
            UnivariateRealSolverUtils.solve(null, 1.0, 2.0, 1e-7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the endpoints do not specify a valid interval
        try {
            UnivariateRealSolverUtils.solve(x -> x, 2.0, 1.0, 1e-7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the absolute accuracy is not valid for the default solver
        try {
            UnivariateRealSolverUtils.solve(x -> x, 0, 1, -1e-7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the iteration count is exceeded
        try {
            UnivariateRealSolverUtils.solve(x -> x, -1e9, 1e9, 1e-7);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with valid inputs
        double result = UnivariateRealSolverUtils.solve(x -> x - 1, 0, 2, 1e-7);
        assertEquals(1.0, result, 1e-7);
    }

    @Test
    public void testBracket() {
        // Test when the function is null
        try {
            UnivariateRealSolverUtils.bracket(null, 0, -1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the maximumIterations is not positive
        try {
            UnivariateRealSolverUtils.bracket(x -> x, 0, -1, 1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when initial is not between lowerBound and upperBound
        try {
            UnivariateRealSolverUtils.bracket(x -> x, -1, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test when the algorithm fails to find a and b
        try {
            UnivariateRealSolverUtils.bracket(x -> x, 0, -1, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with valid inputs
        double[] result = UnivariateRealSolverUtils.bracket(x -> x - 1, 0, -1, 1);
        assertEquals(-1.0, result[0], 1e-7);
        assertEquals(1.0, result[1], 1e-7);
    }

    @Test
    public void testMidpoint() {
        double mid = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1.0, mid, 1e-7);
    }
}