package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionLowerAndUpperBounds() {
        double lower = 0.0;
        double upper = 5.0;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assertNotSame(lower, distribution.getSupportLowerBound());
        assertEquals(lower, distribution.getSupportLowerBound(), 0.0);
        assertEquals(upper, distribution.getSupportUpperBound(), 0.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidLowerAndUpperBounds() {
        new UniformRealDistribution(5.0, 5.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }

        UniformRealDistribution customDistribution = new UniformRealDistribution(1.0, 5.0);
        for (int i = 0; i < 100; i++) {
            double sample = customDistribution.sample();
            assertTrue(sample >= 1.0 && sample <= 5.0);
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            assertEquals(1.0 / 1.0, density, 0.001);
        }

        UniformRealDistribution customDistribution = new UniformRealDistribution(1.0, 5.0);
        for (double x = 1.0; x <= 5.0; x += 0.01) {
            double density = customDistribution.density(x);
            assertEquals(1.0 / (5.0 - 1.0), density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            assertEquals(x, cumulativeProbability, 0.001);
        }

        UniformRealDistribution customDistribution = new UniformRealDistribution(1.0, 5.0);
        for (double x = 1.0; x <= 5.0; x += 0.01) {
            double cumulativeProbability = customDistribution.cumulativeProbability(x);
            assertEquals((x - 1.0) / (5.0 - 1.0), cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);

        UniformRealDistribution customDistribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(3.0, customDistribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testUniformRealDistributionVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.000244948974278317, distribution.getNumericalVariance(), 0.000001);

        UniformRealDistribution customDistribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(2.41555373361832, customDistribution.getNumericalVariance(), 0.000001);
    }
}