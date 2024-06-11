package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

/**
 * Test cases for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;

        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);

        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the cumulative probability method with valid input.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test with a value less than the minimum possible value.
        double x = -1.0;
        double cdf = distribution.cumulativeProbability(x);
        assertEquals(0.0, cdf, 0.0);

        // Test with a value equal to the minimum possible value.
        x = 0.0;
        cdf = distribution.cumulativeProbability(x);
        assertEquals(0.0, cdf, 0.0);

        // Test with a value close to the minimum possible value.
        x = 0.01;
        cdf = distribution.cumulativeProbability(x);
        assertEquals(cdf, Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom), 0.001);

        // Test with a value close to the mean.
        x = (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom) / (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom);
        cdf = distribution.cumulativeProbability(x);
        assertEquals(cdf, 0.5, 0.001);

        // Test with a value close to the maximum possible value.
        x = 10.0;
        cdf = distribution.cumulativeProbability(x);
        assertEquals(1.0, cdf, 0.001);

        // Test with a value greater than the maximum possible value.
        x = Double.POSITIVE_INFINITY;
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(x));
    }

    /**
     * Test the inverse cumulative probability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 7.0;
        final FDistributionImpl distribution = new FDistributionImpl(
                numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test with a probability less than 0.
        double p = -0.1;
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(p));

        // Test with a probability equal to 0.
        p = 0.0;
        double x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);

        // Test with a probability close to 0.
        p = 0.01;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(x, Double.NEGATIVE_INFINITY, 0.0);

        // Test with a probability close to 0.5.
        p = 0.5;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(x, (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom) / (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom), 0.001);

        // Test with a probability close to 1.
        p = 0.99;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(x, Double.POSITIVE_INFINITY, 0.0);

        // Test with a probability greater than 1.
        p = 1.1;
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(p));
    }

    /**
     * Test the setNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        final FDistributionImpl distribution = new FDistributionImpl(1.0, 2.0);

        distribution.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the setDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        final FDistributionImpl distribution = new FDistributionImpl(1.0, 2.0);

        distribution.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}