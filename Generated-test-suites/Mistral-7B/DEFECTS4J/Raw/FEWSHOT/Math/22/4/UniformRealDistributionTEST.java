package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.density(0.0), 0.001);
        assertEquals(0.0, dist.density(1.0), 0.001);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.001);

        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);

        try {
            new UniformRealDistribution(1.0, 0.0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        UniformRealDistribution dist1 = new UniformRealDistribution(0.1, 0.4);
        double sample = dist1.sample();
        assertTrue(sample >= 0.1 && sample < 0.4);
    }

    @Test
    public void testUniformRealDistributionWithGenerator() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.1, 0.4);
        double sample = dist.sample();
        assertTrue(sample >= 0.1 && sample < 0.4);
    }

    @Test
    public void testUniformRealDistributionMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.01);
    }

    @Test
    public void testUniformRealDistributionVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.01);
    }

    @Test
    public void testUniformRealDistributionWithCustomAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4, 1e-6);
        double sample = dist.sample();
        assertTrue(sample >= 0.1 && sample <= 0.4);
        assertEquals(0.25, dist.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionWithCustomAccuracyAndGenerator()
            throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.1, 0.4, 1e-6);
        double sample = dist.sample();
        assertTrue(sample >= 0.1 && sample <= 0.4);
        assertEquals(0.25, dist.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionLowerSupport() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionUpperSupport() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionSupportBoundsAreInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertTrue(dist.isSupportUpperBoundInclusive() == false);
    }

    @Test
    public void testUniformRealDistributionSupportIsConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionSampleMultipleTimes() {
        UniformRealDistribution dist = new UniformRealDistribution();
        int n = 10000;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += dist.sample();
        }
        assertEquals(n * (upper - lower) / 2.0, sum, 1e-6 * (n * (upper - lower) / 2.0));
    }

    @Test
    public void testUniformRealDistributionWithCustomAccuracySampleMultipleTimes()
            throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4, 1e-6);
        int n = 10000;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += dist.sample();
        }
        assertEquals(n * (upper - lower) / 2.0, sum, 1e-6 * (n * (upper - lower) / 2.0));
    }

    @Test
    public void testUniformRealDistributionWithGeneratorSampleMultipleTimes()
            throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.1, 0.4);
        int n = 10000;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += dist.sample();
        }
        assertEquals(n * (upper - lower) / 2.0, sum, 1e-6 * (n * (upper - lower) / 2.0));
    }
}