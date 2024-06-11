package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;

import org.junit.Test;

public class UniformRealDistributionTest {
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(0, distribution.density(-1), 0);
        assertEquals(1, distribution.density(0), 1e-9);
        assertEquals(1, distribution.density(0.5), 1e-9);
        assertEquals(1, distribution.density(1), 1e-9);
        assertEquals(0, distribution.density(2), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0, distribution.cumulativeProbability(0), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-9);
        assertEquals(1, distribution.cumulativeProbability(1), 1e-9);
        assertEquals(1, distribution.cumulativeProbability(2), 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(0.5, distribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(1. / 12, distribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0 && sample < 1);
        }
    }
}