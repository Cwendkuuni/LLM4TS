import org.junit.Test;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testGetA() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.getA(0, 0.0), EPSILON);
        assertEquals(2.0, cf.getA(1, 0.0), EPSILON);
        assertEquals(4.0, cf.getA(2, 0.0), EPSILON);

        assertEquals(4.0, cf.getA(0, 1.0), EPSILON);
        assertEquals(6.0, cf.getA(1, 1.0), EPSILON);
        assertEquals(8.0, cf.getA(2, 1.0), EPSILON);
    }

    @Test
    public void testGetB() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.getB(0, 0.0), EPSILON);
        assertEquals(1.0, cf.getB(1, 0.0), EPSILON);
        assertEquals(2.0, cf.getB(2, 0.0), EPSILON);

        assertEquals(2.0, cf.getB(0, 1.0), EPSILON);
        assertEquals(3.0, cf.getB(1, 1.0), EPSILON);
        assertEquals(4.0, cf.getB(2, 1.0), EPSILON);
    }

    @Test
    public void testEvaluate() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        double x = 0.0;
        assertEquals(1.0, cf.evaluate(x), EPSILON);

        x = 1.0;
        assertEquals(1.5, cf.evaluate(x), EPSILON);

        x = 2.0;
        assertEquals(1.625, cf.evaluate(x), EPSILON);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        double x = 1.0;
        int maxIterations = 10;
        cf.evaluate(x, maxIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        double x = 0.0;
        cf.evaluate(x);
    }

    @Test
    public void testEvaluateSmallX() {
        // Create a test instance of ContinuedFraction
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / (n + 1);
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double x = 1e-9;
        assertEquals(1.000000000000001, cf.evaluate(x), EPSILON);

        x = 1e-20;
        assertEquals(1.00000000000000001, cf.evaluate(x), EPSILON);
    }    
}