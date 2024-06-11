package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for the {@code FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, dist.getNumeratorDegreesOfFreedom(), 1e-13);
        assertEquals(denominatorDegreesOfFreedom, dist.getDenominatorDegreesOfFreedom(), 1e-13);
    }

    /**
     * Test construction with invalid parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalid() {
        new FDistribution(-1.0, 5.0);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);

        // Test some positive values
        final double x = 1.5;
        final double density = dist.density(x);
        final double expected = 0.1323621415329982;
        assertEquals(expected, density, 1e-6);

        // Test some negative values
        x = -1.0;
        density = dist.density(x);
        assertTrue(Double.isNaN(density));

        // Test some zero value
        x = 0.0;
        density = dist.density(x);
        assertEquals(0.0, density, 1e-13);

        // Test extreme values
        x = Double.POSITIVE_INFINITY;
        density = dist.density(x);
        assertEquals(0.0, density, 1e-13);
    }

    /**
     * Test the inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);

        // Test some values close to zero
        double x = 0.0;
        double p = dist.cumulativeProbability(x);
        assertEquals(x, dist.inverseCumulativeProbability(p), 1e-8);

        x = 0.01;
        p = dist.cumulativeProbability(x);
        assertEquals(x, dist.inverseCumulativeProbability(p), 1e-8);

        // Test some values close to 1
        x = 1.0 - 1e-5;
        p = dist.cumulativeProbability(x);
        assertEquals(x, dist.inverseCumulativeProbability(p), 1e-8);

        x = 1.0;
        p = dist.cumulativeProbability(x);
        assertTrue(Double.isNaN(dist.inverseCumulativeProbability(p)));

        // Test some values between
        x = 0.5;
        p = dist.cumulativeProbability(x);
        assertEquals(x, dist.inverseCumulativeProbability(p), 1e-8);

        // Test extreme values
        x = Double.NEGATIVE_INFINITY;
        p = dist.cumulativeProbability(x);
        assertTrue(Double.isNaN(dist.inverseCumulativeProbability(p)));

        x = Double.POSITIVE_INFINITY;
        p = dist.cumulativeProbability(x);
        assertTrue(Double.isNaN(dist.inverseCumulativeProbability(p)));
    }

    /**
     * Test the numerical mean method.
     */
    @Test
    public void testNumericalMean() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);

        final double mean = dist.getNumericalMean();
        final double expected = 3.0 / 2.0;
        assertEquals(expected, mean, 1e-6);
    }

    /**
     * Test the numerical variance method.
     */
    @Test
    public void testNumericalVariance() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);

        final double variance = dist.getNumericalVariance();
        final double expected = 12.0 / 5.0;
        assertEquals(expected, variance, 1e-6);
    }

    /**
     * Test the random generation.
     */
    @Test
    public void testRandomGeneration() {
        final int seed = 1234;
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution dist = new FDistribution(new Well19937c(seed),
                                                    numeratorDegreesOfFreedom,
                                                    denominatorDegreesOfFreedom);

        final int samplesSize = 10000;
        final double[] samples = new double[samplesSize];
        for (int i = 0; i < samplesSize; ++i) {
            samples[i] = dist.inverseCumulativeProbability(dist.randomValue());
        }

        // Check mean
        final double mean = dist.getNumericalMean();
        double sum = 0.0;
        for (double sample : samples) {
            sum += sample;
        }
        assertEquals(mean, sum / samplesSize, 1e-6);

        // Check variance
        final double variance = dist.getNumericalVariance();
        double varianceSampled = 0.0;
        for (double sample : samples) {
            varianceSampled += Math.pow(sample - mean, 2);
        }
        assertEquals(variance, varianceSampled / samplesSize, 1e-6);
    }
}