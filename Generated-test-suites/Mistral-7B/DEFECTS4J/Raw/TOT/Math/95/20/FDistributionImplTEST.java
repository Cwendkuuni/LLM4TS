package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution dist = new FDistributionImpl(5.0, 7.0);
        assertEquals(5.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(7.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the cumulativeProbability method for various input values.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(5.0, 7.0);

        // Test with valid input values
        assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-15);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 1e-15);
        double expected = Beta.regularizedBeta(5.0 * 1.5 / (7.0 + 5.0 * 1.5),
            0.5 * 5.0, 0.5 * 7.0);
        assertEquals(expected, dist.cumulativeProbability(1.5), 1e-15);
        expected = Beta.regularizedBeta(5.0 * 3.0 / (7.0 + 5.0 * 3.0),
            0.5 * 5.0, 0.5 * 7.0);
        assertEquals(expected, dist.cumulativeProbability(3.0), 1e-15);

        // Test with large input value
        expected = Beta.regularizedBeta(5.0 * 10.0 / (7.0 + 5.0 * 10.0),
            0.5 * 5.0, 0.5 * 7.0);
        assertEquals(expected, dist.cumulativeProbability(10.0), 1e-15);

        // Test with invalid input value (negative x)
        try {
            dist.cumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test the inverseCumulativeProbability method for various input values.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(5.0, 7.0);

        // Test with valid input values
        assertEquals(1.0, dist.inverseCumulativeProbability(0.5), 1e-15);
        double p = 0.99;
        double expected = (5.0 * 3.0) / (7.0 + 5.0 * 3.0);
        assertEquals(expected, dist.inverseCumulativeProbability(p), 1e-15);

        // Test with edge cases
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 1e-15);

        // Test with invalid input value (p < 0 or p > 1)
        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }
}