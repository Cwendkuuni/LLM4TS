import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate_withDefaultEpsilon() {
        // Test with default epsilon
        double x = 1.234;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return (n + 1) * x;
            }
        };

        double result = fraction.evaluate(x);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testEvaluate_withCustomEpsilon() {
        // Test with custom epsilon
        double x = 1.234;
        double epsilon = 0.001;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return (n + 1) * x;
            }
        };

        double result = fraction.evaluate(x, epsilon);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testEvaluate_withMaxIterations() {
        // Test with max iterations
        double x = 1.234;
        int maxIterations = 10;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return (n + 1) * x;
            }
        };

        double result = fraction.evaluate(x, maxIterations);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testEvaluate_withInfinityDivergence() {
        // Test with infinity divergence
        double x = 1.0;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n * x;
            }
        };

        try {
            fraction.evaluate(x);
        } catch (ConvergenceException e) {
            Assert.assertEquals(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, e.getGeneralPattern());
        }
    }

    @Test
    public void testEvaluate_withNanDivergence() {
        // Test with NaN divergence
        double x = 1.0;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return Double.NaN;
            }
        };

        try {
            fraction.evaluate(x);
        } catch (ConvergenceException e) {
            Assert.assertEquals(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, e.getGeneralPattern());
        }
    }

    @Test
    public void testEvaluate_withNonConvergent() {
        // Test with non-convergent fraction
        double x = 1.234;
        int maxIterations = 1;
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return (n + 1) * x;
            }
        };

        try {
            fraction.evaluate(x, maxIterations);
        } catch (MaxCountExceededException e) {
            Assert.assertEquals(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, e.getGeneralPattern());
            Assert.assertEquals(maxIterations, e.getMax());
        }
    }

}