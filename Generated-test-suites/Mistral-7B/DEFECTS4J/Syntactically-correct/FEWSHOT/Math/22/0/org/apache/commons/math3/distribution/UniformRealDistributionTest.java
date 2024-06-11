package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionZeroOne() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.5, urd.density(0.5), 0.001);
        assertEquals(1 / (1 - 0), urd.density(0.99), 0.001);
        assertEquals(1 / (1 - 0), urd.density(0.01), 0.001);
        assertEquals(0, urd.density(2), 0.0);
        assertEquals(1, urd.cumulativeProbability(1), 0.001);
        assertEquals(0, urd.cumulativeProbability(0), 0.0);

        // Test mean and variance
        assertEquals(0.5, urd.getNumericalMean(), 0.001);
        assertEquals(0.0013888888888888889, urd.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testUniformRealDistributionCustomBounds() {
        double lower = 1.0;
        double upper = 3.0;
        UniformRealDistribution urd = new UniformRealDistribution(lower, upper);
        assertEquals(1 / (upper - lower), urd.density(2), 0.001);
        assertEquals(0, urd.density(lower), 0.0);
        assertEquals(1, urd.cumulativeProbability(upper), 0.001);
        assertEquals(0, urd.cumulativeProbability(lower), 0.0);

        // Test mean and variance
        assertEquals(2.0, urd.getNumericalMean(), 0.001);
        assertEquals(0.013888888888888889, urd.getNumericalVariance(), 0.00001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds() {
        new UniformRealDistribution(3.0, 2.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution urd = new UniformRealDistribution();
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 10000; i++) {
            double sample = urd.sample(rng);
            assertTrue(sample >= urd.getSupportLowerBound() && sample < urd.getSupportUpperBound());
        }
    }
}