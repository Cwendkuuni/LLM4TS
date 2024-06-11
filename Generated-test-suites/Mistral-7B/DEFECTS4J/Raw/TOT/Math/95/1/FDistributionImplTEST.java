package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FLocationSpecificMoment;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test the construction of FDistributionImpl with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        FDistribution dist = new FDistributionImpl(3.0, 5.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the construction of FDistributionImpl with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() {
        new FDistributionImpl(-1.0, 5.0);
        new FDistributionImpl(3.0, -1.0);
    }

    /**
     * Test the cumulative probability of FDistributionImpl.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 5.0);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        for (double x = 0.0; x <= 2.5; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            double expected = Beta.regularizedBeta((3.0 * x) / (5.0 + 3.0 * x), 1.5, 1.5);
            assertEquals(expected, cdf, 0.001);
        }
    }

    /**
     * Test the inverse cumulative probability of FDistributionImpl.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 5.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = (5.0 * p) / (3.0 * (1.0 - p));
            assertEquals(expected, x, 0.001);
        }

        // Test for edge cases
        assertThrows(MathException.class, () -> dist.cumulativeProbability(Double.NEGATIVE_INFINITY));
        assertThrows(MathException.class, () -> dist.cumulativeProbability(Double.POSITIVE_INFINITY));
    }

    /**
     * Test the expected value of FDistributionImpl using FLocationSpecificMoment.
     */
    @Test
    public void testExpectedValue() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 5.0);
        double expected = FLocationSpecificMoment.moment(dist, 1.0);
        double actual = dist.getExpectedValue();
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test the variance of FDistributionImpl using FLocationSpecificMoment.
     */
    @Test
    public void testVariance() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 5.0);
        double expected = FLocationSpecificMoment.moment(dist, 2.0);
        double actual = dist.getVariance();
        assertEquals(expected, actual, 0.001);
    }
}