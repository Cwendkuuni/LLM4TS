import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ContinuedFractionTest {

    // Test case for getA method
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(0, cf.getA(0, 10), 0.0001);
        assertEquals(1, cf.getA(1, 10), 0.0001);
        assertEquals(2, cf.getA(2, 10), 0.0001);
    }

    // Test case for getB method
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 2;
            }
        };

        assertEquals(2, cf.getB(0, 10), 0.0001);
        assertEquals(2, cf.getB(1, 10), 0.0001);
        assertEquals(2, cf.getB(2, 10), 0.0001);
    }

    // Test case for evaluate method with default parameters
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        double result = cf.evaluate(5);
        assertEquals(0.9589242746631385, result, 0.0001);
    }

    // Test case for evaluate method with specified epsilon
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        double result = cf.evaluate(5, 0.0001);
        assertEquals(0.9589242746631385, result, 0.0001);
    }

    // Test case for evaluate method with specified maxIterations
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        double result = cf.evaluate(5, 10);
        assertEquals(0.9589242747, result, 0.0001);
    }

    // Test case for evaluate method with specified epsilon and maxIterations
    @Test
    public void testEvaluateCustomEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return x;
            }
        };

        double result = cf.evaluate(5, 0.0001, 10);
        assertEquals(0.0000128001, result, 0.0001);
    }

    // Test case for evaluate method with non-convergent continued fraction
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateNonConvergent() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        cf.evaluate(5, 0.0001, 10);
    }
}