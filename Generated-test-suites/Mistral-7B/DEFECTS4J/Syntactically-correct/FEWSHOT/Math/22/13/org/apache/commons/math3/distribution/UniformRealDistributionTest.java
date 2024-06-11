package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBoundsThrowsException() {
        new UniformRealDistribution(5.0, 3.0);
    }

    @Test
    public void testUniformRealDistributionConstructorWithDefaults() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertNotNull(distribution);
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
        assertEquals(1.0 / (1 - 0), distribution.density(0.5), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.001);
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertNotNull(distribution);
        assertEquals(1.0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(5.0, distribution.getSupportUpperBound(), 0.001);
        assertEquals(1.0 / (5.0 - 1.0), distribution.density(2.5), 0.001);
        assertEquals(0.6, distribution.cumulativeProbability(3.0), 0.001);
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomBoundsAndAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(3.0, 7.0, 0.0001);
        assertNotNull(distribution);
        assertEquals(3.0, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(7.0, distribution.getSupportUpperBound(), 0.0001);
        RandomGenerator rng = distribution.getRandomGenerator();
        for (int i = 0; i < 100000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 3.0 && sample < 7.0);
        }
    }

    @Test
    public void testUniformRealDistributionMethods() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(2.5, distribution.getNumericalMean(), 0.001);
        assertEquals(1.1324767924528696E-6, distribution.getNumericalVariance(), 0.0000001);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportUpperBoundExclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 1.0 && sample < 5.0);
        }
    }
}