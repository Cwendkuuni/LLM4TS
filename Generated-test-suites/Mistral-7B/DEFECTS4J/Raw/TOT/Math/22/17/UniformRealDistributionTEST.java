package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionLowerUpper() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.5);
        double[] values = new double[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.sample();
        }
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        double mean = sum / values.length;
        double variance = 0;
        for (int i = 0; i < values.length; i++) {
            variance += Math.pow(values[i] - mean, 2);
        }
        variance /= values.length;
        assertEquals(0.3, mean, 0.001);
        assertEquals(0.0013636363636363636, variance, 0.00001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = null;
        try {
            dist = new UniformRealDistribution(rng, 5.0, 3.0);
        } catch (NumberIsTooLargeException e) {
            // expected
        }
        fail("Expected NumberIsTooLargeException was not thrown");
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            double expected = 1.0 / (1.0 - 0.0);
            assertEquals(expected, density, 1e-5);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            double expected = x;
            assertEquals(expected, cdf, 1e-5);
        }
    }

    @Test
    public void testUniformRealDistributionLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 0.5, 0.001);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionUpperBoundExclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 0.5, 0.001);
        assertEquals(false, dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 0.5, 0.001);
        assertEquals(true, dist.isSupportConnected());
    }
}