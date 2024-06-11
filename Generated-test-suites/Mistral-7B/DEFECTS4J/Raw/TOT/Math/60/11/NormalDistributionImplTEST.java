package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testNormalDistributionConstruction() {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 1e-9);
        assertEquals(sd, dist.getStandardDeviation(), 1e-9);
    }

    /**
     * Test constructor with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionNegativeStandardDeviation() {
        final double mean = 0.0;
        final double sd = -1.0;
        new NormalDistributionImpl(mean, sd);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        final double x = FastMath.nextGaussian(mean, sd);
        final double density = dist.density(x);

        final double expectedDensity = FastMath.exp(-0.5 * Math.pow(x - mean, 2) / Math.pow(sd, 2)) / (sd * Math.sqrt(2 * Math.PI));
        assertEquals(expectedDensity, density, 1e-6);
    }

    /**
     * Test cumulative probability method with a valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        final double x = FastMath.nextGaussian(mean, sd);
        final double probability = dist.cumulativeProbability(x);

        final double expectedProbability = 0.5 * (1.0 + Erf.erf((x - mean) / (sd * Math.sqrt(2.0))));
        assertEquals(expectedProbability, probability, 1e-7);
    }

    /**
     * Test cumulative probability method with an out of range input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRangeInput() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        final double x = mean - 10 * sd;
        dist.cumulativeProbability(x);
    }

    /**
     * Test inverse cumulative probability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        final double p = FastMath.nextDouble();
        final double x = dist.inverseCumulativeProbability(p);

        final double expectedX = mean + sd * Math.erfInverse(2 * p - 1);
        assertEquals(expectedX, x, 1e-6);
    }

    /**
     * Test inverse cumulative probability method with an invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        dist.inverseCumulativeProbability(-1);
    }

    /**
     * Test inverse cumulative probability method with an out of range input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRangeInput() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        final double sample = dist.sample();

        // Check sample lies within the expected range
        final double lowerBound = mean - 3 * sd;
        final double upperBound = mean + 3 * sd;
        assertTrue(sample >= lowerBound);
        assertTrue(sample <= upperBound);
    }
}