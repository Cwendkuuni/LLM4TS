import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate_DefaultEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = continuedFraction.evaluate(2.0);

        assertEquals(0.5454545454545454, result, 1e-9);
    }

    @Test
    public void testEvaluate_CustomEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = continuedFraction.evaluate(2.0, 1e-6);

        assertEquals(0.5454545454545454, result, 1e-6);
    }

    @Test
    public void testEvaluate_CustomMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        double result = continuedFraction.evaluate(2.0, 1e-9, 100);

        assertEquals(0.5454545454545454, result, 1e-9);
    }

    @Test
    public void testEvaluate_ZeroCoefficientException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 0.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            double result = continuedFraction.evaluate(2.0);

            assertEquals(Double.NaN, result, 1e-9);
        } catch (ConvergenceException e) {
            assertEquals("Continued fraction convergence failure for value 2.0", e.getMessage());
        }
    }

    @Test
    public void testEvaluate_DivergenceException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };

        try {
            double result = continuedFraction.evaluate(Double.POSITIVE_INFINITY);

            assertEquals(Double.POSITIVE_INFINITY, result, 1e-9);
        } catch (ConvergenceException e) {
            assertEquals("Continued fraction divergence for value Infinity", e.getMessage());
        }
    }

    @Test
    public void testEvaluate_NonConvergentException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        try {
            double result = continuedFraction.evaluate(2.0, 1e-9, 10);

            assertEquals(Double.NaN, result, 1e-9);
        } catch (MaxCountExceededException e) {
            assertEquals("Continued fraction failed to converge within 10 iterations", e.getMessage());
        }
    }
}