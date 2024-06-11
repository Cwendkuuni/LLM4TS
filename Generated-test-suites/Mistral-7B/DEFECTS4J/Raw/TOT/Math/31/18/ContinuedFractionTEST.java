import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test the evaluation of the golden ratio continued fraction.
     */
    @Test
    public void testGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(1.61803398875);
        double expected = 1.61803398875;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test the evaluation of the square root of 2 continued fraction.
     */
    @Test
    public void testSqrt2() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        double result = sqrt2.evaluate(2.0);
        double expected = 1.41421356237;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test the evaluation of the logarithm base 2 continued fraction for positive numbers.
     */
    @Test
    public void testLog2() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        for (double x = 0.1; x <= 5.0; x += 0.1) {
            double result = log2.evaluate(x);
            double expected = Math.log(x) / Math.log(2);
            assertEquals(expected, result, 1e-13);
        }
    }

    /**
     * Test the evaluation of the logarithm base 2 continued fraction for negative numbers.
     */
    @Test(expected = ConvergenceException.class)
    public void testLog2Negative() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        log2.evaluate(-1.0);
    }

    /**
     * Test the evaluation of the logarithm base e continued fraction for positive numbers.
     */
    @Test
    public void testLogE() {
        ContinuedFraction logE = new LogEContinuedFraction();
        for (double x = 0.1; x <= 5.0; x += 0.1) {
            double result = logE.evaluate(x);
            double expected = Math.log(x);
            assertEquals(expected, result, 1e-13);
        }
    }

    /**
     * Test the evaluation of the logarithm base e continued fraction for negative numbers.
     */
    @Test(expected = ConvergenceException.class)
    public void testLogENegative() {
        ContinuedFraction logE = new LogEContinuedFraction();
        logE.evaluate(-1.0);
    }

    /**
     * Test the max iterations in the evaluate method.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        sqrt2.evaluate(2.0, 0.001, 1);
    }
}