import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    private ContinuedFraction continuedFraction;

    @Before
    public void setUp() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                if (n % 2 == 0) {
                    return 2 * n;
                } else {
                    return 1.0;
                }
            }
        };
    }

    @Test
    public void testEvaluate() {
        double result = continuedFraction.evaluate(1.0);
        assertEquals(1.7320508075688772, result, EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        double result = continuedFraction.evaluate(1.0, EPSILON);
        assertEquals(1.7320508075688772, result, EPSILON);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        double result = continuedFraction.evaluate(1.0, 100);
        assertEquals(1.7320508075688772, result, EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        double result = continuedFraction.evaluate(1.0, EPSILON, 100);
        assertEquals(1.7320508075688772, result, EPSILON);
    }

    @Test
    public void testEvaluateInfiniteFraction() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0; // Causes infinite fraction
            }
        };
        try {
            double result = continuedFraction.evaluate(1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Test passed
        }
    }

    @Test
    public void testEvaluateNonConvergentFraction() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        try {
            double result = continuedFraction.evaluate(2.0, EPSILON, 10);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Test passed
        }
    }

    @Test
    public void testEvaluateZeroEpsilon() {
        double result = continuedFraction.evaluate(1.0, 0.0);
        assertEquals(1.7320508075688772, result, EPSILON);
    }

    @Test
    public void testEvaluateZeroMaxIterations() {
        try {
            double result = continuedFraction.evaluate(1.0, EPSILON, 0);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Test passed
        }
    }

}