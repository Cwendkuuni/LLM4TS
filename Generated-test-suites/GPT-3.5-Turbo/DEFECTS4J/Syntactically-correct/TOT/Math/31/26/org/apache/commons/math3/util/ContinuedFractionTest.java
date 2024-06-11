import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test typical use case
        double expected = 3.5;
        double x = 2.0;
        double result = MyContinuedFraction.getA(0, x);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = 0.0;
        x = 0.0;
        result = MyContinuedFraction.getA(0, x);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.getA(0, x);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetB() {
        // Test typical use case
        double expected = 2.5;
        double x = 2.0;
        double result = MyContinuedFraction.getB(0, x);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = 0.0;
        x = 0.0;
        result = MyContinuedFraction.getB(0, x);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.getB(0, x);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEvaluate() {
        // Test typical use case
        double expected = 5.0;
        double x = 2.0;
        double result = MyContinuedFraction.evaluate(x);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = Double.POSITIVE_INFINITY;
        x = 0.0;
        result = MyContinuedFraction.evaluate(x);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.evaluate(x);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test typical use case
        double expected = 5.0;
        double x = 2.0;
        double epsilon = 1e-6;
        double result = MyContinuedFraction.evaluate(x, epsilon);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = Double.POSITIVE_INFINITY;
        x = 0.0;
        result = MyContinuedFraction.evaluate(x, epsilon);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.evaluate(x, epsilon);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test typical use case
        double expected = 5.0;
        double x = 2.0;
        int maxIterations = 10;
        double result = MyContinuedFraction.evaluate(x, maxIterations);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = Double.POSITIVE_INFINITY;
        x = 0.0;
        result = MyContinuedFraction.evaluate(x, maxIterations);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.evaluate(x, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testEvaluateWithAllParameters() {
        // Test typical use case
        double expected = 5.0;
        double x = 2.0;
        double epsilon = 1e-6;
        int maxIterations = 10;
        double result = MyContinuedFraction.evaluate(x, epsilon, maxIterations);
        assertEquals(expected, result, 0.0);

        // Test edge case
        expected = Double.POSITIVE_INFINITY;
        x = 0.0;
        result = MyContinuedFraction.evaluate(x, epsilon, maxIterations);
        assertEquals(expected, result, 0.0);

        // Test error scenario
        x = Double.NaN;
        try {
            MyContinuedFraction.evaluate(x, epsilon, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }

        // Test error scenario with maxIterations = 0
        x = 2.0;
        maxIterations = 0;
        try {
            MyContinuedFraction.evaluate(x, epsilon, maxIterations);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected exception
        }
    }
}