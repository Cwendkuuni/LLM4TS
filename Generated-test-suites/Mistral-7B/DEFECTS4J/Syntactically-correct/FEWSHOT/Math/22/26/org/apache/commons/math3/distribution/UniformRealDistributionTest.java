package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getLowerBound(), 0.0);
        assertEquals(1.0, urd.getUpperBound(), 0.0);
        assertEquals(0.5, urd.getNumericalMean(), 0.0);
        assertEquals(0.25, urd.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution(1.5, 3.5);
        assertEquals(1.5, urd.getLowerBound(), 0.0);
        assertEquals(3.5, urd.getUpperBound(), 0.0);
        assertEquals(2.55, urd.getNumericalMean(), 0.01);
        assertEquals(0.7125, urd.getNumericalVariance(), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidLowerAndUpperBounds() {
        new UniformRealDistribution(3.5, 1.5);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithInverseCumulativeProbabilityAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(1.5, 3.5, 1e-7);
        assertEquals(1.5, urd.getLowerBound(), 0.0);
        assertEquals(3.5, urd.getUpperBound(), 0.0);
        assertEquals(2.55, urd.getNumericalMean(), 0.001);
        assertEquals(0.7125, urd.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionWithRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(rng, 1.5, 3.5);
        assertEquals(1.5, urd.getLowerBound(), 0.0);
        assertEquals(3.5, urd.getUpperBound(), 0.0);
        assertEquals(2.55, urd.getNumericalMean(), 0.01);
        assertEquals(0.7125, urd.getNumericalVariance(), 0.001);

        double sample = urd.sample();
        assertTrue(sample >= 1.5 && sample < 3.5);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double density = urd.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 1e-5);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double cdf = urd.cumulativeProbability(x);
            assertEquals(x, cdf, 1e-5);
        }
    }
}