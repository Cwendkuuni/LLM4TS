package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean value.
     */
    @Test
    public void testConstructorPositiveMean() {
        final double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean value.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNegativeMean() {
        final double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test probability method for zero value.
     */
    @Test
    public void testProbabilityZero() {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(FastMath.exp(-mean), poissonDistribution.probability(0), 0.0);
    }

    /**
     * Test probability method for positive value.
     */
    @Test
    public void testProbabilityPositive() {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        double expected = FastMath.exp(-mean) * FastMath.pow(FastMath.E, -mean + 1) / FastMath.sqrt(FastMath.TWO_PI * mean);
        assertEquals(expected, poissonDistribution.probability(1), 0.001);
    }

    /**
     * Test probability method for negative value.
     */
    @Test
    public void testProbabilityNegative() {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, poissonDistribution.probability(-1), 0.0);
    }

    /**
     * Test probability method for maximum integer value.
     */
    @Test
    public void testProbabilityMaxInt() {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.0);
    }

    /**
     * Test cumulativeProbability method for zero value.
     */
    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, poissonDistribution.cumulativeProbability(0), 0.0);
    }

    /**
     * Test cumulativeProbability method for positive value.
     */
    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        double expected = Gamma.regularizedGammaQ(1, mean);
        assertEquals(expected, poissonDistribution.cumulativeProbability(1), 0.001);
    }

    /**
     * Test cumulativeProbability method for negative value.
     */
    @Test
    public void testCumulativeProbabilityNegative() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, poissonDistribution.cumulativeProbability(-1), 0.0);
    }

    /**
     * Test cumulativeProbability method for maximum integer value.
     */
    @Test
    public void testCumulativeProbabilityMaxInt() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        double expected = 1.0;
        assertEquals(expected, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.001);
    }

    /**
     * Test normalApproximateProbability method for zero value.
     */
    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(FastMath.exp(-mean), poissonDistribution.normalApproximateProbability(0), 0.001);
    }

    /**
     * Test normalApproximateProbability method for positive value.
     */
    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        double expected = Gamma.regularizedGammaQ(1.5, mean) + 0.5 * Gamma.errorFunction(1.5, mean);
        assertEquals(expected, poissonDistribution.normalApproximateProbability(1), 0.001);
    }

    /**
     * Test normalApproximateProbability method for negative value.
     */
    @Test
    public void testNormalApproximateProbabilityNegative() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(0.0, poissonDistribution.normalApproximateProbability(-1), 0.0);
    }

    /**
     * Test normalApproximateProbability method for maximum integer value.
     */
    @Test
    public void testNormalApproximateProbabilityMaxInt() throws MathException {
        final double mean = 2.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        double expected = 1.0;
        assertEquals(expected, poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE), 0.001);
    }

    /**
     * Test sample method for small mean value.
     */
    @Test
    public void testSampleSmallMean() throws MathException {
        final double mean = 0.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        int sample = poissonDistribution.sample();
        assertEquals(0, sample, 0);
    }

    /**
     * Test sample method for large mean value.
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        final double mean = 100.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        int sample = poissonDistribution.sample();
        assertEquals(sample, sample, 0);
        // check that the sample is within the bounds
        assertEquals(sample, FastMath.min(sample * mean, Integer.MAX_VALUE), 0);
    }

    /**
     * Test sample method with IllegalArgumentException for negative mean value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSampleNegativeMean() {
        final double mean = -1.5;
        new PoissonDistributionImpl(mean).sample();
    }
}