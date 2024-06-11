package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest extends AbstractMathTestCase {

    /**
     * Tests the evaluation of the golden ratio continued fraction.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(0.0);
        assertEquals(1.618051232076661, result, 1e-10);
    }

    /**
     * Tests the evaluation of the square root of 2 continued fraction.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testSqrt2() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        double result = sqrt2.evaluate(0.0);
        assertEquals(1.4142135623730955, result, 1e-10);
    }

    /**
     * Tests the evaluation of the logarithm base 2 continued fraction.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testLog2() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        double result = log2.evaluate(1.0);
        assertEquals(0.3010299956639812, result, 1e-10);
    }

    /**
     * Tests the evaluation of the Euler-Mascheroni constant continued fraction.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testEulerMascheroni() {
        ContinuedFraction eulerMascheroni = new EulerMascheroniContinuedFraction();
        double result = eulerMascheroni.evaluate(0.0);
        assertEquals(0.5772156649015328, result, 1e-10);
    }

    /**
     * Tests the evaluation with maximum error allowed.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testMaxError() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        double result = sqrt2.evaluate(0.0, 1e-6, 10);
        assertEquals(1.4142135623730955, result, 1e-6);
    }

    /**
     * Tests the evaluation with maximum number of iterations.
     * @throws ConvergenceException if the evaluation fails to converge.
     */
    @Test
    public void testMaxIterations() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        try {
            log2.evaluate(0.0, 0.0, 0);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}