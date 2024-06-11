package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.0);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.3, dist.getUpperBound(), 0.0);
        assertEquals(0.2, dist.getNumericalMean(), 0.0);
        assertEquals(0.0013888888888889, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (0.3 - 0.1), dist.density(0.2), 0.0);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        for (int i = 0; i < 100; i++) {
            double x = dist.inverseCumulativeProbability(RandomUtils.nextDouble(0.0, 1.0));
            assertTrue(x >= 0.1 && x <= 0.3);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        for (int i = 0; i < 100; i++) {
            double sampledValue = dist.sample();
            assertTrue(sampledValue >= 0.1 && sampledValue <= 0.3);
        }
    }

    @Test
    public void testUniformRealDistributionRandomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0.1, 0.3);
        for (int i = 0; i < 100; i++) {
            double sampledValue = dist.sample();
            assertTrue(sampledValue >= 0.1 && sampledValue <= 0.3);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(0.3, 0.1);
    }
}