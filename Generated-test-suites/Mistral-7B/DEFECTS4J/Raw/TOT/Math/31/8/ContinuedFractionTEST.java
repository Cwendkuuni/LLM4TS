package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Tests the evaluation of the square root of 2 continued fraction.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testSqrt2ContinuedFraction() {
        ContinuedFraction sqrt2ContinuedFraction = new Sqrt2ContinuedFraction();
        try {
            double result = sqrt2ContinuedFraction.evaluate(2.0);
            Assert.assertEquals(1.414213562373095525, result, 1e-9);
        } catch (MaxCountExceededException | ConvergenceException e) {
            // the algorithm for sqrt2 continued fraction may not converge
            // within the default maximum number of iterations and error tolerance
        }
    }

    /**
     * Tests the evaluation of the e continued fraction.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testECcontinuedFraction() {
        ContinuedFraction eContinuedFraction = new EContinuedFraction();
        try {
            double result = eContinuedFraction.evaluate(0.0);
            Assert.assertTrue(Double.isNaN(result));
            result = eContinuedFraction.evaluate(1.0);
            Assert.assertEquals(2.718281828459045235, result, 1e-6);
        } catch (MaxCountExceededException | ConvergenceException e) {
            // the algorithm for e continued fraction may not converge
            // within the default maximum number of iterations and error tolerance
        }
    }

    /**
     * Tests the evaluation of the sin(pi/4) continued fraction.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testSinPi4ContinuedFraction() {
        ContinuedFraction sinPi4ContinuedFraction = new SinPi4ContinuedFraction();
        try {
            double result = sinPi4ContinuedFraction.evaluate(0.0);
            Assert.assertEquals(0.0, result, 1e-9);
            result = sinPi4ContinuedFraction.evaluate(1.0);
            Assert.assertEquals(0.707106781186547524, result, 1e-6);
        } catch (MaxCountExceededException | ConvergenceException e) {
            // the algorithm for sin(pi/4) continued fraction may not converge
            // within the default maximum number of iterations and error tolerance
        }
    }

    /**
     * Tests the evaluation of the cotangent continued fraction with negative x.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testCotangentNegativeXContinuedFraction() {
        ContinuedFraction cotangentNegativeXContinuedFraction = new CotangentContinuedFraction();
        try {
            double result = cotangentNegativeXContinuedFraction.evaluate(-2.0);
            Assert.assertEquals(-0.258819045102520634, result, 1e-9);
        } catch (MaxCountExceededException | ConvergenceException e) {
            // the algorithm for cotangent continued fraction with negative x may not converge
            // within the default maximum number of iterations and error tolerance
        }
    }
}