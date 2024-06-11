import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testEvaluate() {

        // Test default evaluate() method

        // Typical use case
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return x;
            }
            protected double getB(int n, double x) {
                return n + 1;
            }
        };
        double x1 = 2.0;
        double expected1 = 0.357142857;
        assertEquals(expected1, cf.evaluate(x1), EPSILON);

        // Edge case: x = 0
        double x2 = 0.0;
        double expected2 = 1.0;
        assertEquals(expected2, cf.evaluate(x2), EPSILON);

        // Edge case: x = NaN
        double x3 = Double.NaN;
        try {
            cf.evaluate(x3);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected Exception
        }

        // Error scenario: Algorithm fails to converge (maxIterations exceeded)
        double x4 = 1.0;
        int maxIterations = 3; // Set low maxIterations to trigger exception
        try {
            cf.evaluate(x4, EPSILON, maxIterations);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected Exception
            assertEquals(maxIterations, e.getMax());
            assertEquals(x4, e.getArgument());
        }
    }

    @Test
    public void testEvaluateWithEpsilon() {

        // Test evaluate(double x, double epsilon) method

        // Typical use case
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return x;
            }
            protected double getB(int n, double x) {
                return n + 1;
            }
        };
        double x1 = 1.0;
        double epsilon1 = 1e-6;
        double expected1 = 0.6380809;
        assertEquals(expected1, cf.evaluate(x1, epsilon1), EPSILON);

        // Edge case: epsilon = 0
        double x2 = 2.0;
        double epsilon2 = 0.0;
        double expected2 = 0.357142857;
        assertEquals(expected2, cf.evaluate(x2, epsilon2), EPSILON);

        // Error scenario: Algorithm fails to converge (maxIterations exceeded)
        double x3 = 3.0;
        double epsilon3 = 1e-2;
        int maxIterations = 3; // Set low maxIterations to trigger exception
        try {
            cf.evaluate(x3, epsilon3, maxIterations);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected Exception
            assertEquals(maxIterations, e.getMax());
            assertEquals(x3, e.getArgument());
        }
    }

    @Test
    public void testEvaluateWithMaxIterations() {

        // Test evaluate(double x, int maxIterations) method

        // Typical use case
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return x;
            }
            protected double getB(int n, double x) {
                return n + 1;
            }
        };
        double x1 = 2.0;
        int maxIterations1 = 5;
        double expected1 = 0.357142857;
        assertEquals(expected1, cf.evaluate(x1, maxIterations1), EPSILON);

        // Edge case: maxIterations = 0
        double x2 = 1.0;
        int maxIterations2 = 0;
        try {
            cf.evaluate(x2, maxIterations2);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected Exception
            assertEquals(maxIterations2, e.getMax());
            assertEquals(x2, e.getArgument());
        }

        // Error scenario: Algorithm fails to converge (maxIterations exceeded)
        double x3 = 3.0;
        int maxIterations3 = 5;
        try {
            cf.evaluate(x3, maxIterations3);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected Exception
            assertEquals(maxIterations3, e.getMax());
            assertEquals(x3, e.getArgument());
        }
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {

        // Test evaluate(double x, double epsilon, int maxIterations) method

        // Typical use case
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return x;
            }
            protected double getB(int n, double x) {
                return n + 1;
            }
        };
        double x1 = 1.0;
        double epsilon1 = 1e-6;
        int maxIterations1 = 10;
        double expected1 = 0.6380809;
        assertEquals(expected1, cf.evaluate(x1, epsilon1, maxIterations1), EPSILON);

        // Edge case: epsilon = 0, maxIterations = 0
        double x2 = 2.0;
        double epsilon2 = 0.0;
        int maxIterations2 = 0;
        double expected2 = 0.357142857;
        assertEquals(expected2, cf.evaluate(x2, epsilon2, maxIterations2), EPSILON);

        // Error scenario: Algorithm fails to converge (maxIterations exceeded)
        double x3 = 3.0;
        double epsilon3 = 1e-2;
        int maxIterations3 = 3; // Set low maxIterations to trigger exception
        try {
            cf.evaluate(x3, epsilon3, maxIterations3);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected Exception
            assertEquals(maxIterations3, e.getMax());
            assertEquals(x3, e.getArgument());
        }
    }
}