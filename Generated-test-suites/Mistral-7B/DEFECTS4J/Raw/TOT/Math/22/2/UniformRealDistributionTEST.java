package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /** Tests construction with default bounds. */
    @Test
    public void testDefaultConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-11);
        assertEquals(1.0, distribution.getSupportUpperBound(), 1e-11);
        assertEquals(0.5, distribution.getNumericalMean(), 1e-11);
        assertEquals(0.25, distribution.getNumericalVariance(), 1e-11);
    }

    /** Tests construction with custom bounds. */
    @Test
    public void testCustomConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        assertEquals(0.1, distribution.getSupportLowerBound(), 1e-11);
        assertEquals(0.3, distribution.getSupportUpperBound(), 1e-11);
        assertEquals(0.2, distribution.getNumericalMean(), 1e-11);
        assertEquals(0.0045, distribution.getNumericalVariance(), 1e-11);
    }

    /** Tests construction with invalid bounds. */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstruction() {
        new UniformRealDistribution(1.0, 1.0);
    }

    /** Tests density calculation. */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.density(1.1), 1e-9);
        assertEquals(1.0 / (1.0 - 0.0), distribution.density(0.5), 1e-9);
        assertEquals(1.0 / (1.0 - 0.0), distribution.density(0.499), 1e-9);
        assertEquals(1.0 / (1.0 - 0.0), distribution.density(0.501), 1e-9);
        assertEquals(0.444444444444, distribution.density(0.25), 1e-9);
    }

    /** Tests cumulative probability calculation. */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 1e-9);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-9);
        assertEquals(1.0, distribution.cumulativeProbability(1.0), 1e-9);
        assertEquals(0.95, distribution.cumulativeProbability(0.95), 1e-9);
        assertEquals(0.05, distribution.cumulativeProbability(0.05), 1e-9);
    }

    /** Tests inverse cumulative probability calculation. */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 1e-9);
        assertEquals(0.5, distribution.inverseCumulativeProbability(0.5), 1e-9);
        assertEquals(1.0, distribution.inverseCumulativeProbability(1.0), 1e-9);
        assertEquals(0.045, distribution.inverseCumulativeProbability(0.045), 1e-9);
        assertEquals(0.955, distribution.inverseCumulativeProbability(0.955), 1e-9);
    }

    /** Tests sampling. */
    @Test
    public void testSampling() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample(rng);
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    /** Tests get methods. */
    @Test
    public void testGetMethods() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-11);
        assertEquals(1.0, distribution.getSupportUpperBound(), 1e-11);
        assertEquals(0.5, distribution.getNumericalMean(), 1e-11);
        assertEquals(0.25, distribution.getNumericalVariance(), 1e-11);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
        assertEquals(true, distribution.isSupportConnected());
    }
}